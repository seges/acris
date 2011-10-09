/**
 * 
 */
package sk.seges.acris.binding.client.samples.form;

import java.io.Serializable;

import sk.seges.acris.binding.client.holder.validation.HighlightedWidget;
import sk.seges.acris.binding.client.holder.validation.ValidationHighligther;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.InvalidConstraint;

/**
 * @author ladislav.gazo
 *
 */
public class ExampleHighlighter<T extends Serializable> implements ValidationHighligther<Widget, T> {
	public static VerticalPanel errorsPanel;
	
	@Override
	public HighlightedWidget highlight(Widget widget, InvalidConstraint<T> constraint) {
		widget.getElement().getStyle().setBackgroundColor("red");
		Label error = new Label("Error in " + constraint.getItemName() + ": " + constraint.getMessage());
		errorsPanel.add(error);
		return new RemoveHighlightStyle(widget, error);
	}

	private static class RemoveHighlightStyle implements HighlightedWidget {
		private final Widget widget;
		private final Widget error;
		
		public RemoveHighlightStyle(Widget widget, Widget error) {
			super();
			this.widget = widget;
			this.error = error;
		}

		@Override
		public void removeHighlight() {
			widget.getElement().getStyle().setBackgroundColor("white");
			error.removeFromParent();
		}
		
	}
}
