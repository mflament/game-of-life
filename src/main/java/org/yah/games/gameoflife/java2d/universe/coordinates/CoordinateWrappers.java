package org.yah.games.gameoflife.java2d.universe.coordinates;

public class CoordinateWrappers {

	private CoordinateWrappers() {}

	public static CoordinateWrapper ceate(int width) {
		if (BinaryWrapper.isPowerOfTwo(width))
			return new BinaryWrapper(width);
		//return new ModWrapper(width);
		return new RecursiveWrapper(width);
	}
}
