package org.yah.games.gameoflife.rule;

import java.util.function.Function;


public class Rules {
	
	public static final String STANDARD_DEFINITION = "B3/S23";

	public static Rule create(String definition, Function<RuleConfiguration, Rule> factory) {
		RuleConfiguration configuration = new RuleConfiguration(definition);
		return factory.apply(configuration);
	}

	private Rules() {}

}
