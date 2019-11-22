package ru.vagola;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ru.vagola.node.QuadTreeNode;
import ru.vagola.node.RootNode;

import java.util.Set;

public class QuadTreeWriter {

    public static byte[] createBinaryIndex(BoundingBox boundingBox, Set<Area> areas, QuadTreeConfig config) {
        QuadTreeNode rootNode = new RootNode(boundingBox, config);

        for (Area area : areas) {
            rootNode.putArea(area);
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        rootNode.writeToBinary(output);

        return output.toByteArray();
    }

}
