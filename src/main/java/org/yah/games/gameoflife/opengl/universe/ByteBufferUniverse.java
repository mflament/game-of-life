package org.yah.games.gameoflife.opengl.universe;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.yah.games.gameoflife.java2d.universe.AbstractUniverse;
import org.yah.games.gameoflife.java2d.universe.State;

public class ByteBufferUniverse extends AbstractUniverse {

	private static final int PIXEL_SIZE = 3;

	private static final byte[][] PIXELS_BYTES = { { 0, 0, 0 }, { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF } };

	private static State toState(int pixel) {
		return STATES[pixel & 1];
	}

	private static byte[] toPixel(State state) {
		return PIXELS_BYTES[state.ordinal()];
	}

	private final ByteBuffer buffer;

	public ByteBufferUniverse(int width, int height) {
		this(width, height, BufferUtils.createByteBuffer(width * height * PIXEL_SIZE));
	}

	public ByteBufferUniverse(int width, int height, ByteBuffer buffer) {
		super(width, height);
		this.buffer = buffer;
	}

	@Override
	public State get(int x, int y) {
		int index = offset(x, y) * PIXEL_SIZE;
		return toState(buffer.get(index));
	}

	@Override
	public void set(int x, int y, State state) {
		int index = offset(x, y) * PIXEL_SIZE;
		buffer.position(index);
		buffer.put(toPixel(state));
	}

	@Override
	public void clear() {
		buffer.position(0);
		while (buffer.hasRemaining()) {
			buffer.put((byte) 0);
		}
	}

	@Override
	public int[] getPixels() {
		int[] res = new int[width() * height()];
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				res[offset(x, y)] = PIXELS[get(x, y).ordinal()];
			}
		}
		return res;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}

}
