/**
 * 
 */
package org.yah.games.gameoflife.opengl;

/**
 * @author Oodrive
 * @created 2019/03/29
 */
public abstract class GLObject {
	
	protected final int id;

	protected GLObject(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
