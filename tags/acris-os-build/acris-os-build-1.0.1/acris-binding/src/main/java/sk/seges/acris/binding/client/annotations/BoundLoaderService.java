package sk.seges.acris.binding.client.annotations;

/**
 * @author eldzi
 */
public @interface BoundLoaderService {
	Class<?> loaderClass() default Void.class;
	String ref() default "";
}
