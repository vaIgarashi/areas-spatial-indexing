package ru.vagola;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QuadTreeReader {

    public static Set<Short> searchPossibleAreas(Point point, byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        BoundingBox boundingBox = readBoundingBox(input);

        if (!boundingBox.containsPoint(point)) {
            return Collections.emptySet();
        }

        return searchAreasInNode(input, point, boundingBox);
    }

    private static BoundingBox readBoundingBox(ByteArrayDataInput input) {
        int centralPointX = input.readInt();
        int centralPointY = input.readInt();

        int distanceX = input.readInt();
        int distanceY = input.readInt();

        Point minPoint = new Point(centralPointX - distanceX, centralPointY - distanceY);
        Point maxPoint = new Point(centralPointX + distanceX, centralPointY + distanceY);

        return new BoundingBox(minPoint, maxPoint);
    }

    private static Set<Short> searchAreasInNode(ByteArrayDataInput input, Point point, BoundingBox boundingBox) {
        byte nodeInfo = input.readByte();
        int nodeType = nodeInfo & 0x01;

        switch (nodeType) {
            case 0:
                return searchAreasInDataNode(input, nodeInfo);
            case 1:
                return moveTroughEdgeNode(input, nodeInfo, point, boundingBox);
            default:
                throw new IllegalArgumentException(String.format("Unknown node type %d", nodeType));
        }
    }

    private static Set<Short> searchAreasInDataNode(ByteArrayDataInput input, byte nodeInfo) {
        Set<Short> possibleAreas = new HashSet<>();
        int areasAmount = nodeInfo >> 1;

        for (int i = 0; i < areasAmount; i++) {
            possibleAreas.add(input.readShort());
        }

        return possibleAreas;
    }

    private static Set<Short> moveTroughEdgeNode(ByteArrayDataInput input, byte nodeInfo, Point point, BoundingBox boundingBox) {
        Point diff = point.subtract(boundingBox.getCentralPoint());
        Quadrant quadrant = diff.determineQuadrant();
        int currentOffset = 0;

        for (Quadrant other : Quadrant.values()) {
            int value = 2 << (quadrant.ordinal());

            if ((nodeInfo & value) == value) {
                if (other == quadrant) {
                    input.skipBytes(currentOffset);
                    return searchAreasInNode(input, point, boundingBox.splitQuadrant(quadrant));
                }

                currentOffset += input.readUnsignedByte();
            }
        }

        return Collections.emptySet();
    }

}
