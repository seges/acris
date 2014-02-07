package sk.seges.acris.widget.client.celltable.filterable;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;
import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

import java.util.List;

public class FilterableSelectionHeaderWithoutValidator extends Header<SimpleExpressionDTO> {

	private SimpleExpressionDTO criteria;

	public FilterableSelectionHeaderWithoutValidator(ValueUpdater<SimpleExpressionDTO> valueUpdate,
			SimpleExpressionDTO criteria, List<String> options, String text) {
		super(new SelectionFilterColumnWithoutValidator(options, text));
		setUpdater(valueUpdate);
		this.criteria = criteria;
	}

	@Override
	public SimpleExpressionDTO getValue() {
		return criteria;
	}

}
