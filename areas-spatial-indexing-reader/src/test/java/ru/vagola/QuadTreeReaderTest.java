package ru.vagola;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import ru.vagola.quadtree.QuadTreeReader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class QuadTreeReaderTest {

    @Test
    public void testSearchMaximumOfAreasInLeaf() throws IOException {
        RandomAccessFile input = new RandomAccessFile("../data/maximum_areas_in_leaf.dat", "rw");
        assertEquals(127, QuadTreeReader.searchPossibleAreas(new Point(10, 10), input).size());
    }

    @Test
    public void testSearchAreasRootSplit() throws IOException {
        RandomAccessFile input = new RandomAccessFile("../data/root_split.dat", "rw");

        Set<Short> result1 = QuadTreeReader.searchPossibleAreas(new Point(-25, 25), input);
        input.seek(0);
        Set<Short> result2 = QuadTreeReader.searchPossibleAreas(new Point(25, 25), input);
        input.seek(0);
        Set<Short> result3 = QuadTreeReader.searchPossibleAreas(new Point(-25, -25), input);
        input.seek(0);
        Set<Short> result4 = QuadTreeReader.searchPossibleAreas(new Point(25, -25), input);

        assertEquals(ImmutableSet.of((short) 1), result1);
        assertEquals(ImmutableSet.of((short) 2), result2);
        assertEquals(ImmutableSet.of((short) 3), result3);
        assertEquals(ImmutableSet.of((short) 4), result4);
    }

}
