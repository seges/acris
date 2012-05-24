package sk.seges.acris.scaffold.model.view.compose;

/**
 * In case no value is specified, editor is taken from default mapping of the field in the model.
 * 
 * @author ladislav.gazo
 *
 */
public @interface Editable {

	Class<?> editor() default Void.class;

}
