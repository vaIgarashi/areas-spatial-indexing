package ru.vagola;

public class Area {

    private final int id;
    private final BoundingBox boundingBox;

    public Area(int id, BoundingBox boundingBox) {
        this.id = id;
        this.boundingBox = boundingBox;
    }

    public int getId() {
        return id;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
