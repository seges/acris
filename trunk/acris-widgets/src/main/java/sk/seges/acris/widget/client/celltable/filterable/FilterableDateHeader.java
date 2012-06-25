package sk.seges.acris.widget.client.celltable.filterable;

import java.util.Date;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.dao.BetweenExpression;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class FilterableDateHeader extends Header<BetweenExpression<Date>> {

	private BetweenExpression<Date> criteria;
	
	public FilterableDateHeader(ValueUpdater<BetweenExpression<Date>> valueUpdate, BetweenExpression<Date> criteria, Validator<Date> validator, String text) {
		super(new DateBoxFilterCell(validator, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public BetweenExpression<Date> getValue() {
		return criteria;
	}
}