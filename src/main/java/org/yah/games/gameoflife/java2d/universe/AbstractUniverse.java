package org.yah.games.gameoflife.java2d.universe;

import java.awt.Color;

import org.yah.games.gameoflife.java2d.universe.coordinates.CoordinateWrapper;
import org.yah.games.gameoflife.java2d.universe.coordinates.CoordinateWrappers;

public abstract class AbstractUniverse implements Universe {

	protected static final int[] PIXELS = { Color.BLACK.getRGB(), Color.WHITE.getRGB() };

	protected final static State[] STATES = State.values();

	private final int width, height;

	private final CoordinateWrapper xWrapper, yWrapper;

	public AbstractUniverse(int width, int height) {
		this.width = width;
		this.height = height;
		xWrapper = CoordinateWrappers.ceate(width);
		yWrapper = CoordinateWrappers.ceate(height);
	}

	@Override
	public final int width() {
		return width;
	}

	@Override
	public final int height() {
		return height;
	}

	protected final int offset(int x, int y) {
		return yWrapper.get(y) * width + xWrapper.get(x);
	}

	@Override
	public int neighbors(int x, int y) {
		int res = get(x - 1, y - 1).ordinal();
		res += get(x, y - 1).ordinal();
		res += get(x + 1, y - 1).ordinal();
		res += get(x - 1, y).ordinal();
		res += get(x + 1, y).ordinal();
		res += get(x - 1, y + 1).ordinal();
		res += get(x, y + 1).ordinal();
		res += get(x + 1, y + 1).ordinal();
		return res;
	}

}
