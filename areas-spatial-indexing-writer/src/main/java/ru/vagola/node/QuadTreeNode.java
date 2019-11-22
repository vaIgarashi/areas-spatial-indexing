package ru.vagola.node;

import com.google.common.io.ByteArrayDataOutput;
import ru.vagola.Area;

public interface QuadTreeNode {

    QuadTreeNode putArea(Area area);

    void writeToBinary(ByteArrayDataOutput output);

}
