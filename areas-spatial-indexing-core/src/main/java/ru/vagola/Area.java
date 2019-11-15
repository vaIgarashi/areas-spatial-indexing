package ru.vagola;

public class Area {

    private final short id;
    private final BoundingBox boundingBox;

    public Area(short id, BoundingBox boundingBox) {
        this.id = id;
        this.boundingBox = boundingBox;
    }

    public short getId() {
        return id;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
