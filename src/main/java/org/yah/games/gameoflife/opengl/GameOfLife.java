package org.yah.games.gameoflife.opengl;

public class GameOfLife extends GL2DApplication {

	public GameOfLife() {}

	@Override
	protected String getTitle() {
		return "Game of Life";
	}
	
	@Override
	protected boolean isDebugEnabled() {
		return true;
	}
	
	public static void main(String[] args) {
		new GameOfLife().start();
	}

}
