package sk.seges.acris.binding.client.annotations;

/**
 * Validation is part of the binding mechanism and you can define action
 * when you want to execute validation process.
 * <p>
 * Three validation strategies are available:
 * <ul>
 * <li><b>never</b> - validation is never executed and beans can contains
 * invalid values</li>
 * <li><b>on submit</b> - when user submits form (with submit button) all
 * bean properties are validated based on bean validation implementation (JSR 303)
 * <li><b>on leave</b> - validation is executed right after onLeave event is
 * raised from the appropriate widget.
 * </ul> 
 * </p>
 * @author fat
 */
public enum ValidationStrategy {
	NEVER, ON_SUBMIT, ON_LEAVE, BOTH;
}
