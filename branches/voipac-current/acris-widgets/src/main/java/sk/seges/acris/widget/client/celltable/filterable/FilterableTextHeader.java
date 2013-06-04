package sk.seges.acris.widget.client.celltable.filterable;

import java.io.Serializable;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class FilterableTextHeader<T extends Comparable<? extends Serializable>> extends Header<SimpleExpression<T>> {

	private SimpleExpression<T> criteria;
	
	public FilterableTextHeader(ValueUpdater<SimpleExpression<T>> valueUpdate, SimpleExpression<T> criteria, Validator<T> validator, String text) {
		super(new InputFilterColumn<T>(validator, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpression<T> getValue() {
		return criteria;
	}
}