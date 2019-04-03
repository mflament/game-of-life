package org.yah.games.gameoflife.opengl;

public class GameOfLife extends GL2DApplication {

	public GameOfLife() {}

	@Override
	protected String getTitle() {
		return "Game of Life";
	}
	
	@Override
	protected void render() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		new GameOfLife().start();
	}

}
