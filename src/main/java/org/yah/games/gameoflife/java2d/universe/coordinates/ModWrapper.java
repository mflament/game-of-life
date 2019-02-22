package org.yah.games.gameoflife.java2d.universe.coordinates;

public class ModWrapper implements CoordinateWrapper {

	private final int width;

	public ModWrapper(int width) {
		super();
		this.width = width;
	}

	@Override
	public int get(int c) {
		if (c < 0)
			c += width * (-c / width + 1);
		return c % width;
	}

}
