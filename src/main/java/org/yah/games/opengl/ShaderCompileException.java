/**
 * 
 */
package org.yah.games.opengl;

/**
 * @author Marc Flament
 * @created 2019/03/27
 */
public class ShaderCompileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String source;

	public ShaderCompileException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
