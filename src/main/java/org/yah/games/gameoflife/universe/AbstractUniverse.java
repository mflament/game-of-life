package org.yah.games.gameoflife.universe;

import java.awt.Color;
import java.util.Random;

import org.yah.games.gameoflife.universe.coordinates.CoordinateWrapper;
import org.yah.games.gameoflife.universe.coordinates.CoordinateWrappers;

public abstract class AbstractUniverse implements Universe {

	protected static final int[] PIXELS = { Color.BLACK.getRGB(), Color.WHITE.getRGB() };

	protected final static State[] STATES = State.values();

	protected static State toState(int pixel) {
		return STATES[pixel & 1];
	}

	protected static int toPixel(State state) {
		return PIXELS[state.ordinal()];
	}

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
	public void clear() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				set(x, y, State.OFF);
			}
		}
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

	@Override
	public void randomize(long randomSeed, float randomFactor) {
		clear();
		Random random = new Random(randomSeed);
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				float pct = random.nextFloat();
				set(x, y, pct < randomFactor ? State.ON : State.OFF);
			}
		}
	}

}
