package org.yah.games.gameoflife.universe;

public enum State {
	
	OFF, ON;
	
	private State() {}

	public static State toggle(State s) {
		return s == ON ? OFF : ON;
	}

}
