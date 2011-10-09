package sk.seges.corpis.pap.service.annotation;

public @interface TransactionPropagation {

	public enum PropagationType {
		PROPAGATE, ISOLATE
	}
	
	PropagationType value() default PropagationType.PROPAGATE;
}
