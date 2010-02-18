/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.InvalidConstraint;

/**
 * @author eldzi
 */
public class ValidationMediator {
	@SuppressWarnings("unchecked")
	public static <T> void highlightConstraints(Widget widget, Set<InvalidConstraint<T>> constraints) {
		if(widget instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) widget).highlightConstraints(constraints);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void clearHighlight(Widget widget) {
		if(widget instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) widget).clearHighlight();
		}
	}
}
