package ru.vagola.node;

import ru.vagola.Area;

import java.io.DataOutput;
import java.io.IOException;

public interface QuadTreeNode {

    QuadTreeNode putArea(Area area);

    void writeToBinary(DataOutput output) throws IOException;

}
