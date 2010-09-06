package sk.seges.acris.mvp.client.form.smartgwt.core;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.binding.client.annotations.Generated;
import sk.seges.acris.binding.client.holder.IBeanBindingHolder;
import sk.seges.sesam.domain.IDomainObject;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class AbstractSmartFormBase<T extends IDomainObject<?>> extends HLayout implements IBeanBindingHolder<T> {

	private final VLayout vp;

	protected DynamicForm form;

	protected AbstractSmartFormBase() {
		vp = getContainer();
	}

	List<FormItem> formItems = new ArrayList<FormItem>();

	protected VLayout getContainer() {
		return new VLayout();
	}

	protected void prepareFields() {
	};

	@Override
	protected void onInit() {
		initPanel();
		prepareFields();
		form.setFields(formItems.toArray(new FormItem[] {}));
		onLoad();
		super.onInit();
	}

	protected abstract DynamicForm initializeForm(DynamicForm form);

	protected void initPanel() {
		form = new DynamicForm();
		form.setAutoFocus(true);
		form = initializeForm(form);
		form.setWidth100();

		setMembers(vp);

		vp.setWidth100();
		vp.setMembers(form);
	}

	public FormItem withLabel(String label, FormItem formItem) {
		formItem.setShowTitle(true);
		formItem.setTitle(label);
		return formItem;
	}

	public FormItem withoutLabel(FormItem formItem) {
		formItem.setShowTitle(false);
		formItem.setTitle("");
		formItem.setTooltip("");
		return formItem;
	}

	public void addFormRow(boolean autofill, FormItem... items) {
		int count = 0;
		for (FormItem formItem : items) {
			formItems.add(formItem);

			Integer colSpan = formItem.getAttributeAsInt("colSpan");

			count += colSpan == null ? 1 : colSpan;
			if (formItem.getShowTitle()) {
				count++;
			}
		}

		if (autofill) {
			FormItem formItem = items[items.length - 1];
			formItem.setEndRow(true);
		}
	}

	public void addFormRow(FormItem... items) {
		addFormRow(true, items);
	}

	public void addEmptyRow() {
		addFormRow(new SpacerItem());
	}

	public void setButtons(ButtonItem... buttons) {
		addFormRow(buttons);
	}

	public void clearValues() {
		for (FormItem item : formItems) {
			item.clearValue();
		}
	}

	@Override
	@Generated
	public void setBean(T bean) {
	}

	@Override
	@Generated
	public T getBean() {
		return null;
	}
}