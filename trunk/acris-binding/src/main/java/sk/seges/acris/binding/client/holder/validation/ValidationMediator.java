/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.InvalidConstraint;

/**
 * @author ladislav.gazo
 */
public class ValidationMediator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> void highlightConstraints(Widget widget, Set<InvalidConstraint<T>> constraints) {
		if (widget instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) widget).highlightConstraints(constraints);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> void highlightConstraint(Widget widget, InvalidConstraint<T> constraint) {
		if (widget instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) widget).highlightConstraint(constraint);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void clearHighlight(Widget widget) {
		if (widget instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) widget).clearHighlight();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void clearHighlight(Widget form, Widget widget) {
		if (form instanceof ValidatableBeanBinding<?>) {
			((ValidatableBeanBinding) form).clearHighlight(widget);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Widget getPropertyWidget(Widget form, String property) {
		if (form instanceof ValidatableBeanBinding<?>) {
			return ((ValidatableBeanBinding) form).getPropertyWidget(property);
		}

		return null;
	}
}
