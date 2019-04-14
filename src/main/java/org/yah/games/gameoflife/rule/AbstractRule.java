package org.yah.games.gameoflife.rule;

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
