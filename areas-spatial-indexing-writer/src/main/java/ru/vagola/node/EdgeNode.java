package ru.vagola.node;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Quadrant;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class EdgeNode implements QuadTreeNode {

    private final Map<Quadrant, QuadTreeNode> children;
    private final BoundingBox boundingBox;

    EdgeNode(Map<Quadrant, QuadTreeNode> children, BoundingBox boundingBox) {
        this.children = children;
        this.boundingBox = boundingBox;
    }

    @Override
    public QuadTreeNode putArea(Area area) {
        for (Entry<Quadrant, QuadTreeNode> entry : children.entrySet()) {
            Quadrant quadrant = entry.getKey();
            QuadTreeNode node = entry.getValue();

            if (boundingBox.containsBoundingBox(area.getBoundingBox())) {
                // Area may overlap several nodes.
                QuadTreeNode newNode = node.putArea(area);

                if (node != newNode) {
                    children.put(quadrant, newNode);
                }
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
            QuadTreeNode child = entry.getValue();

            switch (quadrant) {
                case SOUTH_WEST:
                    nodeInfo |= 0x02;
                    break;
                case SOUTH_EAST:
                    nodeInfo |= 0x04;
                    break;
                case NORTH_WEST:
                    nodeInfo |= 0x08;
                    break;
                case NORTH_EAST:
                    nodeInfo |= 0x16;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            writeChildNode(output, childrenBytes, quadrant, child);
        }

        output.writeByte(nodeInfo);

        for (byte[] childBytes : childrenBytes) {
            output.write(childBytes);
        }
    }

    private void writeChildNode(DataOutput output, byte[][] childrenBytes, Quadrant quadrant, QuadTreeNode child) throws IOException {
        ByteArrayDataOutput childrenOutput = ByteStreams.newDataOutput();
        child.writeToBinary(childrenOutput);

        byte[] outputByteArray = childrenOutput.toByteArray();
        childrenBytes[quadrant.ordinal()] = outputByteArray;
        output.writeByte(outputByteArray.length);
    }

}
