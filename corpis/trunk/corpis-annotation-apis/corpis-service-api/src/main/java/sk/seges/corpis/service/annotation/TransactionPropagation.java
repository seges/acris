package sk.seges.corpis.service.annotation;

import sk.seges.sesam.core.configuration.annotation.GenerateModel;

@GenerateModel
public @interface TransactionPropagation {

	public enum PropagationType {
		PROPAGATE, ISOLATE
	}
	
	public enum PropagationTarget {
		RETURN_VALUE, ARGUMENTS
	}

	PropagationType value() default PropagationType.PROPAGATE;
	PropagationTarget[] target() default { PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS };

	String[] fields() default {};
}