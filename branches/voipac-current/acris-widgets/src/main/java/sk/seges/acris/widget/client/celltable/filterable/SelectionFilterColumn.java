package sk.seges.acris.widget.client.celltable.filterable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SelectionFilterColumn<T extends Comparable<? extends Serializable>> extends AbstractFilterableCell<SimpleExpression<T>> {

	interface Template extends SafeHtmlTemplates {
		@Template("<div style=\"\">{0}</div>")
		SafeHtml header(String columnName);

		@Template("<option value=\"{0}\">{0}</option>")
		SafeHtml deselected(String option);

		@Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml selected(String option);
	}

	private static Template template;

	private HashMap<String, Integer> indexForOption = new HashMap<String, Integer>();

	private final List<String> options;
	private String text;
	
	private final Map<Object, String> viewDataMap = new HashMap<Object, String>();

	private static final String FILTER_INPUT_PREFIX = "filterInput";

	private final Validator<T> validator;
	
	public SelectionFilterColumn(Validator<T> validator, List<String> options, String text) {
		super("click", "change", "focus", "blur");

		this.validator = validator;
		this.text = text;
		if (template == null) {
			template = GWT.create(Template.class);
		}
		this.options = new ArrayList<String>(options);
		int index = 0;
		for (String option : options) {
			indexForOption.put(option, index++);
		}
	}

	public void clearViewData(Object key) {
		if (key != null) {
			viewDataMap.remove(key);
		}
	}

	public String getViewData(Object key) {
		return (key == null) ? null : viewDataMap.get(key);
	}
		  
	public void setViewData(Object key, String viewData) {
		if (key == null) {
			return;
		}

		if (viewData == null) {
			clearViewData(key);
		} else {
			viewDataMap.put(key, viewData);
		}
	}

	private boolean ignoreClick = true;
	
	@Override
	public void onBrowserEvent(Context context, Element parent,
			SimpleExpression<T> value, NativeEvent event, ValueUpdater<SimpleExpression<T>> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		String type = event.getType();
		if ("change".equals(type)) {
			Object key = context.getKey();
			SelectElement inputElement = getSelectElement(parent);
			int index = inputElement.getSelectedIndex();
			
			String newValue = options.get(index);
			setViewData(key, newValue);
			if (valueUpdater != null) {
				SimpleExpression<T> newExpression = new SimpleExpression<T>();
				newExpression.setProperty(value.getProperty());
				newExpression.setOperation(value.getOperation());
				if (newValue != null && !newValue.isEmpty()) {
					newExpression.setValue(validator.getValue(newValue));
					valueUpdater.update(newExpression);
				} else {
					newExpression.setValue(null);
					valueUpdater.update(newExpression);
				}
			}
			ignoreClick = true;
		} else if ("focus".equals(event.getType())) {
			ignoreClick = true;
		} else if ("click".equals(event.getType())) {
			if (ignoreClick) {
				ignoreClick = false;
			} else {
				changeSorting(value, valueUpdater);
			}
		}
		
		GWT.log(event.getType());
	}
	
	protected void changeSorting(SimpleExpression<T> value, ValueUpdater<SimpleExpression<T>> valueUpdater) {
		if (!hasFocus(FILTER_INPUT_PREFIX + value.getProperty())) {
			if (valueUpdater != null) {
				SimpleExpression<T> newExpression = new SimpleExpression<T>();
				newExpression.setProperty(value.getProperty());
	
				valueUpdater.update(newExpression);
			}
		}
	}
	
	protected SelectElement getSelectElement(Element parent) {
		Element elem = parent.getElementsByTagName("select").getItem(0);
		assert (elem.getClass() == SelectElement.class);
		return elem.cast();
	}
	
	@Override
	public void render(Context context, SimpleExpression<T> value, SafeHtmlBuilder sb) {
		// Get the view data.
		sb.append(template.header(text));
		Object key = context.getKey();
		String viewData = getViewData(key);
		if (viewData != null && viewData.equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		int selectedIndex = getSelectedIndex(viewData == null ? validator.toString(value.getValue()) : viewData);
		sb.appendHtmlConstant("<select tabindex=\"-1\">");
		int index = 0;
		for (String option : options) {
			if (index++ == selectedIndex) {
				sb.append(template.selected(option));
			} else {
				sb.append(template.deselected(option));
			}
		}
		sb.appendHtmlConstant("</select>");
	}

	private int getSelectedIndex(String value) {
		Integer index = indexForOption.get(value);
		if (index == null) {
			return -1;
		}
		return index.intValue();
	}

	@Override
	protected String valueToString(SimpleExpression<T> value, int index) {
		return validator.toString(value.getValue());
	}
}