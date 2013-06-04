package sk.seges.acris.widget.client.celltable.filterable;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class FilterableSelectionHeader<T extends Comparable<? extends Serializable>> extends Header<SimpleExpression<T>> {

	private SimpleExpression<T> criteria;
	
	public FilterableSelectionHeader(ValueUpdater<SimpleExpression<T>> valueUpdate, SimpleExpression<T> criteria, Validator<T> validator, List<String> options, String text) {
		super(new SelectionFilterColumn<T>(validator, options, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpression<T> getValue() {
		return criteria;
	}

}