/**
 * 
 */
package sk.seges.acris.mvp.client.view.core.forms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.binding.client.holder.validation.HighlightedWidget;
import sk.seges.acris.binding.client.holder.validation.ValidationHighligther;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.validation.client.InvalidConstraint;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author ladislav.gazo
 *
 */
public class ExampleHighlighter<T extends Serializable> implements ValidationHighligther<FormItem, T> {
	public static VerticalPanel errorsPanel;
	
	@SuppressWarnings("unchecked")
	@Override
	public HighlightedWidget highlight(FormItem widget, InvalidConstraint<T> constraint) {

		Map<String, String> errors = widget.getForm().getErrors();

		if (errors == null) {
			errors = new HashMap<String, String>();
		}
		errors.put(widget.getName(), constraint.getMessage());
		widget.getForm().setErrors(errors, true);

		return new RemoveHighlightStyle(widget);
	}

	public static class RemoveHighlightStyle implements HighlightedWidget {

		private final FormItem widget;

		public RemoveHighlightStyle(FormItem widget) {
			super();
			this.widget = widget;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void removeHighlight() {
			Map<String, String> errors = widget.getForm().getErrors();
			errors.remove(widget.getName());
			widget.getForm().setErrors(errors, true);
		}
	}
}
