package org.yah.games.gameoflife.universe.coordinates;

public class RecursiveWrapper implements CoordinateWrapper {

	private final int width;

	public RecursiveWrapper(int width) {
		super();
		this.width = width;
	}

	@Override
	public int get(int c) {
		if (c < 0)
			return get(width + c);
		if (c >= width)
			return get(c - width);
		return c;
	}

}