package sk.seges.acris.binding.client.holder.validation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingFieldInfo;
import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.InvalidConstraint;

/**
 * Extended binding holder with validation support. It is build on top of
 * gwt-validation framework and JSR-303. It facilitates validation of a widget
 * but doesn't define specific graphical representation.
 * 
 * @author ladislav.gazo
 */
public class ValidatableBindingHolder<S, T extends Serializable> extends BindingHolder<T> implements
		ValidatableBeanBinding<T> {

	private ValidationHighligther<S, T> highlighter;
	private Map<Widget, HighlightedWidget> highlighted = new HashMap<Widget, HighlightedWidget>();

	public ValidatableBindingHolder(UpdateStrategy updateStrategy, BeanWrapper<T> beanWrapper) {
		super(updateStrategy, beanWrapper);
	}

	public void setHighlighter(ValidationHighligther<S, T> highlighter) {
		this.highlighter = highlighter;
	}

	@Override
	public void highlightConstraints(Set<InvalidConstraint<T>> constraints) {
		clearHighlight();

		for (InvalidConstraint<T> constraint : constraints) {
			highlight(constraint);
		}
	}

	@Override
	public void highlightConstraint(InvalidConstraint<T> constraint) {
		Widget propertyWidget = getPropertyWidget(constraint.getPropertyPath());

		clearHighlight(propertyWidget);

		highlight(constraint);
	}

	@Override
	public void clearHighlight() {
		for (HighlightedWidget highlight : highlighted.values()) {
			highlight.removeHighlight();
		}
		highlighted.clear();
	}

	@Override
	public void clearHighlight(Widget widget) {
		HighlightedWidget highlightedWidget = highlighted.get(widget);
		if (highlightedWidget != null) {
			highlightedWidget.removeHighlight();
			highlighted.remove(widget);
		}
	}

	@Override
	public Widget getPropertyWidget(String property) {
		BindingFieldInfo info = find(property);
		return (Widget) info.getTargetWidget();
	}

	private BindingFieldInfo find(String propertyPath) {
		for (BindingFieldInfo info : bindingFieldInfos) {
			if (info.getSourceProperty().equals(propertyPath) || info.getTargetProperty().equals(propertyPath)) {
				return info;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void highlight(InvalidConstraint<T> constraint) {
		BindingFieldInfo info = find(constraint.getPropertyPath());
		if (info == null) {
			throw new RuntimeException("No such binding field for constraint = " + constraint.getPropertyPath());
		}

		Object widget = info.getTargetWidget();
		if (highlighter != null) {
			HighlightedWidget highlightedWidget = highlighter.highlight((S) widget, constraint);
			highlighted.put((Widget) widget, highlightedWidget);
		}
	}
}
