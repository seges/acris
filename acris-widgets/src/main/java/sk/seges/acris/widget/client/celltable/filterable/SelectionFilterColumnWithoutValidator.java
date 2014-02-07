package sk.seges.acris.widget.client.celltable.filterable;

import java.util.List;

import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

public class SelectionFilterColumnWithoutValidator extends SelectionFilterColumn {

	public SelectionFilterColumnWithoutValidator(List<String> options, String text) {
		super(null, options, text);
	}

	@Override
	protected SimpleExpressionDTO setValueToExpresion(SimpleExpressionDTO newExpression, String newValue){
		newExpression.setValue(new PropertyHolder(newValue));
		return newExpression;
	}

	@Override
	protected String valueToString(SimpleExpressionDTO value, int index) {
		return value.getValue().getStringValue() == null ? "" : value.getValue().getStringValue();
	}
	@Override
	protected String valueToString(SimpleExpressionDTO value) {
		return validator.toString(value.getValue());
	}
}
