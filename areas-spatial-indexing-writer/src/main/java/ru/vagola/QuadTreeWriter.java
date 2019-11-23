package ru.vagola;

import ru.vagola.node.QuadTreeNode;
import ru.vagola.node.RootNode;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

public class QuadTreeWriter {

    private final BoundingBox boundingBox;
    private final QuadTreeConfig config;

    public QuadTreeWriter(BoundingBox boundingBox, QuadTreeConfig config) {
        this.boundingBox = boundingBox;
        this.config = config;
    }

    public void writeAreas(DataOutput output, Set<Area> areas) throws IOException {
        QuadTreeNode rootNode = new RootNode(boundingBox, config);

        for (Area area : areas) {
            rootNode.putArea(area);
        }

        rootNode.writeToBinary(output);
    }

}
