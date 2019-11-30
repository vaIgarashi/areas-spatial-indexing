package ru.vagola.node;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.QuadTreeConfig;
import ru.vagola.Quadrant;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class EdgeNode implements QuadTreeNode {

    private final int level;
    private final Map<Quadrant, QuadTreeNode> children;

    private EdgeNode(int level, Map<Quadrant, QuadTreeNode> children) {
        this.level = level;
        this.children = children;
    }

    static EdgeNode createEdgeNode(int level, BoundingBox boundingBox, QuadTreeConfig config) {
        Map<Quadrant, QuadTreeNode> children = new EnumMap<>(Quadrant.class);

        for (Entry<Quadrant, BoundingBox> entry : boundingBox.splitToQuadrants().entrySet()) {
            children.put(entry.getKey(), new LeafNode(level, entry.getValue(), config));
        }

        return new EdgeNode(level, children);
    }

    public Map<Quadrant, QuadTreeNode> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    @Override
    public QuadTreeNode putArea(Area area) {
        for (Entry<Quadrant, QuadTreeNode> entry : children.entrySet()) {
            Quadrant quadrant = entry.getKey();
            QuadTreeNode childNode = entry.getValue();

            // Area may overlap several nodes.
            QuadTreeNode newNode = childNode.putArea(area);

            if (childNode != newNode) {
                children.put(quadrant, newNode);
            }
        }

        return this;
    }

    @Override
    public void writeToBinary(DataOutput output) throws IOException {
        byte[][] childrenBytes = new byte[Quadrant.values().length][];
        // Lowest bit used as edge node type.
        byte nodeInfo = 1;

        for (Entry<Quadrant, QuadTreeNode> entry : children.entrySet()) {
            Quadrant quadrant = entry.getKey();
            QuadTreeNode childNode = entry.getValue();

            if (!childNode.isEmpty()) {
                nodeInfo |= 2 << quadrant.ordinal();

                ByteArrayDataOutput childrenOutput = ByteStreams.newDataOutput();
                childNode.writeToBinary(childrenOutput);
                childrenBytes[quadrant.ordinal()] = childrenOutput.toByteArray();
            }
        }

        output.writeByte(nodeInfo);

        for (byte[] childBytes : childrenBytes) {
            if (childBytes != null) {
                output.writeByte(childBytes.length);
            }
        }

        for (byte[] childBytes : childrenBytes) {
            if (childBytes != null) {
                output.write(childBytes);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        for (QuadTreeNode node : children.values()) {
            if (!node.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (Entry<Quadrant, QuadTreeNode> entry : children.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            for(int indent = 0; indent < (level - 1) * 2; indent++) {
                output.append(" ");
            }

            output.append(entry.getKey());
            output.append("(");
            output.append(level);
            output.append("): \n");

            output.append(entry.getValue());
        }

        return output.toString();
    }

}
