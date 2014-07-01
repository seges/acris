package sk.seges.acris.widget.client.celltable.filterable;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SelectionFilterColumn extends AbstractFilterableCell<SimpleExpressionDTO> {

	protected interface Template extends SafeHtmlTemplates {
		@Template("<div style=\"\">{0}</div>")
		SafeHtml header(String columnName);

		@Template("<option value=\"{0}\">{0}</option>")
		SafeHtml deselected(String option);

		@Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml selected(String option);
	}

	protected static Template template;

	protected final Map<String, String> options;
	protected String text;
	
	private final Map<Object, String> viewDataMap = new HashMap<Object, String>();

	protected static final String FILTER_INPUT_PREFIX = "filterInput";

	protected final Validator validator;
	
	public SelectionFilterColumn(Validator validator, Map<String, String> options, String text) {
		super("click", "change", "focus", "blur", "mousewheel", "mousedown", "mouseup", "mousemove", "DOMMouseScroll");

		this.validator = validator;
		this.text = text;
		initTemplate();
		
		this.options = options;
	}
	
	protected void initTemplate(){
		if (template == null) {
			template = GWT.create(Template.class);
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
			SimpleExpressionDTO value, NativeEvent event, ValueUpdater<SimpleExpressionDTO> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		String type = event.getType();
		if ("change".equals(type)) {
			Object key = context.getKey();
			SelectElement inputElement = getSelectElement(parent);
			int index = inputElement.getSelectedIndex();
			
			String newValue = options.get(index);
			setViewData(key, newValue);
			if (valueUpdater != null) {
				SimpleExpressionDTO newExpression = new SimpleExpressionDTO();
				newExpression.setProperty(value.getProperty());
				newExpression.setOperation(value.getOperation());
				if (newValue != null && !newValue.isEmpty()) {
					valueUpdater.update(setValueToExpresion(newExpression, newValue));
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
	
	protected SimpleExpressionDTO setValueToExpresion(SimpleExpressionDTO newExpression, String newValue){
		newExpression.setValue(validator.getValue(newValue));
		return newExpression;
	}
	
	
	protected void changeSorting(SimpleExpressionDTO value, ValueUpdater<SimpleExpressionDTO> valueUpdater) {
		if (!hasFocus(FILTER_INPUT_PREFIX + value.getProperty())) {
			if (valueUpdater != null) {
				SimpleExpressionDTO newExpression = new SimpleExpressionDTO();
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
	public void render(Context context, SimpleExpressionDTO value, SafeHtmlBuilder sb) {
		// Get the view data.
		sb.append(template.header(text));
		Object key = context.getKey();
		String viewData = getViewData(key);
		if (viewData != null && viewData.equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		String selectedValue = getSelectedValue(viewData == null ? valueToString(value) : viewData);
		sb.appendHtmlConstant("<select tabindex=\"-1\">");
		for (String option : options.keySet()) {
			if (options.get(option).equals(selectedValue)) {
				sb.append(template.selected(options.get(option)));
			} else {
				sb.append(template.deselected(options.get(option)));
			}
		}
		sb.appendHtmlConstant("</select>");
	}

	private String getSelectedValue(String value) {
		String selectedValue = options.get(value);
		if (selectedValue == null) {
			return "";
		}
		return selectedValue;
	}

	@Override
	protected String valueToString(SimpleExpressionDTO value, int index) {
		return validator.toString(value.getValue());
	}
	
	protected String valueToString(SimpleExpressionDTO value) {
		return validator.toString(value.getValue());
	}
}