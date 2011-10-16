package sk.seges.corpis.service.annotation;

public @interface TransactionPropagation {

	public enum PropagationType {
		PROPAGATE, ISOLATE
	}
	
	PropagationType value() default PropagationType.PROPAGATE;

	String[] fields() default {};
}