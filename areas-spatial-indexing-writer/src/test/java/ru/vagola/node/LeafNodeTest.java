package ru.vagola.node;

import org.junit.Test;
import ru.vagola.Area;
import ru.vagola.BoundingBox;
import ru.vagola.Point;
import ru.vagola.QuadTreeConfig;

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

}
