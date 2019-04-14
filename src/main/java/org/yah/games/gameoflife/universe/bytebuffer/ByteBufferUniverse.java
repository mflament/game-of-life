package org.yah.games.gameoflife.universe.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.yah.games.gameoflife.universe.AbstractUniverse;
import org.yah.games.gameoflife.universe.State;

public class ByteBufferUniverse extends AbstractUniverse {

	private static int bufferSize(int width, int height) {
		return width * height * 4;
	}

	private final ByteBuffer buffer;

	private final IntBuffer intBuffer;

	public ByteBufferUniverse(int width, int height) {
		this(width, height, BufferUtils.createByteBuffer(bufferSize(width, height)));
	}

	public ByteBufferUniverse(int width, int height, ByteBuffer buffer) {
		super(width, height);
		this.buffer = buffer;
		this.intBuffer = buffer.asIntBuffer();
		clear();
	}

	@Override
	public State get(int x, int y) {
		return toState(intBuffer.get(offset(x, y)));
	}

	@Override
	public void set(int x, int y, State state) {
		intBuffer.put(offset(x, y), toPixel(state));
	}

	@Override
	public int[] getPixels() {
		int[] res = new int[intBuffer.capacity()];
		intBuffer.position(0);
		intBuffer.get(res, 0, res.length);
		return res;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public IntBuffer getIntBuffer() {
		return intBuffer;
	}
}
