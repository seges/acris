package sk.seges.acris.scaffold.model.view.compose;

/**
 * In case no value is defined, takes default validator based on the model
 * object and the field.
 * 
 * @author ladislav.gazo
 */
public @interface Validate {

	Class<? extends CustomValidator> value() default CustomValidator.class;

}
