package org.yah.games.gameoflife.java2d.rule;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yah.games.gameoflife.java2d.universe.State;


public class RuleConfiguration {

	private static final Pattern RLE_PATERN = Pattern.compile("B(\\d*)/S(\\d*)");

	private final String definition;

	/**
	 * arrays of number of neighbors to born([0]) (0 to 8) or survives([1])
	 */
	private final int[][] matchingNeighbors = new int[2][];

	public RuleConfiguration(String rleDefintinon) {
		Matcher matcher = RLE_PATERN.matcher(rleDefintinon);
		if (matcher.matches()) {
			matchingNeighbors[State.OFF.ordinal()] = parseNeighbors(matcher.group(1));
			matchingNeighbors[State.ON.ordinal()] = parseNeighbors(matcher.group(2));
		} else {
			throw new IllegalArgumentException("Invalid RLE definition " + rleDefintinon);
		}
		this.definition = rleDefintinon;
	}

	public int[] getMatchingNeighbors(State state) {
		return matchingNeighbors[state.ordinal()];
	}
	
	public int[] getBorns() {
		return matchingNeighbors[State.OFF.ordinal()];
	}

	public int[] getSurvives() {
		return matchingNeighbors[State.ON.ordinal()];
	}

	public String getDefinition() {
		return definition;
	}

	@Override
	public String toString() {
		return definition;
	}

	private static int[] parseNeighbors(String group) {
		int[] res = new int[group.length()];
		for (int i = 0; i < res.length; i++) {
			res[i] = group.charAt(i) - '0';
			if (res[i] > Rule.MAX_NEIGHBOR_COUNT)
				throw new IllegalArgumentException(
						"Invalid neighbor count " + res[i] + ", must between [0," + Rule.MAX_NEIGHBOR_COUNT + "]");
		}
		Arrays.sort(res);
		return res;
	}
}
