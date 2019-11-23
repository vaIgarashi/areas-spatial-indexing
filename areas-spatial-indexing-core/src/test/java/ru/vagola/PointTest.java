package ru.vagola;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {

    @Test
    public void testSubtract() {
        Point point = new Point(10, 10);
        Point subtract = point.subtract(point);

        assertEquals(0, subtract.getX());
        assertEquals(0, subtract.getY());
    }

    @Test
    public void testDistance() {
        Point point1 = new Point(50, 150);
        Point point2 = new Point(150, 300);
        Point point3 = new Point(-50, -150);
        Point point4 = new Point(150, 300);
        Point point5 = new Point(-150, -300);
        Point point6 = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Point point7 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        Point distance1 = point2.distance(point1);
        Point distance2 = point4.distance(point3);
        Point distance3 = point5.distance(point3);
        Point distance4 = point4.distance(point5);
        Point distance5 = point6.distance(point7);
        Point distance6 = point7.distance(point7);

        assertEquals(50, distance1.getX());
        assertEquals(75, distance1.getY());

        assertEquals(100, distance2.getX());
        assertEquals(225, distance2.getY());

        assertEquals(50, distance3.getX());
        assertEquals(75, distance3.getY());

        assertEquals(150, distance4.getX());
        assertEquals(300, distance4.getY());

        assertEquals(0, distance5.getX());
        assertEquals(0, distance5.getY());

        assertEquals(0, distance6.getX());
        assertEquals(0, distance6.getY());
    }

    @Test
    public void testCenter() {
        Point point1 = new Point(0, -50);
        Point point2 = new Point(50, 50);
        Point point3 = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Point point4 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        Point center1 = point1.center(point2);
        Point center2 = point3.center(point4);

        assertEquals(25, center1.getX());
        assertEquals(0, center1.getY());

        assertEquals(0, center2.getX());
        assertEquals(0, center2.getY());
    }

    @Test
    public void testDetermineQuadrant() {
        assertEquals(Quadrant.NORTH_WEST, new Point(-1, 1).determineQuadrant());
        assertEquals(Quadrant.NORTH_EAST, new Point(1, 1).determineQuadrant());
        assertEquals(Quadrant.SOUTH_WEST, new Point(-1, -1).determineQuadrant());
        assertEquals(Quadrant.SOUTH_EAST, new Point(1, -1).determineQuadrant());
    }

}
