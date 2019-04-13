package org.yah.games.gameoflife.java2d.universe;

public interface Universe {

	int width();

	int height();

	State get(int x, int y);

	void set(int x, int y, State state);

	int neighbors(int x, int y);
	
	void clear();

	int[] getPixels();

}
