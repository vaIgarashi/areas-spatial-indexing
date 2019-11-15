package ru.vagola.node;

import ru.vagola.*;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LeafNode implements QuadTreeNode {

    private final Map<Short, Area> areas = new HashMap<>();
    private final int level;
    private final BoundingBox boundingBox;
    private final QuadTreeConfig config;

    public LeafNode(BoundingBox boundingBox, QuadTreeConfig config) {
        this(1, boundingBox, config);
    }

    private LeafNode(int level, BoundingBox boundingBox, QuadTreeConfig config) {
        this.level = level;
        this.boundingBox = boundingBox;
        this.config = config;
    }

    @Override
    public QuadTreeNode putArea(Area area) {
        // In 1 byte header we can use only 7 bits for areas size.
        if (areas.size() == 255) {
            throw new IllegalStateException("Leaf node can't have more than 255 areas");
        }

        areas.put(area.getId(), area);

        if (config.getMaxLevel() != level && config.getNodesToSplit() == areas.size()) {
            Point distance = boundingBox.getDistance();

            int distanceX = distance.getX();
            int distanceY = distance.getY();

            if (config.getMinimalDistanceToSplit() > distanceX && config.getMinimalDistanceToSplit() > distanceY) {
                return evolveToEdgeNode();
            }
        }

        return this;
    }

    private QuadTreeNode evolveToEdgeNode() {
        Map<Quadrant, QuadTreeNode> children = new HashMap<>();

        for (Entry<Quadrant, BoundingBox> entry : boundingBox.splitToQuadrants().entrySet()) {
            children.put(entry.getKey(), new LeafNode(level + 1, entry.getValue(), config));
        }

        for (Area area : areas.values()) {
            for (Entry<Quadrant, QuadTreeNode> entry : children.entrySet()) {
                Quadrant quadrant = entry.getKey();
                QuadTreeNode node = entry.getValue();

                if (boundingBox.containsBoundingBox(area.getBoundingBox())) {
                    QuadTreeNode newNode = node.putArea(area);

                    // Children node may evolve to edge node in case all areas are placed in one quadrant.
                    if (node != newNode) {
                        children.put(quadrant, newNode);
                    }
                }
            }
        }

        return new EdgeNode(children, boundingBox);
    }

    @Override
    public void writeToBinary(DataOutput output) throws IOException {
        // Lowest bit used as leaf node type.
        output.writeByte((areas.size() & 0x7F) << 1);

        for (short areaId : areas.keySet()) {
            output.writeShort(areaId);
        }
    }

}
