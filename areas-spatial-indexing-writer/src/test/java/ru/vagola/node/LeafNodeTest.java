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

public class LeafNodeTest {

    @Test(expected = IllegalStateException.class)
    public void testPutAreaLimitReached() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox = new BoundingBox(new Point(25, 25), new Point(75, 75));

        for (short i = 0; i <= 127; i++) {
            Area area = new Area(i, areaBoundingBox);
            leafNode.putArea(area);
        }
    }

    @Test
    public void testPutAreaLimitNotReached() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox = new BoundingBox(new Point(25, 25), new Point(75, 75));

        for (short i = 0; i < 127; i++) {
            Area area = new Area(i, areaBoundingBox);
            leafNode.putArea(area);
        }
    }

    @Test
    public void testWriteToBinary() throws IOException {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox = new BoundingBox(new Point(25, 25), new Point(75, 75));

        for (short i = 0; i < 127; i++) {
            Area area = new Area(i, areaBoundingBox);
            leafNode.putArea(area);
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        leafNode.writeToBinary(output);

        ByteArrayDataInput input = ByteStreams.newDataInput(output.toByteArray());

        assertEquals(254, input.readByte() & 0xFF);

        for (short i = 0; i < 127; i++) {
            assertEquals(i, input.readShort());
        }
    }

}
