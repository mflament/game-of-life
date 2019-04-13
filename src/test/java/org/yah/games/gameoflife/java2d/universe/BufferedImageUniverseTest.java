package org.yah.games.gameoflife.java2d.universe;

public class BufferedImageUniverseTest extends AbstractUniverseTest {

	@Override
	protected Universe createUniverse(int size) {
		return new BufferedImageUniverse(size, size);
	}

}
