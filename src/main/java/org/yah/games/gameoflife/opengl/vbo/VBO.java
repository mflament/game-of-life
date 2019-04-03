package org.yah.games.gameoflife.opengl.vbo;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import org.yah.games.gameoflife.opengl.GLObject;


public class VBO extends GLObject {

	private VBO(int vboId) {
		super(vboId);
	}

	public static class Builder {

		private final Target target;

		private final int id;

		public Builder() {
			this(Target.ARRAY);
		}

		public Builder(Target target) {
			this.target = target;
			id = glGenBuffers();
			glBindBuffer(target.getGlTarget(), id);
		}

		public Builder withData(ByteBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(short[] data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(ShortBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(int[] data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(IntBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(float[] data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(FloatBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(double[] data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(DoubleBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(long[] data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withData(LongBuffer data, BufferAccess access) {
			glBufferData(target.getGlTarget(), data, access.glUsage());
			return this;
		}

		public Builder withSize(long size, BufferAccess access) {
			glBufferData(target.getGlTarget(), size, access.glUsage());
			return this;
		}
	
		public VBO build() {
			glBindBuffer(target.getGlTarget(), 0);
			return new VBO(id);
		}
	}
}
