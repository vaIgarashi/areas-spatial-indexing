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

        assertEquals(25, input.readInt());
        assertEquals(25, input.readInt());
        assertEquals(225, input.readInt());
        assertEquals(75, input.readInt());
    }

}
