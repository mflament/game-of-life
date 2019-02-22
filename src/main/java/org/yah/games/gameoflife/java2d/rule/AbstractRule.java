package org.yah.games.gameoflife.java2d.rule;

public abstract class AbstractRule implements Rule {

	protected final RuleConfiguration configuration;

	public AbstractRule(RuleConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + configuration.toString() + "]";
	}

}
