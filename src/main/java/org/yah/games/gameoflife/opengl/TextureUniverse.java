package org.yah.games.gameoflife.opengl;

import java.nio.ByteBuffer;

import org.yah.games.gameoflife.universe.bytebuffer.ByteBufferUniverse;
import org.yah.games.opengl.texture.Texture2D;
import org.yah.games.opengl.texture.TextureDataType;
import org.yah.games.opengl.texture.TextureFormat;
import org.yah.games.opengl.texture.TextureInternalFormat;
import org.yah.games.opengl.texture.TextureMagFilter;
import org.yah.games.opengl.texture.TextureMinFilter;
import org.yah.games.opengl.texture.TextureWrap;

public class TextureUniverse extends ByteBufferUniverse implements AutoCloseable {

	private final Texture2D texture;

	public TextureUniverse(int width, int height) {
		super(width, height);
		texture = createUniverseTexture(width, height,getBuffer());
	}

	public void push() {
		texture.bind();
		updateData(getBuffer(), texture);
	}

	public void pull() {
		texture.bind();
		texture.getData(0, TextureFormat.RGBA, TextureDataType.UNSIGNED_BYTE, getBuffer());
	}

	@Override
	public void clear() {
		super.clear();
		if (texture != null)
			push();
	}

	@Override
	public void randomize(long randomSeed, float randomFactor) {
		super.randomize(randomSeed, randomFactor);
		push();
	}

	@Override
	public void close() throws Exception {
		texture.delete();
	}

	public Texture2D getTexture() {
		return texture;
	}

	public void delete() {
		texture.delete();
	}

	public void bind() {
		texture.bind();
	}

	public static Texture2D createUniverseTexture(int width, int height, ByteBuffer date) {
		return createTextureBuilder(width, height)
			.withData(TextureFormat.RGBA, TextureDataType.UNSIGNED_BYTE, date)
			.build();
	}

	public static Texture2D createUniverseTexture(int width, int height) {
		return createTextureBuilder(width, height).build();
	}

	private static Texture2D.Builder createTextureBuilder(int width, int height) {
		return Texture2D.builder(width, height)
			.withInternalFormat(TextureInternalFormat.RGBA)
			.wrapR(TextureWrap.REPEAT)
			.wrapS(TextureWrap.REPEAT)
			.minFilter(TextureMinFilter.NEAREST)
			.magFilter(TextureMagFilter.NEAREST);
	}

	public static void updateData(ByteBuffer buffer, Texture2D texture) {
		texture.updateData(TextureFormat.RGBA, TextureDataType.UNSIGNED_BYTE, buffer);		
	}

}
