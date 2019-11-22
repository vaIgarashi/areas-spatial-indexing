package ru.vagola.node;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Point;
import ru.vagola.QuadTreeConfig;

import static org.junit.Assert.*;

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
    public void testPutAreaOutOfBounds() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(250, 250));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox = new BoundingBox(new Point(-250, -250), new Point(-50, -50));
        Area area = new Area((short) 1, areaBoundingBox);
        leafNode.putArea(area);

        assertTrue(leafNode.getAreas().isEmpty());
    }

    @Test
    public void testEvolveToEdgeNode() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(-100, -100), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox1 = new BoundingBox(new Point(-100, -100), new Point(-1, -1));
        Area area1 = new Area((short) 1, areaBoundingBox1);
        leafNode.putArea(area1);

        BoundingBox areaBoundingBox2 = new BoundingBox(new Point(100, 100), new Point(1, 1));
        Area area2 = new Area((short) 2, areaBoundingBox2);
        leafNode.putArea(area2);

        BoundingBox areaBoundingBox3 = new BoundingBox(new Point(-100, 100), new Point(-1, 1));
        Area area3 = new Area((short) 3, areaBoundingBox3);
        leafNode.putArea(area3);

        BoundingBox areaBoundingBox4 = new BoundingBox(new Point(100, -100), new Point(1, -1));
        Area area4 = new Area((short) 4, areaBoundingBox4);

        QuadTreeNode evolvedNode = leafNode.putArea(area4);
        assertTrue(evolvedNode instanceof EdgeNode);
    }

    @Test
    public void testNotReachedRequiredAreasAmountToSplit() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig().setAreasToSplit(150));

        BoundingBox areaBoundingBox = new BoundingBox(new Point(25, 25), new Point(75, 75));

        for (short i = 0; i < 127; i++) {
            Area area = new Area(i, areaBoundingBox);
            assertSame(leafNode.putArea(area), leafNode);
        }
    }

    @Test
    public void testNotSplitAtMaxLevel() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(0, 0), new Point(100, 100));
        LeafNode leafNode = new LeafNode(nodeBoundingBox, new QuadTreeConfig().setMaxLevel(1));

        BoundingBox areaBoundingBox = new BoundingBox(new Point(25, 25), new Point(75, 75));

        for (short i = 0; i < 127; i++) {
            Area area = new Area(i, areaBoundingBox);
            assertSame(leafNode.putArea(area), leafNode);
        }
    }

    @Test
    public void testWriteToBinary() {
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

        assertEquals(254, input.readUnsignedByte());

        for (short i = 0; i < 127; i++) {
            assertEquals(i, input.readShort());
        }
    }

}
