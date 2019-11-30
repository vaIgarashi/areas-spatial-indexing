package ru.vagola;

import ru.vagola.node.QuadTreeNode;
import ru.vagola.node.RootNode;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

public class QuadTreeWriter {

    private final QuadTreeNode rootNode;

    private QuadTreeWriter(QuadTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public static QuadTreeWriter createWriter(BoundingBox boundingBox, QuadTreeConfig config, Set<Area> areas) {
        QuadTreeNode rootNode = new RootNode(boundingBox, config);

        for (Area area : areas) {
            rootNode.putArea(area);
        }

        return new QuadTreeWriter(rootNode);
    }

    public void writeAreas(DataOutput output) throws IOException {
        rootNode.writeToBinary(output);
    }

    public String toString() {
        return rootNode.toString().trim();
    }

}
