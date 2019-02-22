package org.yah.games.gameoflife.java2d.universe;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class BufferedImageUniverseTest {

	private static final int SIZE = 16;

	private final static int PIXEL_OFF = Color.BLACK.getRGB();

	private final static int PIXEL_ON = Color.WHITE.getRGB();

	private BufferedImageUniverse universe;

	@Before
	public void setupUniverse() {
		universe = new BufferedImageUniverse(SIZE, SIZE);
	}

	@Test
	public void test_default_state() {
		assertArrayEquals(emptyScreen(), universe.getPixels());
	}

	@Test
	public void test_pixels() {
		universe.set(0, 0, State.ON);
		assertEquals(State.ON, universe.get(0, 0));
		int[] expectedPixels = emptyScreen();
		expectedPixels[0] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());

		universe.set(0, 0, State.OFF);
		assertEquals(State.OFF, universe.get(0, 0));
		assertArrayEquals(emptyScreen(expectedPixels), universe.getPixels());

		universe.clear();
		universe.set(0, 1, State.ON);
		assertEquals(State.ON, universe.get(0, 1));
		emptyScreen(expectedPixels)[SIZE] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());

		universe.clear();
		universe.set(-1, 0, State.ON);
		assertEquals(State.ON, universe.get(-1, 0));
		assertEquals(State.ON, universe.get(SIZE - 1, 0));
		emptyScreen(expectedPixels)[SIZE - 1] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());

		universe.clear();
		universe.set(SIZE, 0, State.ON);
		assertEquals(State.ON, universe.get(SIZE, 0));
		assertEquals(State.ON, universe.get(0, 0));
		emptyScreen(expectedPixels)[0] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());

		universe.clear();
		universe.set(0, -1, State.ON);
		assertEquals(State.ON, universe.get(0, -1));
		assertEquals(State.ON, universe.get(0, SIZE - 1));
		emptyScreen(expectedPixels)[(SIZE - 1) * SIZE] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());

		universe.clear();
		universe.set(0, SIZE, State.ON);
		assertEquals(State.ON, universe.get(0, SIZE));
		assertEquals(State.ON, universe.get(0, 0));
		emptyScreen(expectedPixels)[0] = PIXEL_ON;
		assertArrayEquals(expectedPixels, universe.getPixels());
	}

	@Test
	public void test_neighbors() {
		universe.set(SIZE - 1, 0, State.ON);
		universe.set(1, 0, State.ON);
		universe.set(1, 1, State.ON);
		universe.set(1, SIZE - 1, State.ON);
		printPixels(universe.getPixels());

		assertEquals(4, universe.neighbors(0, 0));
		assertEquals(2, universe.neighbors(1, 0));
		assertEquals(3, universe.neighbors(0, SIZE - 1));
		assertEquals(1, universe.neighbors(1, SIZE - 1));
		assertEquals(3, universe.neighbors(2, 0));
		assertEquals(0, universe.neighbors(3, 0));
		assertEquals(1, universe.neighbors(SIZE - 2, 0));
		assertEquals(1, universe.neighbors(SIZE - 1, SIZE - 1));
	}

	private static final int[] emptyScreen() {
		return emptyScreen(new int[SIZE * SIZE]);
	}

	private static final int[] emptyScreen(int[] target) {
		Arrays.fill(target, PIXEL_OFF);
		return target;
	}

	private static void printPixels(int[] pixels) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				sb.append(pixels[y * SIZE + x] == PIXEL_ON ? "1 " : "0 ");
			}
			sb.append(System.lineSeparator());
		}
		System.out.println(sb);
	}

}
