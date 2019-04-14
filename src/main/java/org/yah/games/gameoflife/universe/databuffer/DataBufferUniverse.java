package org.yah.games.gameoflife.universe.databuffer;

import java.awt.image.DataBufferInt;

import org.yah.games.gameoflife.universe.AbstractUniverse;
import org.yah.games.gameoflife.universe.State;

public class DataBufferUniverse extends AbstractUniverse {

	private DataBufferInt buffer;

	public DataBufferUniverse(int width, int height) {
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

}
