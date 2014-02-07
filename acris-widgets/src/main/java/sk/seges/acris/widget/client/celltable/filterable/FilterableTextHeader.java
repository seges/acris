package sk.seges.acris.widget.client.celltable.filterable;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;
import sk.seges.acris.widget.client.celltable.AbstractFilterableTable.Validator;
import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

public class FilterableTextHeader extends Header<SimpleExpressionDTO> {

	private SimpleExpressionDTO criteria;
	
	public FilterableTextHeader(ValueUpdater<SimpleExpressionDTO> valueUpdate, SimpleExpressionDTO criteria, Validator validator, String text) {
		super(new InputFilterColumn(validator, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpressionDTO getValue() {
		return criteria;
	}
}