package sk.seges.acris.binding.client.annotations;

/**
 * @author ladislav.gazo
 */
public @interface BoundLoaderService {
	/**
	 * @return GWT RPC service interface responsible for providing entities.
	 */
	Class<?> loaderClass() default Void.class;
	/**
	 * @return Optional endpoint URL of the service. If left empty, it will not be taken.
	 */
	String ref() default "";
}
