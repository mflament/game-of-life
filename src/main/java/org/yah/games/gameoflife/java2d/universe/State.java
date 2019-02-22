package org.yah.games.gameoflife.java2d.universe;

public enum State {
	
	OFF, ON;
	
	private State() {}

	public static State toggle(State s) {
		return s == ON ? OFF : ON;
	}

}
