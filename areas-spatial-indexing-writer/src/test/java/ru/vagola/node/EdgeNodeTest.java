package ru.vagola.node;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import ru.vagola.*;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EdgeNodeTest {

    @Test
    public void testPutArea() {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(-100, -100), new Point(100, 100));
        EdgeNode edgeNode = new EdgeNode(1, nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox1 = new BoundingBox(new Point(-100, -100), new Point(-1, -1));
        Area area1 = new Area((short) 1, areaBoundingBox1);
        edgeNode.putArea(area1);

        BoundingBox areaBoundingBox2 = new BoundingBox(new Point(100, 100), new Point(1, 1));
        Area area2 = new Area((short) 2, areaBoundingBox2);
        edgeNode.putArea(area2);

        BoundingBox areaBoundingBox3 = new BoundingBox(new Point(-100, 100), new Point(-1, 1));
        Area area3 = new Area((short) 3, areaBoundingBox3);
        edgeNode.putArea(area3);

        BoundingBox areaBoundingBox4 = new BoundingBox(new Point(100, -100), new Point(1, -1));
        Area area4 = new Area((short) 4, areaBoundingBox4);
        edgeNode.putArea(area4);

        Map<Quadrant, QuadTreeNode> children = edgeNode.getChildren();
        assertEquals(Quadrant.values().length, children.size());

        LeafNode childNode1 = (LeafNode) children.get(Quadrant.SOUTH_WEST);
        Map<Short, Area> childNodeAreas1 = childNode1.getAreas();

        assertTrue(childNodeAreas1.containsKey((short) 1));
        assertEquals(1, childNodeAreas1.size());

        LeafNode childNode2 = (LeafNode) children.get(Quadrant.NORTH_EAST);
        Map<Short, Area> childNodeAreas2 = childNode2.getAreas();

        assertTrue(childNodeAreas2.containsKey((short) 2));
        assertEquals(1, childNodeAreas2.size());

        LeafNode childNode3 = (LeafNode) children.get(Quadrant.NORTH_WEST);
        Map<Short, Area> childNodeAreas3 = childNode3.getAreas();

        assertTrue(childNodeAreas3.containsKey((short) 3));
        assertEquals(1, childNodeAreas3.size());

        LeafNode childNode4 = (LeafNode) children.get(Quadrant.SOUTH_EAST);
        Map<Short, Area> childNodeAreas4 = childNode4.getAreas();

        assertTrue(childNodeAreas4.containsKey((short) 4));
        assertEquals(1, childNodeAreas4.size());
    }

    @Test
    public void testWriteToBinary() throws IOException {
        BoundingBox nodeBoundingBox = new BoundingBox(new Point(-100, -100), new Point(100, 100));
        EdgeNode edgeNode = new EdgeNode(1, nodeBoundingBox, new QuadTreeConfig());

        BoundingBox areaBoundingBox1 = new BoundingBox(new Point(-100, -100), new Point(-1, -1));
        Area area1 = new Area((short) 1, areaBoundingBox1);
        edgeNode.putArea(area1);

        BoundingBox areaBoundingBox2 = new BoundingBox(new Point(100, 100), new Point(1, 1));
        Area area2 = new Area((short) 2, areaBoundingBox2);
        edgeNode.putArea(area2);

        BoundingBox areaBoundingBox3 = new BoundingBox(new Point(-100, 100), new Point(-1, 1));
        Area area3 = new Area((short) 3, areaBoundingBox3);
        edgeNode.putArea(area3);

        BoundingBox areaBoundingBox4 = new BoundingBox(new Point(100, -100), new Point(1, -1));
        Area area4 = new Area((short) 4, areaBoundingBox4);
        edgeNode.putArea(area4);

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        edgeNode.writeToBinary(output);

        ByteArrayDataInput input = ByteStreams.newDataInput(output.toByteArray());
        assertEquals(31, input.readByte());

        for (int i = 0; i < Quadrant.values().length; i++) {
            assertEquals(3, input.readByte());
        }

        assertEquals(2, input.readByte());
        assertEquals(3, input.readShort());

        assertEquals(2, input.readByte());
        assertEquals(2, input.readShort());

        assertEquals(2, input.readByte());
        assertEquals(1, input.readShort());

        assertEquals(2, input.readByte());
        assertEquals(4, input.readShort());
    }

}
