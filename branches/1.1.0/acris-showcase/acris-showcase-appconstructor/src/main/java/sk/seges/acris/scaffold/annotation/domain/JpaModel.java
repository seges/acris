package sk.seges.acris.scaffold.annotation.domain;


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
	boolean includeAll() default true;
}
