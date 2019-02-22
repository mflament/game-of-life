package org.yah.games.gameoflife.java2d.rule;

import static org.junit.Assert.*;

import org.junit.Test;


public class RuleConfigurationTest {

	@Test
	public void test_parsing() {
		RuleConfiguration configuration = new RuleConfiguration("B23/S3");
		assertArrayEquals(new int[] { 2, 3 }, configuration.getBorns());
		assertArrayEquals(new int[] { 3 }, configuration.getSurvives());

		configuration = new RuleConfiguration("B/S03");
		assertArrayEquals(new int[] {}, configuration.getBorns());
		assertArrayEquals(new int[] { 0, 3 }, configuration.getSurvives());
	}

}
