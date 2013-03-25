package sk.seges.corpis.appscaffold.shared.annotation.domain;


/**
 * No methods specified in JpaModel means to generate all methods. Using @Exclude
 * on a method you define which to not include in generation.
 * 
 * @see Exclude
 * 
 * @author ladislav.gazo
 * 
 */
public @interface JpaModel {
	FieldStrategyDefinition strategy() default FieldStrategyDefinition.IMPLICIT;
	boolean portable() default true;
}
