package ru.vagola;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class BoundingBox {

    private final Point minPoint;
    private final Point maxPoint;
    private final Point distance;
    private final Point centralPoint;

    public BoundingBox(Point minPoint, Point maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.distance = maxPoint.distance(minPoint);
        this.centralPoint = maxPoint.center(minPoint);

        checkArgument(distance.getX() != 0 && distance.getY() != 0, "Bounding box side must be greater than zero");
    }

    public static BoundingBox fromPoints(List<Point> points) {
        checkArgument(points.size() >= 2, "Must have at least two points in list");

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());

            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        Point minPoint = new Point(minX, minY);
        Point maxPoint = new Point(maxX, maxY);

        return new BoundingBox(minPoint, maxPoint);
    }

    public Point getMinPoint() {
        return minPoint;
    }

    public Point getMaxPoint() {
        return maxPoint;
    }

    public Point getDistance() {
        return distance;
    }

    public Point getCentralPoint() {
        return centralPoint;
    }

    public boolean containsPoint(Point point) {
        return point.getX() >= minPoint.getX() &&
                point.getX() <= maxPoint.getX() &&
                point.getY() >= minPoint.getY() &&
                point.getY() <= maxPoint.getY();
    }

    public boolean containsBoundingBox(BoundingBox other) {
        return other.maxPoint.getX() >= minPoint.getX() &&
                other.minPoint.getX() <= maxPoint.getX() &&
                other.maxPoint.getY() >= minPoint.getY() &&
                other.minPoint.getY() <= maxPoint.getY();
    }

    public Map<Quadrant, BoundingBox> splitToQuadrants() {
        Map<Quadrant, BoundingBox> quadrants = new EnumMap<>(Quadrant.class);

        for (Quadrant quadrant : Quadrant.values()) {
            quadrants.put(quadrant, splitQuadrant(quadrant));
        }

        return quadrants;
    }

    public BoundingBox splitQuadrant(Quadrant quadrant) {
        switch (quadrant) {
            case SOUTH_WEST:
                return new BoundingBox(minPoint, centralPoint);
            case SOUTH_EAST:
                return new BoundingBox(new Point(centralPoint.getX(), minPoint.getY()), new Point(maxPoint.getX(), centralPoint.getY()));
            case NORTH_WEST:
                return new BoundingBox(new Point(minPoint.getX(), centralPoint.getY()), new Point(centralPoint.getX(), maxPoint.getY()));
            case NORTH_EAST:
                return new BoundingBox(centralPoint, maxPoint);
            default:
                throw new UnsupportedOperationException();
        }
    }

}
