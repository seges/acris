package sk.seges.acris.mvp.client.form.smartgwt.core;

import java.io.Serializable;
import java.util.Set;

import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.acris.binding.client.holder.validation.ValidatableBeanBinding;

import com.google.gwt.validation.client.InvalidConstraint;
import com.smartgwt.client.widgets.layout.HLayout;


public abstract class AbstractSmartBindingHolder<T extends Serializable> extends HLayout implements IBeanBindingHolder<T>, ValidatableBeanBinding<T> {

	@Override
	public void highlightConstraints(Set<InvalidConstraint<T>> constraints) {
	}

	@Override
	public void clearHighlight() {
	}
	
	public void clearBinding() {
	}
}