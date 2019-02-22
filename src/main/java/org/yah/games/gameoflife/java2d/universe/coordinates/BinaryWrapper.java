package org.yah.games.gameoflife.java2d.universe.coordinates;

public class BinaryWrapper implements CoordinateWrapper {

	private final int mask;

	public BinaryWrapper(int width) {
		if (!isPowerOfTwo(width))
			throw new IllegalArgumentException(width + " is not a power of 2");
		this.mask = width - 1;
	}

	@Override
	public int get(int c) {
		return c & mask;
	}

	public static final boolean isPowerOfTwo(int n) {
		return (n & (n - 1)) == 0;
	}
	
	/**
	 * @see http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
	 */
	public static final int nextPowerOfTwo(int n) {
		n--;
		n |= n >> 1;
		n |= n >> 2;
		n |= n >> 4;
		n |= n >> 8;
		n |= n >> 16;
		n++;
		return n;
	}
}
