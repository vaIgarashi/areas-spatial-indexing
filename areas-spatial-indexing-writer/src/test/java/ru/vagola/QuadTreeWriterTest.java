package ru.vagola;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class QuadTreeWriterTest {

    @Test
    public void testWriteIndexMaximumOfAreasInLeaf() throws IOException {
        BoundingBox boundingBox = new BoundingBox(new Point(-50, -50), new Point(50, 50));
        QuadTreeConfig config = new QuadTreeConfig().setMaxLevel(4);
        Set<Area> areas = new HashSet<>();

        for (short i = 0; i < 127; i++) {
            BoundingBox areaBoundingBox = new BoundingBox(new Point(1, 1), new Point(25, 25));
            areas.add(new Area(i, areaBoundingBox));
        }

        QuadTreeWriter writer = QuadTreeWriter.createWriter(boundingBox, config, areas);
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        writer.writeAreas(output);

        RandomAccessFile input = new RandomAccessFile("../data/maximum_areas_in_leaf.dat", "rw");
        //writer.writeAreas(input);
        //input.seek(0);

        byte[] inputBytes = new byte[(int) input.length()];
        input.readFully(inputBytes);

        assertEquals(2331, output.toByteArray().length);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testWriteIndexAreasRootSplit() throws IOException {
        BoundingBox boundingBox = new BoundingBox(new Point(-50, -50), new Point(50, 50));
        QuadTreeConfig config = new QuadTreeConfig();
        Set<Area> areas = new HashSet<>();

        areas.add(new Area((short) 1, new BoundingBox(new Point(-50, 50), new Point(-1, 1))));
        areas.add(new Area((short) 2, new BoundingBox(new Point(50, 50), new Point(1, 1))));
        areas.add(new Area((short) 3, new BoundingBox(new Point(-50, -50), new Point(-1, -1))));
        areas.add(new Area((short) 4, new BoundingBox(new Point(50, -50), new Point(1, -1))));

        QuadTreeWriter writer = QuadTreeWriter.createWriter(boundingBox, config, areas);
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        writer.writeAreas(output);

        RandomAccessFile input = new RandomAccessFile("../data/root_split.dat", "rw");
        //writer.writeAreas(input);
        //input.seek(0);

        byte[] inputBytes = new byte[(int) input.length()];
        input.readFully(inputBytes);

        assertEquals(33, output.toByteArray().length);
        assertArrayEquals(inputBytes, output.toByteArray());

    }

}
