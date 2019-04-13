package org.yah.games.gameoflife.java2d.universe;

import java.awt.image.DataBufferInt;

public class BufferedImageUniverse extends AbstractUniverse {

	private static State toState(int pixel) {
		return STATES[pixel & 1];
	}

	private static int toPixel(State state) {
		return PIXELS[state.ordinal()];
	}

	private DataBufferInt buffer;

	public BufferedImageUniverse(int width, int height) {
		super(width, height);
		buffer = new DataBufferInt(width * height);
		clear();
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

}
