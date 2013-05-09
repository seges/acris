package sk.seges.acris.widget.client.celltable.filterable;

import java.util.List;

import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class FilterableSelectionHeaderWithoutValidator extends Header<SimpleExpression<String>> {

	private SimpleExpression<String> criteria;

	public FilterableSelectionHeaderWithoutValidator(ValueUpdater<SimpleExpression<String>> valueUpdate,
			SimpleExpression<String> criteria, List<String> options, String text) {
		super(new SelectionFilterColumnWithoutValidator(options, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpression<String> getValue() {
		return criteria;
	}

}
