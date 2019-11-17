package ru.vagola.node;

import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Point;
import ru.vagola.QuadTreeConfig;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LeafNode implements QuadTreeNode {

    private final Map<Short, Area> areas = new HashMap<>();
    private final int level;
    private final BoundingBox boundingBox;
    private final QuadTreeConfig config;

    LeafNode(BoundingBox boundingBox, QuadTreeConfig config) {
        this(1, boundingBox, config);
    }

    LeafNode(int level, BoundingBox boundingBox, QuadTreeConfig config) {
        this.level = level;
        this.boundingBox = boundingBox;
        this.config = config;
    }

    public Map<Short, Area> getAreas() {
        return Collections.unmodifiableMap(areas);
    }

    @Override
    public QuadTreeNode putArea(Area area) {
        if (!boundingBox.containsBoundingBox(area.getBoundingBox())) {
            return this;
        }

        // This limit comes from maximum binary node data length of 255 bytes.
        if (areas.size() == 127) {
            throw new IllegalStateException("Leaf node can't have more than 127 areas");
        }

        areas.put(area.getId(), area);

        if (config.getMaxLevel() != level && config.getAreasToSplit() == areas.size()) {
            Point distance = boundingBox.getDistance();

            int distanceX = distance.getX();
            int distanceY = distance.getY();

            if (distanceX > config.getMinimalDistanceToSplit() && distanceY > config.getMinimalDistanceToSplit()) {
                EdgeNode edgeNode = new EdgeNode(level + 1, boundingBox, config);

                for (Area other : areas.values()) {
                    edgeNode.putArea(other);
                }

                return edgeNode;
            }
        }

        return this;
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
