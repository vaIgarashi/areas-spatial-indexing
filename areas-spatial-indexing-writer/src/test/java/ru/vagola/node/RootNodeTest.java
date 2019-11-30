package ru.vagola.node;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Point;
import ru.vagola.QuadTreeConfig;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RootNodeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testPutAreaOutOfBounds() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(250, 250));
        RootNode rootNode = new RootNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox = new BoundingBox(new Point(-250, -250), new Point(-50, -50));
        Area area = new Area((short) 1, areaBoundingBox);
        rootNode.putArea(area);
    }

    @Test
    public void testWriteToBinary() throws IOException {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(-200, -50), new Point(250, 100));
        RootNode rootNode = new RootNode(nodeBoundingBox, new QuadTreeConfig());

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        rootNode.writeToBinary(output);

        ByteArrayDataInput input = ByteStreams.newDataInput(output.toByteArray());

        assertEquals(17, output.toByteArray().length);
        assertEquals(-200, input.readInt());
        assertEquals(-50, input.readInt());
        assertEquals(250, input.readInt());
        assertEquals(100, input.readInt());
        // No areas in leaf node.
        assertEquals(0, input.readByte());
    }

    @Test
    public void testWriteToBinaryWithEvolvedNode() throws IOException {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(-100, -100), new Point(100, 100));
        RootNode rootNode = new RootNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox1 = new BoundingBox(new Point(-100, -100), new Point(-1, -1));
        Area area1 = new Area((short) 1, areaBoundingBox1);
        rootNode.putArea(area1);

        BoundingBox areaBoundingBox2 = new BoundingBox(new Point(100, 100), new Point(1, 1));
        Area area2 = new Area((short) 2, areaBoundingBox2);
        rootNode.putArea(area2);

        BoundingBox areaBoundingBox3 = new BoundingBox(new Point(-100, 100), new Point(-1, 1));
        Area area3 = new Area((short) 3, areaBoundingBox3);
        rootNode.putArea(area3);

        BoundingBox areaBoundingBox4 = new BoundingBox(new Point(100, -100), new Point(1, -1));
        Area area4 = new Area((short) 4, areaBoundingBox4);
        rootNode.putArea(area4);

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        rootNode.writeToBinary(output);

        ByteArrayDataInput input = ByteStreams.newDataInput(output.toByteArray());
        input.skipBytes(16);

        // Edge node with all children have data.
        assertEquals(31, input.readByte());
    }

}
