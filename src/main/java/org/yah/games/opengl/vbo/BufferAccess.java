package org.yah.games.opengl.vbo;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

public class BufferAccess {

	public enum Frequency {
		STREAM(GL_STREAM_DRAW),
		STATIC(GL_STATIC_DRAW),
		DYNAMIC(GL_DYNAMIC_DRAW);

		private final int baseUsage;

		private Frequency(int baseUsage) {
			this.baseUsage = baseUsage;
		}

	}

	public enum Nature {
		DRAW,
		READ,
		COPY;
	}

	private final Frequency frequency;

	private final Nature nature;

	private BufferAccess(Frequency frequency, Nature nature) {
		super();
		this.frequency = frequency;
		this.nature = nature;
	}

	public int glUsage() {
		return frequency.baseUsage + nature.ordinal();
	}

	public static BufferAccess from(Frequency frequency, Nature nature) {
		return new BufferAccess(frequency, nature);
	}
}
