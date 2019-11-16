package ru.vagola.node;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ru.vagola.Area;
import ru.vagola.Quadrant;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class EdgeNode implements QuadTreeNode {

    private final Map<Quadrant, QuadTreeNode> children;

    EdgeNode(Map<Quadrant, QuadTreeNode> children) {
        this.children = children;
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

            ByteArrayDataOutput childrenOutput = ByteStreams.newDataOutput();
            childNode.writeToBinary(childrenOutput);

            byte[] outputByteArray = childrenOutput.toByteArray();

            // We write child data only if it have more than just node info.
            if (outputByteArray.length > 1) {
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

                childrenBytes[quadrant.ordinal()] = outputByteArray;
                output.writeByte(outputByteArray.length);
            }
        }

        output.writeByte(nodeInfo);

        for (byte[] childBytes : childrenBytes) {
            output.write(childBytes);
        }
    }

}
