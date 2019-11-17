package ru.vagola;

public class QuadTreeConfig {

    public static final int DEFAULT_AREAS_TO_SPLIT = 4;
    public static final int DEFAULT_MAX_LEVEL = 12;
    public static final int MINIMAL_DISTANCE_TO_SPLIT = 5;

    private final int areasToSplit;
    private final int maxLevel;
    private final int minimalDistanceToSplit;

    public QuadTreeConfig() {
        this(DEFAULT_AREAS_TO_SPLIT, DEFAULT_MAX_LEVEL, MINIMAL_DISTANCE_TO_SPLIT);
    }

    public QuadTreeConfig(int areasToSplit, int maxLevel, int minimalDistanceToSplit) {
        this.areasToSplit = areasToSplit;
        this.maxLevel = maxLevel;
        this.minimalDistanceToSplit = minimalDistanceToSplit;
    }

    public QuadTreeConfig setAreasToSplit(int areasToSplit) {
        return new QuadTreeConfig(areasToSplit, this.maxLevel, this.minimalDistanceToSplit);
    }

    public QuadTreeConfig setMaxLevel(int maxLevel) {
        return new QuadTreeConfig(this.areasToSplit, maxLevel, this.minimalDistanceToSplit);
    }

    public QuadTreeConfig setMinimalDistanceToSplit(int minimalDistanceToSplit) {
        return new QuadTreeConfig(this.areasToSplit, this.maxLevel, minimalDistanceToSplit);
    }

    public int getAreasToSplit() {
        return areasToSplit;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMinimalDistanceToSplit() {
        return minimalDistanceToSplit;
    }

}
