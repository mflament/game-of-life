package org.yah.games.gameoflife.universe.array;

import org.yah.games.gameoflife.universe.AbstractUniverse;
import org.yah.games.gameoflife.universe.State;

public class ArrayUniverse extends AbstractUniverse {
	
	private final int[] array;

	public ArrayUniverse(int width, int height) {
		super(width, height);
		array = new int[width * height];
		clear();
	}

	@Override
	public State get(int x, int y) {
		return toState(array[offset(x, y)]);
	}

	@Override
	public void set(int x, int y, State state) {
		array[offset(x, y)] = toPixel(state);
	}

	@Override
	public int[] getPixels() {
		return array;
	}

}
