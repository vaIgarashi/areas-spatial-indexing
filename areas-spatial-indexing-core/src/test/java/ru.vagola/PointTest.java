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

        Point distance1 = point2.distance(point1);
        Point distance2 = point4.distance(point3);

        assertEquals(50, distance1.getX());
        assertEquals(75, distance1.getY());

        assertEquals(100, distance2.getX());
        assertEquals(225, distance2.getY());
    }

    @Test
    public void testCentral() {
        Point point1 = new Point(0, -50);
        Point point2 = new Point(50, 50);

        Point center = point1.center(point2);

        assertEquals(25, center.getX());
        assertEquals(0, center.getY());
    }

    @Test
    public void testOffset() {

    }

}
