package sk.seges.acris.mvp.client.form.smartgwt.core;

import sk.seges.sesam.domain.IDomainObject;

import com.smartgwt.client.widgets.form.DynamicForm;

public abstract class FourColumnSmartForm<T extends IDomainObject<?>> extends AbstractSmartFormBase<T> {

	@Override
	protected DynamicForm initializeForm(DynamicForm form) {
		form.setNumCols(4);
		return form;
	}
}