package sk.seges.acris.widget.client.celltable.filterable;

import java.util.Map;

import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public class FilterableSelectionHeaderWithoutValidator extends Header<SimpleExpressionDTO> {

	private SimpleExpressionDTO criteria;

	public FilterableSelectionHeaderWithoutValidator(ValueUpdater<SimpleExpressionDTO> valueUpdate,
			SimpleExpressionDTO criteria, Map<String, String> options, String text) {
		super(new SelectionFilterColumnWithoutValidator(options, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpressionDTO getValue() {
		return criteria;
	}

}
