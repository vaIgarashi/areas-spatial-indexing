package ru.vagola.node;

import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Point;
import ru.vagola.QuadTreeConfig;

import java.io.DataOutput;
import java.io.IOException;

public class RootNode implements QuadTreeNode {

    private final BoundingBox boundingBox;
    private QuadTreeNode childNode;

    public RootNode(BoundingBox boundingBox, QuadTreeConfig config) {
        this.boundingBox = boundingBox;
        this.childNode = new LeafNode(boundingBox, config);
    }

    @Override
    public QuadTreeNode putArea(Area area) {
        if (!boundingBox.containsBoundingBox(area.getBoundingBox())) {
            throw new IllegalArgumentException(String.format("Area with id %d are out of bounding box", area.getId()));
        }

        QuadTreeNode newChildNode = childNode.putArea(area);

        if (newChildNode != childNode) {
            childNode = newChildNode;
        }

        return this;
    }

    @Override
    public void writeToBinary(DataOutput output) throws IOException {
        Point minPoint = boundingBox.getMinPoint();
        Point maxPoint = boundingBox.getMaxPoint();

        output.writeInt(minPoint.getX());
        output.writeInt(minPoint.getY());
        output.writeInt(maxPoint.getX());
        output.writeInt(maxPoint.getY());

        childNode.writeToBinary(output);
    }

}
