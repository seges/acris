package sk.seges.acris.widget.client.celltable.filterable;

import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;
import sk.seges.sesam.shared.model.dto.BetweenExpressionDTO;

public class FilterableDateHeader extends Header<BetweenExpressionDTO> {

	private BetweenExpressionDTO criteria;
	
	public FilterableDateHeader(ValueUpdater<BetweenExpressionDTO> valueUpdate, BetweenExpressionDTO criteria, Validator validator, String text) {
		super(new DateBoxFilterCell(validator, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public BetweenExpressionDTO getValue() {
		return criteria;
	}
}