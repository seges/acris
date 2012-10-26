package sk.seges.acris.widget.client.celltable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.widget.client.celltable.filterable.FilterableDateHeader;
import sk.seges.acris.widget.client.celltable.filterable.FilterableSelectionHeader;
import sk.seges.acris.widget.client.celltable.filterable.FilterableTextHeader;
import sk.seges.acris.widget.client.celltable.formatter.CellFormatter;
import sk.seges.acris.widget.client.celltable.formatter.DateCellFormatter;
import sk.seges.acris.widget.client.celltable.renderer.CellRenderer;
import sk.seges.acris.widget.client.celltable.renderer.RowRenderer;
import sk.seges.acris.widget.client.celltable.resource.PagerResources;
import sk.seges.acris.widget.client.celltable.resource.TableResources;
import sk.seges.sesam.dao.BetweenExpression;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;
import sk.seges.sesam.dao.SortInfo;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class AbstractFilterableTable<T> extends CellTable<T> {

	private final static TableResources resource = GWT.create(TableResources.class);
	private final static PagerResources pagerResources = GWT.create(PagerResources.class);

	private SimplePager pager;

	private SelectionModel<T> selectionModel;
	private boolean initialized = false;
	protected boolean sortable = true;

	private Page filter = new Page(0, DEFAULT_PAGE_SIZE);

	private static final int DEFAULT_PAGE_SIZE = 10;

	private Map<String, SimpleExpression<?>> filterValues = new HashMap<String, SimpleExpression<?>>();
	private Map<Column<?, ?>, String> columnProperties = new HashMap<Column<?, ?>, String>();

	private final Class<?> dataClass;

	private RowRenderer<T> rowRenderer;

	public AbstractFilterableTable(ProvidesKey<T> keyProvider, Class<?> dataClass) {
		super(DEFAULT_PAGE_SIZE, resource, keyProvider);
		this.dataClass = dataClass;
		addRangeChangeHandler(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				filter.setPageSize(event.getNewRange().getLength());
				filter.setStartIndex(event.getNewRange().getStart());
			}
		});
		selectionModel = new SingleSelectionModel<T>(getKeyProvider());	}
	
	public AbstractFilterableTable(ProvidesKey<T> keyProvider, Class<?> dataClass, boolean sortable) {
		this(keyProvider, dataClass);
		this.sortable = sortable;
	}

	public void setDataProvider(AsyncDataProvider<T> dataProvider) {
		dataProvider.addDataDisplay(this);
	}

	public SimplePager getPager() {
		if (pager == null) {
			pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		}
		return pager;
	};

	@Override
	public void setVisibleRange(Range range) {
		// getPager().setPageStart(range.getStart());
		super.setVisibleRange(range);
		if (getPager().getPageSize() != range.getLength()) {
			getPager().setPageSize(range.getLength());
		}
	}

	public T getSelectedItem() {
		for (T item : getVisibleItems()) {
			if (selectionModel.isSelected(item)) {
				return item;
			}
		}

		return null;
	}

	protected void initialize() {

		if (!initialized) {
			setWidth("100%", true);

			getPager().setDisplay(this);

			setSelectionModel(selectionModel, DefaultSelectionEventManager.<T> createCheckboxManager());

			initialized = true;
		}
	}

	public Criterion getDefaultCriteria() {
		return null;
	}

	public static class RenderableDateCell<T> extends DateCell {

		private final CellFormatter<Date, T> cellFormatter;
		private final CellRenderer<Date, T> cellRenderer;

		private final AbstractFilterableTable<T> cellTable;

		public RenderableDateCell(AbstractFilterableTable<T> cellTable) {
			this(cellTable, new DateCellFormatter<T>(), null);
		}

		public RenderableDateCell(AbstractFilterableTable<T> cellTable, CellFormatter<Date, T> cellFormatter,
				CellRenderer<Date, T> cellRenderer) {
			this.cellFormatter = cellFormatter;
			this.cellTable = cellTable;
			this.cellRenderer = cellRenderer;
		}

		@Override
		public void render(Context context, Date value, SafeHtmlBuilder sb) {
			int index = (context.getIndex() - ((cellTable.getPageStart() / cellTable.getPageSize()) * cellTable
					.getPageSize()));
			T visibleItem = cellTable.getVisibleItem(index);

			if (cellRenderer != null) {
				com.google.gwt.dom.client.Style style = cellTable.getRowElement(index).getCells()
						.getItem(context.getColumn()).getStyle();
				// cellRenderer.renderCell(style, value, visibleItem);
			}

			String renderedValue = cellFormatter.getCellValue(value, visibleItem);
			sb.append(SimpleSafeHtmlRenderer.getInstance().render(renderedValue));
		}
	}

	protected void initializeColumn(Column<T, ?> column, String property) {
		initialize();

		if (property != null) {
			column.setSortable(sortable);
			columnProperties.put(column, property);
		}
	}

	public void addColumn(final Column<T, ?> column, int width, String text) {
		addColumn(column, text);
		setColumnWidth(column, width, Unit.PCT);
	}

	public void addDateColumn(final Column<T, ?> column, int width, String text, String property) {
		addDateColumn(column, width, text, property, new DateValidator());
	}

	public void addDateColumn(final Column<T, ?> column, int width, String text, String property,
			Validator<Date> dateValidator) {

		initializeColumn(column, property);

		final DateFilter dateFilter = new DateFilter(dateValidator);

		ValueUpdater<BetweenExpression<Date>> dateUpdater = new ValueUpdater<BetweenExpression<Date>>() {
			@Override
			public void update(BetweenExpression<Date> value) {
				handleFilterValueChange(dateFilter, value, column);
			}
		};

		@SuppressWarnings("unchecked")
		BetweenExpression<Date> simpleExpression = (BetweenExpression<Date>) filterValues.get(property);

		Date defaultLoVal = null;
		Date defaultHiVal = null;

		if (simpleExpression != null) {
			defaultLoVal = simpleExpression.getLoValue();
			defaultHiVal = simpleExpression.getHiValue();
		}

		FilterableDateHeader filterableDateHeader = new FilterableDateHeader(dateUpdater, dateFilter.getCriterion(
				property, defaultLoVal, defaultHiVal), dateValidator, text);
		addColumn(column, filterableDateHeader);
		setColumnWidth(column, width, Unit.PCT);
	}

	public void addCheckboxColumn(int width) {
		Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(T object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};

		addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		setColumnWidth(checkColumn, width, Unit.PX);
	}

	public void addTextColumn(final Column<T, ?> column, int width, String text, String property) {
		addTextColumn(column, width, text, property, new StringValidator());
	}

	public <F extends Comparable<? extends Serializable>> void addTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator<F> validator) {
		addTextColumn(column, width, text, property, validator, Filter.EQ);
	}

	@SuppressWarnings("unchecked")
	public <F extends Comparable<? extends Serializable>> void addTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator<F> validator, String operator) {

		initializeColumn(column, property);

		final InputFilter<F> textFilter = new InputFilter<F>(validator, operator);

		ValueUpdater<SimpleExpression<F>> columnUpdater = new ValueUpdater<SimpleExpression<F>>() {
			@Override
			public void update(SimpleExpression<F> value) {
				handleFilterValueChange(textFilter, value, column);
			}
		};

		SimpleExpression<F> simpleExpression = (SimpleExpression<F>) filterValues.get(property);

		F defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = simpleExpression.getValue();
		}

		this.addColumn(column, new FilterableTextHeader<F>(columnUpdater,
				textFilter.getCriterion(property, defaultVal), validator, text));
		this.setColumnWidth(column, width, Unit.PCT);
		column.setSortable(sortable);
	}

	public <F extends Enum<?>> void addEnumeratedColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, Validator<F> validator) {
		addEnumeratedColumn(column, width, text, property, options, validator, Filter.EQ);
	}

	@SuppressWarnings("unchecked")
	public <F extends Enum<?>> void addEnumeratedColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, Validator<F> validator, String operation) {

		initializeColumn(column, property);

		final InputFilter<F> textFilter = new InputFilter<F>(validator, operation/*Filter.EQ*/);

		ValueUpdater<SimpleExpression<F>> columnUpdater = new ValueUpdater<SimpleExpression<F>>() {
			@Override
			public void update(SimpleExpression<F> value) {
				handleFilterValueChange(textFilter, value, column);
			}
		};

		SimpleExpression<F> simpleExpression = (SimpleExpression<F>) filterValues.get(property);

		F defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = simpleExpression.getValue();
		}

		this.addColumn(column,
				new FilterableSelectionHeader<F>(columnUpdater, textFilter.getCriterion(property, defaultVal),
						validator, options, text));
		this.setColumnWidth(column, width, Unit.PCT);
		column.setSortable(sortable);
	}

	public interface Validator<F extends Comparable<? extends Serializable>> {

		boolean isEmpty(String value);

		boolean isValid(String value);

		F getValue(String value);

		String toString(F f);
	}

	class InputFilter<F extends Comparable<? extends Serializable>> {

		protected final Validator<F> validator;
		protected final String operation;

		public InputFilter(Validator<F> validator, String operation) {
			this.validator = validator;
			this.operation = operation;
		}

		public SimpleExpression<F> getCriterion(String property, F... vals) {
			if (vals == null || vals.length == 0) {
				return new SimpleExpression<F>(property, null, operation);
			}
			return new SimpleExpression<F>(property, vals[0], operation);
		}

		public SimpleExpression<F> setValue(SimpleExpression<F> expression, String[] values) {
			String value = null;

			if (values.length > 0) {
				value = values[0];
			}

			if (validator.isEmpty(value)) {
				return expression.setValue(null);
			}
			if (validator.isValid(value)) {
				return expression.setValue(validator.getValue(value));
			}

			return null;
		}

	}

	public abstract class AbstractValidator<F extends Comparable<? extends Serializable>> implements Validator<F> {
		@Override
		public boolean isEmpty(String value) {
			return value == null || value.isEmpty();
		}
	}

	public class DateValidator extends AbstractValidator<Date> {

		private DateTimeFormat format;

		public DateValidator() {
			this(DateTimeFormat.getFormat("dd.MM.yyyy"));
		}

		public DateValidator(DateTimeFormat format) {
			this.format = format;
		}

		@Override
		public boolean isValid(String value) {
			try {
				format.parse(value);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		@Override
		public Date getValue(String value) {
			return format.parse(value);
		}

		@Override
		public String toString(Date f) {
			return format.format(f);
		}

	}

	public class StringValidator extends AbstractValidator<String> {

		@Override
		public boolean isValid(String value) {
			return true;
		}

		@Override
		public String getValue(String value) {
			return value;
		}

		@Override
		public String toString(String f) {
			if (f == null) {
				return "";
			}
			return f;
		}
	}

	public class LongValidator extends AbstractValidator<Long> {

		@Override
		public boolean isValid(String value) {
			try {
				Long.parseLong(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public Long getValue(String value) {
			return Long.valueOf(value);
		}

		@Override
		public String toString(Long f) {
			if (f == null) {
				return "";
			}
			return f.toString();
		}

	}

	class DateFilter extends InputFilter<Date> {

		public DateFilter(Validator<Date> validator) {
			super(validator, null);
		}

		@Override
		public BetweenExpression<Date> getCriterion(String property, Date... dates) {
			Date low = (dates != null && dates.length > 0) ? dates[0] : null;
			Date high = (dates != null && dates.length > 1) ? dates[1] : null;

			return Filter.between(property, low, high);
		}

		@Override
		public SimpleExpression<Date> setValue(SimpleExpression<Date> expression, String[] values) {

			String low = (values != null && values.length > 0) ? values[0] : null;
			String high = (values != null && values.length > 1) ? values[1] : null;

			Date lowDate = low != null ? validator.getValue(low) : null;
			Date hightDate = high != null ? validator.getValue(high) : null;

			if (expression instanceof BetweenExpression) {
				((BetweenExpression<Date>) expression).setLoValue(lowDate);
				((BetweenExpression<Date>) expression).setHiValue(hightDate);
				return expression;
			}
			return expression.setValue(lowDate);
		}
	}

	private <F extends Comparable<? extends Serializable>> void handleFilterValueChange(InputFilter<F> dataTypeFilter,
			SimpleExpression<F> value, Column<T, ?> column) {
		if (value.getValue() == null && value.getOperation() == null) {
			ColumnSortList columnSortList = getColumnSortList();
			boolean asc = false;
			if (columnSortList.size() > 0) {
				ColumnSortInfo columnSortInfo = columnSortList.get(0);
				asc = columnSortInfo.isAscending();
			}

			columnSortList.clear();
			columnSortList.push(new ColumnSortInfo(column, asc));

			List<SortInfo> sortables = new ArrayList<SortInfo>();
			SortInfo sortInfo = new SortInfo(asc, value.getProperty());
			sortables.add(sortInfo);
			filter.setSortables(sortables);

			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		} else if (value.getValue() != null) {
			ColumnSortList columnSortList = getColumnSortList();
			if (columnSortList.size() > 0) {
				ColumnSortInfo columnSortInfo = columnSortList.get(columnSortList.size() - 1);
				columnSortList.clear();
				if (columnSortInfo.getColumn().equals(column)) {
					columnSortInfo = new ColumnSortInfo(column, !columnSortInfo.isAscending());
				}
				columnSortList.push(columnSortInfo);
			}

			filterValues.put(value.getProperty(), value);

			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		} else if (value.getValue() == null) {
			filterValues.remove(value.getProperty());
			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		}
	}

	public Page getPage() {
		Page page = new Page(getPager().getPageStart(), getPager().getPageSize());

		Criterion defaultCriteria = getDefaultCriteria();

		if (getColumnSortList().size() > 0) {
			ColumnSortInfo columnSortInfo = getColumnSortList().get(0);
			String property = columnProperties.get(columnSortInfo.getColumn());
			if (property != null) {
				List<SortInfo> sortables = new ArrayList<SortInfo>();
				sortables.add(new SortInfo(columnSortInfo.isAscending(), property));
				page.setSortables(sortables);
				page.setProjectableResult(dataClass.getName());
			} else {
				page.setProjectableResult(null);
				page.setSortables(null);
			}
		}

		Conjunction conjunction = null;

		for (Entry<String, SimpleExpression<?>> filterValue : filterValues.entrySet()) {
			if (filterValue.getValue() != null && filterValue.getValue().getValue() != null) {
				if (conjunction == null) {
					conjunction = Filter.conjunction();
				}
				conjunction.add(filterValue.getValue());
			}
		}

		if (conjunction != null) {
			if (defaultCriteria != null) {
				conjunction.add(defaultCriteria);
			}
			page.setFilterable(conjunction);
		} else {
			page.setFilterable(defaultCriteria);
		}
		
		return page;
	}

	public void setRowRenderer(RowRenderer<T> rowRenderer) {
		this.rowRenderer = rowRenderer;
	}

	@Override
	protected void renderRowValues(SafeHtmlBuilder sb, List<T> values, int start,
			SelectionModel<? super T> selectionModel) {
		super.renderRowValues(sb, values, start, selectionModel);

		if (rowRenderer != null) {
			for (int i = 0; i < values.size(); i++) {
				rowRenderer.renderRow(getRowElement(i).getStyle(), values.get(i));
			}
		}
	}
}