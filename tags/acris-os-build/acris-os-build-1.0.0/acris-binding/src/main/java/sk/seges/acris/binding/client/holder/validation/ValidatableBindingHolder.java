/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 * @author eldzi
 */
public class ValidatableBindingHolder<T extends Serializable> extends BindingHolder<T> implements ValidatableBeanBinding<T> {
	private ValidationHighligther<T> highlighter;
	private List<HighlightedWidget> highlighted = new ArrayList<HighlightedWidget>();

	public ValidatableBindingHolder(UpdateStrategy updateStrategy, BeanWrapper<T> beanWrapper) {
		super(updateStrategy, beanWrapper);
	}

	public void setHighlighter(ValidationHighligther<T> highlighter) {
		this.highlighter = highlighter;
	}

	@Override
	public void highlightConstraints(Set<InvalidConstraint<T>> constraints) {
		clearHighlight();

		for (InvalidConstraint<T> constraint : constraints) {
			BindingFieldInfo info = find(constraint.getPropertyPath());
			if (info == null) {
				throw new RuntimeException("No such binding field for constraint = "
						+ constraint.getPropertyPath());
			}

			Object widget = info.getTargetWidget();
			if (highlighter != null && widget instanceof Widget) {
				highlighted.add(highlighter.highlight((Widget)widget, constraint));
			}
		}
	}

	@Override
	public void clearHighlight() {
		for (HighlightedWidget highlight : highlighted) {
			highlight.removeHighlight();
		}
		highlighted.clear();
	}

	private BindingFieldInfo find(String propertyPath) {
		for (BindingFieldInfo info : bindingFieldInfos) {
			if (info.getSourceProperty().equals(propertyPath)
					|| info.getTargetProperty().equals(propertyPath)) {
				return info;
			}
		}
		return null;
	}
}
