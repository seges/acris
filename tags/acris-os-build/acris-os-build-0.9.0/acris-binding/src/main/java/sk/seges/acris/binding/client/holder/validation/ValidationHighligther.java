/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.InvalidConstraint;

/**
 * Validation highlighter defines a widget responsible for highlighting invalid
 * constraint as the result of bean validation.
 * 
 * @author eldzi
 * 
 * @param <T>
 *            Bean type that is validated
 */
public interface ValidationHighligther<T extends Serializable> {
	/**
	 * Highlights the widgets invalid constraint.
	 * 
	 * @param widget
	 *            Widget to be highlighted
	 * @param constraint
	 *            Invalid constraint containing required information like
	 *            message or property name.
	 * @return Widget that replaced the original one (if any).
	 */
	HighlightedWidget highlight(Widget widget, InvalidConstraint<T> constraint);
}