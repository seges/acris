package sk.seges.acris.widget.client.celltable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.common.util.Triple;
import sk.seges.acris.widget.client.celltable.filterable.FilterableDateHeader;
import sk.seges.acris.widget.client.celltable.filterable.FilterableSelectionHeader;
import sk.seges.acris.widget.client.celltable.filterable.FilterableTextHeader;
import sk.seges.acris.widget.client.celltable.formatter.CellFormatter;
import sk.seges.acris.widget.client.celltable.formatter.DateCellFormatter;
import sk.seges.acris.widget.client.celltable.renderer.CellRenderer;
import sk.seges.acris.widget.client.celltable.renderer.RowRenderer;
import sk.seges.acris.widget.client.celltable.resource.PagerResources;
import sk.seges.acris.widget.client.celltable.resource.TableResources;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.dao.MatchMode;
import sk.seges.sesam.shared.model.dao.SortInfo;
import sk.seges.sesam.shared.model.dto.BetweenExpressionDTO;
import sk.seges.sesam.shared.model.dto.ConjunctionDTO;
import sk.seges.sesam.shared.model.dto.CriterionDTO;
import sk.seges.sesam.shared.model.dto.FilterDTO;
import sk.seges.sesam.shared.model.dto.LikeExpressionDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.SimpleExpressionDTO;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class AbstractFilterableTable<T> extends CellTable<T> {

	public interface ProvidesIdentifier<S> extends ProvidesKey<S> {

		public static final String ID = "id";

		String getKeyColumnName();
	
	}
	
	protected final static TableResources resource = GWT.create(TableResources.class);
	protected final static PagerResources pagerResources = GWT.create(PagerResources.class);

	protected SimplePager pager;

	protected SelectionModel<T> selectionModel;
	protected boolean initialized = false;
	protected boolean sortable = true;
	protected boolean filterable = true;

	private PageDTO filter = new PageDTO(0, DEFAULT_PAGE_SIZE);

	private static final int DEFAULT_PAGE_SIZE = 10;

	private Map<String, SimpleExpressionDTO> filterValues = new HashMap<String, SimpleExpressionDTO>();
	private Map<Column<?, ?>, String> columnProperties = new HashMap<Column<?, ?>, String>();
	
	protected boolean selectedAllChecked = false;
	protected boolean enableMultiselection = false;

	private final Class<?> dataClass;

	private RowRenderer<T> rowRenderer;
	private ProvidesIdentifier<T> keyProvider;
	
	public AbstractFilterableTable(ProvidesIdentifier<T> keyProvider, Class<?> dataClass, boolean enableMultiselection) {
		this(keyProvider, dataClass, enableMultiselection, resource, false, true);		
	}
	
	protected AbstractFilterableTable(ProvidesIdentifier<T> keyProvider, Class<?> dataClass, boolean enableMultiselection, TableResources resource, 
			boolean sortable, boolean filterable){
		super(DEFAULT_PAGE_SIZE, resource, keyProvider);
		this.dataClass = dataClass;
		addRangeChangeHandler(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				filter.setPageSize(event.getNewRange().getLength());
				filter.setStartIndex(event.getNewRange().getStart());
			}
		});
		this.enableMultiselection = enableMultiselection;
		if(enableMultiselection){
			selectionModel = new MultiSelectionModel<T>(getKeyProvider());
		}else{
			selectionModel = new SingleSelectionModel<T>(getKeyProvider());
		}
		this.sortable = sortable;
		this.filterable = filterable;
	}
	
	
	public Class<?> getDataClass() {
		return dataClass;
	}
		
	public AbstractFilterableTable(ProvidesIdentifier<T> keyProvider, Class<?> dataClass, boolean enableMultiselection, boolean sortable) {
		this(keyProvider, dataClass, enableMultiselection, resource, sortable, true);
		
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
	
	public List<T> getSelectedItems() {
		List<T> selectedItems = new ArrayList<T>();
		for (T item : getVisibleItems()) {
			if (selectionModel.isSelected(item)) {
				selectedItems.add(item);
			}
		}

		return selectedItems;
	}

	protected void initialize() {

		if (!initialized) {
			setWidth("100%", true);

			getPager().setDisplay(this);

			setSelectionModel(selectionModel, DefaultSelectionEventManager.<T> createCheckboxManager());

			initialized = true;
		}
	}

	public CriterionDTO getDefaultCriteria() {
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
			columnProperties.put(column, property);
		}
	}

	public void addColumn(final Column<T, ?> column, int width, String text) {
		addColumn(column, text);
		setColumnWidth(column, width, Unit.PCT);
	}

	public void addDateColumn(final Column<T, ?> column, int width, String text, String property, Triple<Button, Integer, ClickHandler> footerButton) {
		addDateColumn(column, width, text, property, new DateValidator(), footerButton);
	}
	
	public void addFooterWidgetDateColumn(final Column<T, ?> column, int width, String text, String property, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {
		addFooterWidgetDateColumn(column, width, text, property, new DateValidator(), footerWidget, colSpan);
	}

	public void addDateColumn(final Column<T, ?> column, int width, String text, String property,
			Validator dateValidator, Triple<Button, Integer, ClickHandler> footerButton) {

		initializeColumn(column, property);

		final DateFilter dateFilter = new DateFilter(dateValidator);

		ValueUpdater<BetweenExpressionDTO> dateUpdater = new ValueUpdater<BetweenExpressionDTO>() {
			@Override
			public void update(BetweenExpressionDTO value) {
				handleFilterValueChange(dateFilter, value, column);
			}
		};

		@SuppressWarnings("unchecked")
		BetweenExpressionDTO simpleExpression = (BetweenExpressionDTO) filterValues.get(property);

		Date defaultLoVal = null;
		Date defaultHiVal = null;

		if (simpleExpression != null) {
			defaultLoVal = simpleExpression.getLoValue().getDateValue();
			defaultHiVal = simpleExpression.getHiValue().getDateValue();
		}

		addColumnWithDateHeader(column, text, property, dateValidator, defaultLoVal, defaultHiVal, dateFilter, dateUpdater, footerButton);
		setColumnWidth(column, width, Unit.PCT);
	}
	
	public void addFooterWidgetDateColumn(final Column<T, ?> column, int width, String text, String property,
			Validator dateValidator, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {

		initializeColumn(column, property);

		final DateFilter dateFilter = new DateFilter(dateValidator);

		ValueUpdater<BetweenExpressionDTO> dateUpdater = new ValueUpdater<BetweenExpressionDTO>() {
			@Override
			public void update(BetweenExpressionDTO value) {
				handleFilterValueChange(dateFilter, value, column);
			}
		};

		@SuppressWarnings("unchecked")
		BetweenExpressionDTO simpleExpression = (BetweenExpressionDTO) filterValues.get(property);

		Date defaultLoVal = null;
		Date defaultHiVal = null;

		if (simpleExpression != null) {
			defaultLoVal = simpleExpression.getLoValue().getDateValue();
			defaultHiVal = simpleExpression.getHiValue().getDateValue();
		}

		addFooterWidgetColumnWithDateHeader(column, text, property, dateValidator, defaultLoVal, defaultHiVal, dateFilter, dateUpdater, footerWidget, colSpan);
		setColumnWidth(column, width, Unit.PCT);
	}
	
	protected void addColumnWithDateHeader(Column<T, ?> column, String text, String property,
			Validator dateValidator, Date defaultLoVal, Date defaultHiVal, DateFilter dateFilter, ValueUpdater<BetweenExpressionDTO> dateUpdater,
			Triple<Button, Integer, ClickHandler> footerButton){
		FilterableDateHeader filterableDateHeader = new FilterableDateHeader(dateUpdater, dateFilter.getCriterion(
				property, new PropertyHolder(defaultLoVal), new PropertyHolder(defaultHiVal)), dateValidator, text);
		if(filterable){
			addColumn(column, filterableDateHeader);
		}else{
			addColumn(column, new TextHeader(text));
		}
	}
	
	protected void addFooterWidgetColumnWithDateHeader(Column<T, ?> column, String text, String property,
			Validator dateValidator, Date defaultLoVal, Date defaultHiVal, DateFilter dateFilter, ValueUpdater<BetweenExpressionDTO> dateUpdater,
			List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {
		FilterableDateHeader filterableDateHeader = new FilterableDateHeader(dateUpdater, dateFilter.getCriterion(
				property, new PropertyHolder(defaultLoVal), new PropertyHolder(defaultHiVal)), dateValidator, text);
		if(filterable){
			addColumn(column, filterableDateHeader);
		}else{
			addColumn(column, new TextHeader(text));
		}
	}

	public void addCheckboxColumn(int width, Triple<Button, Integer, ClickHandler> footerButton) {
		Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(T object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};

		addColumnWithCheckboxHeader(checkColumn, footerButton);
		setColumnWidth(checkColumn, width, Unit.PX);
	}
	
	protected void addColumnWithCheckboxHeader(Column<T, Boolean> checkColumn, Triple<Button, Integer, ClickHandler> footerButton){
		addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
	}

	public void addTextColumn(final Column<T, ?> column, int width, String text, String property, Triple<Button, Integer, ClickHandler> footerButton) {
		addTextColumn(column, width, text, property, new StringValidator(), footerButton);
	}
	
	public void addFooterWidgetTextColumn(final Column<T, ?> column, int width, String text, String property, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {
		addFooterWidgetTextColumn(column, width, text, property, new StringValidator(), footerWidget, colSpan);
	}
	
	public <F extends Comparable<? extends Serializable>> void addFooterWidgetTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator validator,
			List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {
		addFooterWidgetTextColumn(column, width, text, property, validator, FilterDTO.ILIKE, footerWidget, colSpan);
	}

	public <F extends Comparable<? extends Serializable>> void addTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator validator, Triple<Button, Integer, ClickHandler> footerButton) {
		addTextColumn(column, width, text, property, validator, FilterDTO.ILIKE, footerButton);
	}

	@SuppressWarnings("unchecked")
	public <F extends Comparable<? extends Serializable>> void addTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator validator, String operator, Triple<Button, Integer, ClickHandler> footerButton) {

		initializeColumn(column, property);

		final InputFilter textFilter = new InputFilter(validator, operator);

		ValueUpdater<SimpleExpressionDTO> columnUpdater = new ValueUpdater<SimpleExpressionDTO>() {
			@Override
			public void update(SimpleExpressionDTO value) {
				handleFilterValueChange(textFilter, value, column);
			}
		};

		SimpleExpressionDTO simpleExpression = filterValues.get(property);

		F defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = (F) simpleExpression.getValue().getValue();
		}

		addColumnWithTextHeader(column, text, property, validator, textFilter, defaultVal, columnUpdater, footerButton);
		this.setColumnWidth(column, width, Unit.PCT);
	}
	
	@SuppressWarnings("unchecked")
	public <F extends Comparable<? extends Serializable>> void addFooterWidgetTextColumn(final Column<T, ?> column, int width,
			String text, String property, Validator validator, String operator,
			List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {

		initializeColumn(column, property);

		final InputFilter textFilter = new InputFilter(validator, operator);

		ValueUpdater<SimpleExpressionDTO> columnUpdater = new ValueUpdater<SimpleExpressionDTO>() {
			@Override
			public void update(SimpleExpressionDTO value) {
				handleFilterValueChange(textFilter, value, column);
			}
		};

		SimpleExpressionDTO simpleExpression = filterValues.get(property);

		F defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = (F) simpleExpression.getValue().getValue();
		}

		addFooterWidgetColumnWithTextHeader(column, text, property, validator, textFilter, defaultVal, columnUpdater, footerWidget, colSpan);
		this.setColumnWidth(column, width, Unit.PCT);
	}
	
	
	@SuppressWarnings("unchecked")
	protected <F extends Comparable<? extends Serializable>> void addColumnWithTextHeader(Column<T, ?> column, String text, String property,
			Validator validator, InputFilter textFilter, F defaultVal, ValueUpdater<SimpleExpressionDTO> columnUpdater, Triple<Button, Integer, ClickHandler> footerButton){
		if(filterable){
			addColumn(column, new FilterableTextHeader(columnUpdater,
					textFilter.getCriterion(property, new PropertyHolder(defaultVal)), validator, text));
		}else{
			addColumn(column, new TextHeader(text));
		}
	}

	@SuppressWarnings("unchecked")
	protected <F extends Comparable<? extends Serializable>> void addFooterWidgetColumnWithTextHeader(Column<T, ?> column, String text, String property,
			Validator validator, InputFilter textFilter,
			F defaultVal, ValueUpdater<SimpleExpressionDTO> columnUpdater, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan) {
		if(filterable){
			addColumn(column, new FilterableTextHeader(columnUpdater, textFilter.getCriterion(property, new PropertyHolder(defaultVal)), validator, text));
		}else{
			addColumn(column, new TextHeader(text));
		}
	}
	
	public void addEnumeratedColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, Validator validator, Triple<Button, Integer, ClickHandler> footerButton,
			int columnIndex) {
		addEnumeratedColumn(column, width, text, property, options, validator, FilterDTO.EQ, footerButton, columnIndex);
	}

	@SuppressWarnings("unchecked")
	public void addEnumeratedColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, Validator validator, String operation, Triple<Button, Integer, ClickHandler> footerButton, int columnIndex) {

		initializeColumn(column, property);

		final InputFilter textFilter = new InputFilter(validator, operation/*Filter.EQ*/);

		ValueUpdater<SimpleExpressionDTO> columnUpdater = new ValueUpdater<SimpleExpressionDTO>() {
			@Override
			public void update(SimpleExpressionDTO value) {
				handleFilterValueChange(textFilter, value, column);
			}
		};

		SimpleExpressionDTO simpleExpression = filterValues.get(property);

		String defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = simpleExpression.getValue().getStringValue();
		}
		addColumnWithSelectionHeader(column, columnUpdater, textFilter, property, options, validator, text, defaultVal, footerButton, columnIndex);
		
		this.setColumnWidth(column, width, Unit.PCT);
	}
	
	@SuppressWarnings("unchecked")
	protected <F extends Enum<?>> void addColumnWithSelectionHeader(Column<T, ?> column, ValueUpdater<SimpleExpressionDTO> columnUpdater,
			InputFilter textFilter, String property, List<String> options, Validator validator, String text, String defaultVal, Triple<Button, Integer, ClickHandler> footerButton,
			int columnIndex){
		if(filterable){
			insertColumn(columnIndex, column, new FilterableSelectionHeader(columnUpdater, textFilter.getCriterion(property, new PropertyHolder(defaultVal)),
							validator, options, text));
		}else{
			insertColumn(columnIndex, column, new TextHeader(text));
		}
	}
	
	public void addSelectionColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, Triple<Button, Integer, ClickHandler> footerButton, int columnIndex) {
		addSelectionColumn(column, width, text, property, options, FilterDTO.EQ, footerButton, columnIndex);
	}
	
	public void addFooterWidgetSelectionColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, List<Pair<Widget, ClickHandler>> footerWidget, int columnIndex, Integer colSpan) {
		addFooterWidgetSelectionColumn(column, width, text, property, options, FilterDTO.EQ, footerWidget, columnIndex, colSpan);
	}

	@SuppressWarnings("unchecked")
	public void addSelectionColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, String operation, Triple<Button, Integer, ClickHandler> footerButton, int columnIndex) {

		initializeColumn(column, property);

		final SelectionFilter textFilter = new SelectionFilter(operation/*Filter.EQ*/);

		ValueUpdater<SimpleExpressionDTO> columnUpdater = new ValueUpdater<SimpleExpressionDTO>() {
			@Override
			public void update(SimpleExpressionDTO value) {
				handleFilterValueChange(null, value, column);
			}
		};

		SimpleExpressionDTO simpleExpression = filterValues.get(property);

		String defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = simpleExpression.getValue().getStringValue();
		}
		addColumnWithSelectionHeader(column, columnUpdater, textFilter, property, options, text, defaultVal, footerButton, columnIndex);
		
		this.setColumnWidth(column, width, Unit.PCT);
	}
	
	@SuppressWarnings("unchecked")
	public void addFooterWidgetSelectionColumn(final Column<T, ?> column, int width, String text,
			String property, List<String> options, String operation, List<Pair<Widget, ClickHandler>> footerWidget, int columnIndex, Integer colSpan) {

		initializeColumn(column, property);

		final SelectionFilter textFilter = new SelectionFilter(operation/*Filter.EQ*/);

		ValueUpdater<SimpleExpressionDTO> columnUpdater = new ValueUpdater<SimpleExpressionDTO>() {
			@Override
			public void update(SimpleExpressionDTO value) {
				handleFilterValueChange(null, value, column);
			}
		};

		SimpleExpressionDTO simpleExpression = filterValues.get(property);

		String defaultVal = null;

		if (simpleExpression != null) {
			defaultVal = simpleExpression.getValue().getStringValue();
		}
		addFooterWidgetColumnWithSelectionHeader(column, columnUpdater, textFilter, property, options, text, defaultVal, footerWidget, columnIndex, colSpan);
		
		this.setColumnWidth(column, width, Unit.PCT);
	}
	
	protected void addColumnWithSelectionHeader(Column<T, ?> column, ValueUpdater<SimpleExpressionDTO> columnUpdater,
			SelectionFilter textFilter, String property, List<String> options, String text, String defaultVal, Triple<Button, Integer, ClickHandler> footerButton, 
			int columnIndex){
		if(filterable){
			insertColumn(columnIndex, column, new FilterableSelectionHeader(columnUpdater, textFilter.getCriterion(property, defaultVal),
							new StringValidator(), options, text));
		}else{
			insertColumn(columnIndex, column, new TextHeader(text));
		}
	}
	
	protected void addFooterWidgetColumnWithSelectionHeader(Column<T, ?> column, ValueUpdater<SimpleExpressionDTO> columnUpdater,
			SelectionFilter textFilter, String property, List<String> options, String text, String defaultVal, List<Pair<Widget, ClickHandler>> footerWidget,
			int columnIndex, Integer colSpan) {
		if(filterable){
			insertColumn(columnIndex, column,
					new FilterableSelectionHeader(columnUpdater, textFilter.getCriterion(property, defaultVal),
							new StringValidator(), options, text));
		}else{
			insertColumn(columnIndex, column, new TextHeader(text));
		}
	}

	public interface Validator {

		boolean isEmpty(String value);

		boolean isValid(String value);

		PropertyHolder getValue(String value);

		String toString(PropertyHolder f);
	}

	protected class SelectionFilter{
	
		protected final String operation;

		public SelectionFilter(String operation) {
			this.operation = operation;
		}

		public SimpleExpressionDTO getCriterion(String property, String... vals) {
			if (vals == null || vals.length == 0) {
				return new SimpleExpressionDTO(property, operation, new PropertyHolder());
			}
			return new SimpleExpressionDTO(property, operation, new PropertyHolder(vals[0]));
		}

		public SimpleExpressionDTO setValue(SimpleExpressionDTO expression, String[] values) {
			String value = null;

			if (values.length > 0) {
				value = values[0];
			}
			expression.setValue(new PropertyHolder(value.toString()));
			return expression;
		}
	}
	
	protected class InputFilter {

		protected final Validator validator;
		protected final String operation;

		public InputFilter(Validator validator, String operation) {
			this.validator = validator;
			this.operation = operation;
		}

		public SimpleExpressionDTO getCriterion(String property, PropertyHolder... vals) {
			PropertyHolder propertyHolder = null;
			if (vals != null && vals.length == 0) {
				propertyHolder =  vals[0];
			}
			if(operation.equals(FilterDTO.LIKE) || operation.equals(FilterDTO.ILIKE)){
				return new LikeExpressionDTO(property, false, MatchMode.ANYWHERE, propertyHolder);
			}
			return new SimpleExpressionDTO(operation, property, propertyHolder);
		}

		public SimpleExpressionDTO setValue(SimpleExpressionDTO expression, String[] values) {
			String value = null;

			if (values.length > 0) {
				value = values[0];
			}

			if (validator.isEmpty(value)) {
				expression.setValue(new PropertyHolder());
				return expression;
			}
			if (validator.isValid(value)) {
				expression.setValue(new PropertyHolder(validator.getValue(value)));
				return expression;
			}

			return null;
		}

	}

	public static abstract class AbstractValidator implements Validator {
		@Override
		public boolean isEmpty(String value) {
			return value == null || value.isEmpty();
		}
	}

	public class DateValidator extends AbstractValidator {

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
		public PropertyHolder getValue(String value) {
			return new PropertyHolder().setValue(format.parse(value));
		}

		@Override
		public String toString(PropertyHolder f) {
			if (f == null || f.getDateValue() == null) {
				return "";
			}
			return format.format(f.getDateValue());
		}
	}

	public class StringValidator extends AbstractValidator {

		@Override
		public boolean isValid(String value) {
			return true;
		}

		@Override
		public PropertyHolder getValue(String value) {
			return new PropertyHolder(value);
		}

		@Override
		public String toString(PropertyHolder f) {
			if (f == null || f.getValue() == null) {
				return "";
			}
			return f.getStringValue();
		}
	}

	public class LongValidator extends AbstractValidator {

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
		public PropertyHolder getValue(String value) {
			return new PropertyHolder(Long.valueOf(value));
		}

		@Override
		public String toString(PropertyHolder f) {
			if (f == null || f.getLongValue() == null) {
				return "";
			}
			return f.getLongValue().toString();
		}
	}

	protected class DateFilter extends InputFilter {

		public DateFilter(Validator validator) {
			super(validator, null);
		}

		@Override
		public BetweenExpressionDTO getCriterion(String property, PropertyHolder... dates) {
			Date low = (dates != null && dates.length > 0) ? dates[0].getDateValue() : null;
			Date high = (dates != null && dates.length > 1) ? dates[1].getDateValue() : null;

			return FilterDTO.between(property, low, high);
		}

		@Override
		public SimpleExpressionDTO setValue(SimpleExpressionDTO expression, String[] values) {

			String low = (values != null && values.length > 0) ? values[0] : null;
			String high = (values != null && values.length > 1) ? values[1] : null;

			Date lowDate = low != null ? validator.getValue(low).getDateValue() : null;
			Date hightDate = high != null ? validator.getValue(high).getDateValue() : null;

			if (expression instanceof BetweenExpressionDTO) {
				((BetweenExpressionDTO) expression).setLoValue(new PropertyHolder(lowDate));
				((BetweenExpressionDTO) expression).setHiValue(new PropertyHolder(hightDate));
				return expression;
			}
			expression.setValue(new PropertyHolder(lowDate));
			return expression;
		}
	}

	private void handleFilterValueChange(InputFilter dataTypeFilter, SimpleExpressionDTO value, Column<T, ?> column) {
		if (value.getValue() == null && value.getOperation() == null && sortable) { 
			ColumnSortList columnSortList = getColumnSortList();
			boolean asc = false;
			if (columnSortList.size() > 0) {
				ColumnSortInfo columnSortInfo = columnSortList.get(0);
				asc = !columnSortInfo.isAscending();
				columnSortInfo.getColumn().setSortable(false);
			}

			columnSortList.clear();
			columnSortList.push(new ColumnSortInfo(column, asc));

			List<SortInfo> sortables = new ArrayList<SortInfo>();
			SortInfo sortInfo = new SortInfo(asc, value.getProperty());
			sortables.add(sortInfo);
			filter.setSortables(sortables);
			column.setSortable(sortable);

			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		} else if (value.getValue() != null) {


			filterValues.put(value.getProperty(), value);

			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		} else if (value.getValue() == null) {
			filterValues.remove(value.getProperty());
			RangeChangeEvent.fire(this, new Range(getPageStart(), getPageSize()));
		}
	}

	private void addProjectable(String property) {
		List<String> projectables = getPage().getProjectables();
		
		if (projectables == null) {
			projectables = new LinkedList<String>();
		}
		if (projectables == null) {
			// create and add ID because it is usually needed in bean
			projectables = new LinkedList<String>();
		}
		projectables.add(property);
	}

	public PageDTO getPage() {
		//TODO optimize! Do not construct page again and again
		PageDTO page = new PageDTO(getPager().getPageStart(), getPager().getPageSize());

		CriterionDTO defaultCriteria = getDefaultCriteria();

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

		ConjunctionDTO conjunction = null;

		for (Entry<String, SimpleExpressionDTO> filterValue : filterValues.entrySet()) {
			if (filterValue.getValue() != null && filterValue.getValue().getValue() != null) {
				if (conjunction == null) {
					conjunction = FilterDTO.conjunction();
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