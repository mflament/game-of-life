package org.yah.games.gameoflife.universe.coordinates;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yah.games.gameoflife.universe.coordinates.BinaryWrapper;
import org.yah.games.gameoflife.universe.coordinates.CoordinateWrapper;
import org.yah.games.gameoflife.universe.coordinates.ModWrapper;
import org.yah.games.gameoflife.universe.coordinates.RecursiveWrapper;


public class CoordinateWrapperTest {
	@Test
	public void test_default() {
		test_wrapper(new RecursiveWrapper(8));
	}

	@Test
	public void test_binary() {
		test_wrapper(new BinaryWrapper(8));
	}

	@Test
	public void test_mod() {
		test_wrapper(new ModWrapper(8));
	}

	@Test
	public void benchmark() {
		benchmark("recursive", new RecursiveWrapper(8));
		benchmark("mod", new ModWrapper(8));
		benchmark("binary", new BinaryWrapper(8));
	}

	private static int benchmark(String name, CoordinateWrapper wrapper) {
		int total = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 500000; i++) {
			for (int j = -256; j < 256; j++) {
				total += wrapper.get(j);
			}
		}
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(name + " : " + elapsed + "ms");
		return total;
	}

	private static void test_wrapper(CoordinateWrapper wrapper) {
		assertEquals(0, wrapper.get(0));
		assertEquals(7, wrapper.get(7));

		assertEquals(7, wrapper.get(-1));
		assertEquals(6, wrapper.get(-2));
		assertEquals(0, wrapper.get(-8));
		assertEquals(0, wrapper.get(-16));

		assertEquals(0, wrapper.get(8));
		assertEquals(1, wrapper.get(9));
		assertEquals(0, wrapper.get(16));
		assertEquals(7, wrapper.get(23));
		assertEquals(0, wrapper.get(24));
	}

}
