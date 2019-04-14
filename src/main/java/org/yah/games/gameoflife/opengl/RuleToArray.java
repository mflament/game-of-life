package org.yah.games.gameoflife.opengl;

import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.universe.State;

public final class RuleToArray {

	private RuleToArray() {}

	public static String createFloatArrays(Rule rule) {
		StringBuilder sb = new StringBuilder();
		sb.append("const float rule[18] = ");
		sb.append("float[](");
		for (int i = 0; i < Rule.NEIGHBORS_POSIBILITIES; i++) {
			State s = rule.evaluate(State.OFF, i);
			sb.append(s == State.ON ? "1.0" : "0.0");
			sb.append(", ");
		}

		for (int i = 0; i < Rule.NEIGHBORS_POSIBILITIES; i++) {
			State s = rule.evaluate(State.ON, i);
			sb.append(s == State.ON ? "1.0" : "0.0");
			if (i < Rule.NEIGHBORS_POSIBILITIES - 1)
				sb.append(", ");
		}

		sb.append(");");

		return sb.toString();
	}

}
