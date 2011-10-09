package sk.seges.acris.mvp.client.view.core.forms.smartgwt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.mvp.client.view.core.forms.LayoutForm;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


public abstract class SmartGWTLayoutForm<T extends Serializable> extends HLayout implements LayoutForm<T, FormItem> {

	private final VLayout vp;

	protected DynamicForm form;

	List<FormItem> formItems = new ArrayList<FormItem>();

	protected SmartGWTLayoutForm(int columns) {
		setWidth100();
		setHeight100();

		vp = getContainer();

		form = new DynamicForm();
		form.setAutoFocus(true);
		form.setWidth100();

		setMembers(vp);

		vp.setWidth100();
		vp.setMembers(form);
		
		setColumns(columns);
	}

	protected VLayout getContainer() {
		return new VLayout();
	}

	@Override
	public void setColumns(int columns) {
		form.setNumCols(columns);
	}

	@Override
	public void clearValues() {
		for (FormItem item : formItems) {
			item.clearValue();
		}
	}

	protected abstract void prepareFields();

	@Override
	protected void onInit() {
		initialize();
		super.onInit();
	}
	
	@Override
	public void initialize() {
		prepareFields();
		
		form.setFields(formItems.toArray(new FormItem[] {}));

		onLoad();
	}

	@Override
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
	
	protected FormItem withLabel(String label, FormItem formItem) {
		formItem.setShowTitle(true);
		formItem.setTitle(label);
		return formItem;
	}
}