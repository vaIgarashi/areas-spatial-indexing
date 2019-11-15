package ru.vagola;

public class QuadTreeConfig {

    public static final int DEFAULT_NODES_TO_SPLIT = 4;
    public static final int DEFAULT_MAX_LEVEL = 12;
    public static final int MINIMAL_DISTANCE_TO_SPLIT = 5;

    private final int nodesToSplit;
    private final int maxLevel;
    private final int minimalDistanceToSplit;

    public QuadTreeConfig() {
        this(DEFAULT_NODES_TO_SPLIT, DEFAULT_MAX_LEVEL, MINIMAL_DISTANCE_TO_SPLIT);
    }

    public QuadTreeConfig(int nodesToSplit, int maxLevel, int minimalDistanceToSplit) {
        this.nodesToSplit = nodesToSplit;
        this.maxLevel = maxLevel;
        this.minimalDistanceToSplit = minimalDistanceToSplit;
    }

    public int getNodesToSplit() {
        return nodesToSplit;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMinimalDistanceToSplit() {
        return minimalDistanceToSplit;
    }

}
