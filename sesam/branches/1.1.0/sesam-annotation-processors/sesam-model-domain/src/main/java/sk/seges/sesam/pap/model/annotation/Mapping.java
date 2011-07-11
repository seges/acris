package sk.seges.sesam.pap.model.annotation;


public @interface Mapping {

	public enum MappingType {
		EXPLICIT, AUTOMATIC;
	}
	
	MappingType value() default MappingType.AUTOMATIC;
}
