package org.yah.games.gameoflife.java2d.universe;

import java.awt.Color;
import java.awt.image.DataBufferInt;

import org.yah.games.gameoflife.java2d.universe.coordinates.CoordinateWrapper;
import org.yah.games.gameoflife.java2d.universe.coordinates.CoordinateWrappers;

public class BufferedImageUniverse implements Universe {

	private final static State[] STATES = State.values();

	private static final int[] PIXELS = { Color.BLACK.getRGB(), Color.WHITE.getRGB() };

	private final int width, height;

	private DataBufferInt buffer;

	private final CoordinateWrapper xWrapper, yWrapper;

	public BufferedImageUniverse(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		buffer = new DataBufferInt(width * height);
		xWrapper = CoordinateWrappers.ceate(width);
		yWrapper = CoordinateWrappers.ceate(height);
		clear();
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public State get(int x, int y) {
		int pixel = buffer.getElem(offset(x, y));
		return toState(pixel);
	}

	@Override
	public void set(int x, int y, State state) {
		buffer.setElem(offset(x, y), toPixel(state));
	}

	private int offset(int x, int y) {
		return yWrapper.get(y) * width + xWrapper.get(x);
	}

	@Override
	public int[] getPixels() {
		return buffer.getData();
	}

	@Override
	public void clear() {
		for (int i = 0; i < buffer.getSize(); i++) {
			buffer.setElem(i, toPixel(State.OFF));
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

	private static State toState(int pixel) {
		return STATES[pixel & 1];
	}

	private static int toPixel(State state) {
		return PIXELS[state.ordinal()];
	}

}
