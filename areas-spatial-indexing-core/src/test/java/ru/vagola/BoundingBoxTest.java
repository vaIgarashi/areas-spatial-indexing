package ru.vagola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static ru.vagola.Quadrant.*;

public class BoundingBoxTest {

    @Test
    public void testFromPoints() {
        List<Point> points = new ArrayList<>();

        for (int i = -1000; i <= 1000; i += 5) {
            points.add(new Point(i, i));
        }

        BoundingBox boundingBox = BoundingBox.fromPoints(points);

        Point distance = boundingBox.getDistance();
        Point centralPoint = boundingBox.getCentralPoint();

        assertEquals(1000, distance.getX());
        assertEquals(1000, distance.getY());

        assertEquals(0, centralPoint.getX());
        assertEquals(0, centralPoint.getY());
    }

    @Test
    public void testContainsPoint() {
        Point minPoint = new Point(0, -5);
        Point maxPoint = new Point(5, 5);

        BoundingBox boundingBox = new BoundingBox(minPoint, maxPoint);

        assertTrue(boundingBox.containsPoint(new Point(3, 3)));
        assertTrue(boundingBox.containsPoint(new Point(3, -4)));
        assertFalse(boundingBox.containsPoint(new Point(0, -6)));
    }

    @Test
    public void testContainsBoundingBox() {
        BoundingBox boundingBox1 = new BoundingBox(new Point(0, -5), new Point(5, 5));
        BoundingBox boundingBox2 = new BoundingBox(new Point(0, -5), new Point(5, 7));
        BoundingBox boundingBox3 = new BoundingBox(new Point(-1, -4), new Point(-5, -8));

        assertTrue(boundingBox1.containsBoundingBox(boundingBox1));
        assertTrue(boundingBox1.containsBoundingBox(boundingBox2));
        assertFalse(boundingBox1.containsBoundingBox(boundingBox3));
    }

    @Test
    public void testSplitToQuadrants() {
        BoundingBox boundingBox = new BoundingBox(new Point(-200, -150), new Point(200, 150));
        Map<Quadrant, BoundingBox> quadrants = boundingBox.splitToQuadrants();
        assertEquals(4, quadrants.size());

        BoundingBox northWest = quadrants.get(NORTH_WEST);
        Point northWestCentralPoint = northWest.getCentralPoint();
        Point northWestDistance = northWest.getDistance();

        assertEquals(-100, northWestCentralPoint.getX());
        assertEquals(75, northWestCentralPoint.getY());

        assertEquals(100, northWestDistance.getX());
        assertEquals(75, northWestDistance.getY());

        BoundingBox northEast = quadrants.get(NORTH_EAST);
        Point northEastCentralPoint = northEast.getCentralPoint();
        Point northEastDistance = northEast.getDistance();

        assertEquals(100, northEastCentralPoint.getX());
        assertEquals(75, northEastCentralPoint.getY());

        assertEquals(100, northEastDistance.getX());
        assertEquals(75, northEastDistance.getY());

        BoundingBox southWest = quadrants.get(SOUTH_WEST);
        Point southWestCentralPoint = southWest.getCentralPoint();
        Point southWestDistance = southWest.getDistance();

        assertEquals(-100, southWestCentralPoint.getX());
        assertEquals(-75, southWestCentralPoint.getY());

        assertEquals(100, southWestDistance.getX());
        assertEquals(75, southWestDistance.getY());

        BoundingBox southEast = quadrants.get(SOUTH_EAST);
        Point southEastCentralPoint = southEast.getCentralPoint();
        Point southEastDistance = southEast.getDistance();

        assertEquals(100, southEastCentralPoint.getX());
        assertEquals(-75, southEastCentralPoint.getY());

        assertEquals(100, southEastDistance.getX());
        assertEquals(75, southEastDistance.getY());
    }

}
