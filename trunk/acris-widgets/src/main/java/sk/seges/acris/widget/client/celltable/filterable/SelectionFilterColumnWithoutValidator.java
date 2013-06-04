package sk.seges.acris.widget.client.celltable.filterable;

import java.util.List;

import sk.seges.sesam.dao.SimpleExpression;

public class SelectionFilterColumnWithoutValidator extends SelectionFilterColumn<String>{

	public SelectionFilterColumnWithoutValidator(List<String> options, String text) {
		super(null, options, text);		
	}

	@Override
	protected SimpleExpression<String> setValueToExpresion(SimpleExpression<String> newExpression, String newValue){
		return newExpression.setValue(newValue);
	}
	
	@Override
	protected String valueToString(SimpleExpression<String> value, int index) {
		return value.getValue()==null?"":value.getValue();
	}
	@Override
	protected String valueToString(SimpleExpression<String> value) {
		return validator.toString(value.getValue());
	}
}
