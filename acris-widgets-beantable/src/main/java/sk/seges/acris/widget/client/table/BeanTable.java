package sk.seges.acris.widget.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.*;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableModel.Callback;
import com.google.gwt.gen2.table.client.property.ColumnProperty;
import com.google.gwt.gen2.table.event.client.PagingFailureEvent;
import com.google.gwt.gen2.table.event.client.PagingFailureHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.shared.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.action.ActionEvent;
import sk.seges.acris.widget.client.action.ActionListener;
import sk.seges.acris.widget.client.advanced.DoubleTextBox;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;
import sk.seges.acris.widget.client.filterpanel.FilterPanelSpec;
import sk.seges.sesam.domain.ValueHolder;
import sk.seges.sesam.service.remote.IAsyncDataRemoteLoader;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.dao.ICallback;
import sk.seges.sesam.shared.model.dao.SortInfo;
import sk.seges.sesam.shared.model.dto.*;

import java.util.*;

/**
 * @author ladislav.gazo
 */
public abstract class BeanTable<T> extends Composite implements HasDoubleClickHandlers, HasClickHandlers {

	private static final int FILTER_DELAY_MILLIS = 1000;
	private static final int DEFAULT_ROW_COUNT = 10;

	private final PagingScrollTable<T> table;
	private final BeanTableModel<T> model;
	private final CustomFixedWidthGrid dataTable;
	private final DefaultTableDefinition<T> definition;
	private final FixedWidthFlexTable headerTable;
	private final FlowPanel container;
	private PagingOptions pager;
	private FilterPanelSpec advancedFilterPanel;

	private List<String> projectables;
	private ValueHolder<CriterionDTO> filterableModel;
	private Set<ColumnDefinition<T, ?>> filterableColumnDefinitions = new HashSet<ColumnDefinition<T, ?>>();
	private sk.seges.sesam.handler.ValueChangeHandler<CriterionDTO> filterableListener;
	private Set<CriterionDTO> staticFilterables = new HashSet<CriterionDTO>();
	private Set<SortInfo> staticSortables = new HashSet<SortInfo>();
	private List<Callback> additionalLoadCallbacks;
	private boolean reloadOnEveryOnLoadCall = true;
	private boolean firstOnLoadCall = true;

	private Dialog glassDialog;

	public BeanTable() {
		container = new FlowPanel();
		container.setStyleName("acris-bean-table");

		model = new BeanTableModel<T>();
		dataTable = new CustomFixedWidthGrid();
		headerTable = new FixedWidthFlexTable();
		definition = new DefaultTableDefinition<T>();

		table = new PagingScrollTable<T>(model, dataTable, headerTable, definition);
		table.setPageSize(DEFAULT_ROW_COUNT);
		table.setHeight("100%");
		table.setWidth("100%");
		dataTable.setWidth("100%");
		headerTable.setHeight("100%");
		headerTable.setWidth("100%");

		container.add(table);

		table.addPagingFailureHandler(new PagingFailureHandler() {

			@Override
			public void onPagingFailure(PagingFailureEvent event) {
				GWT.log("Cannot load data", event.getException());
			}
		});

		glassDialog = new Dialog();
		glassDialog.setGlassEnabled(true);

		initWidget(container);
	}

	public void setEmptyTableWidget(Widget widget) {
		table.setEmptyTableWidget(widget);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (reloadOnEveryOnLoadCall) {
			reload();
		} else {
			if (firstOnLoadCall) {
				firstOnLoadCall = false;
				reload();
			}
		}
	}

	public void addAdditionalLoadCallback(Callback callback) {
		if (null == additionalLoadCallbacks) {
			additionalLoadCallbacks = new ArrayList<Callback>();
		}
		additionalLoadCallbacks.add(callback);
	}

	public void clearAdditionalLoadCallbacks() {
		additionalLoadCallbacks.clear();
		additionalLoadCallbacks = null;
	}

	/**
	 * Hack method until it will be possible to set height of the table in reasonable way
	 * 
	 * @param height
	 */
	@Deprecated
	public void setTableHeight(String height) {
		table.setHeight(height);
	}

	/**
	 * Should provide ID column name of a bean. Needed to preserve while defining constraint for projectables.
	 * 
	 * @return If it is not database POJO or ID need not to be preserved return null. Else return name of a field marked with @Id annotation.
	 */
	protected abstract String getIdProperty();

	/**
	 * Class is determined by bean table class generics parameter but for further processing (loader, DAO and paging mechanism,...) it is needed to be
	 * explicitly specified.
	 * 
	 * @return Class of the bean contained within the bean table.
	 */
	protected abstract Class<?> getProjectableResult();

	public BeanTable(IAsyncDataRemoteLoader<List<T>> loader) {
		this();
		setLoader(loader);
	}

	protected FixedWidthGrid getDataTable() {
		return dataTable;
	}

	protected PagingScrollTable<T> getTable() {
		return table;
	}

	public void setLoader(IAsyncDataRemoteLoader<List<T>> loader) {
		model.setLoader(loader);
	}

	public void setAdvancedFilterPanel(FilterPanelSpec advancedFilterPanel) {
		this.advancedFilterPanel = advancedFilterPanel;
		advancedFilterPanel.setCommonChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				reconstructFilterable();
				reload();
			}
		});
	}

	public void enablePager() {
		pager = new PagingOptions(table, true);
		container.insert(pager, 0);
	}

	public void reload() {
		table.reloadPage();
	}

	public List<T> getSelected() {
		List<T> result = new ArrayList<T>();

		Set<Integer> selectedRows = dataTable.getSelectedRows();
		for (Integer rowIndex : selectedRows) {
			result.add(table.getRowValue(rowIndex));
		}

		return result;
	}

	public void setBusyIndicatorVisible(boolean visible) {
		pager.getButtonPanel().getWidget(pager.getButtonPanel().getWidgetCount() - 1).setVisible(visible);
	}

	public int getRowCount() {
		int widgetCount = table.getWidgetCount();
		return table.getTableModel().getRowCount();
	}

	public T getRowValue(int i) {
		return table.getRowValue(i);
	}

	public void getSelected(final ActionListener<T> action) {
		int rowCount = dataTable.getSelectedRows().size();
		ColumnSortList sortList = dataTable.getColumnSortList();
		ColumnFilterList filterList = dataTable.getColumnFilterList();
		dataTable.getColumnFilterList();
		if (rowCount == 1) {
			int index = table.getCurrentPage() * DEFAULT_ROW_COUNT + dataTable.getSelectedRows().iterator().next();
			model.requestRows(new Request(index, 1, sortList, filterList), new Callback<T>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("wrongieee", caught);
				}

				@Override
				public void onRowsReady(Request request, Response<T> response) {
					action.actionPerformed(new ActionEvent<T>(response.getRowValues().next()));
				}
			});
		}
	}

	public void setSelected(Collection<T> selectedElements) {
		if (null != selectedElements) {
			int rowCount = model.getRowCount();
			for (int i = 0; i < rowCount; ++i) {
				T value = table.getRowValue(i);
				if (selectedElements.contains(value)) {
					dataTable.selectRow(i, false);
				}
			}
		}
	}

	public void addStaticFilterable(CriterionDTO filterable) {
		staticFilterables.add(filterable);
	}

	public void clearStaticFilterables() {
		staticFilterables.clear();
	}

	public void addStaticSortable(SortInfo sortable) {
		staticSortables.add(sortable);
	}

	public void clearStaticSortables() {
		staticSortables.clear();
	}

	private void reconstructFilterable() {
		boolean skip = false;

		ConjunctionDTO conjunction = FilterDTO.conjunction();
		for (ColumnDefinition<T, ?> columnDefinition : filterableColumnDefinitions) {
			FilterProperty filterProperty = columnDefinition.getColumnProperty(FilterProperty.TYPE);
			CriterionDTO filterable = filterProperty.getFilterable();
			if (filterable instanceof SimpleExpressionDTO) {
				SimpleExpressionDTO expr = (SimpleExpressionDTO) filterable;
				Object value = filterProperty.getWidget().getValue();
				if (value == null || (value instanceof String && "".equals(value))) {
					// filter value not set
					skip = true;
				} else {
					expr.setValue(new PropertyHolder(value));
				}
			}
			if (!skip) {
				conjunction.add(filterable);
			} else {
				skip = false;
			}
		}
		for (CriterionDTO staticFilterable : staticFilterables) {
			conjunction.add(staticFilterable);
		}
		if (null != advancedFilterPanel) {
			conjunction.and(advancedFilterPanel.constructSearchCriteria());
		}
		getFilterableModel().setValue(conjunction);
	}

	private ValueHolder<CriterionDTO> getFilterableModel() {
		if (filterableModel == null) {
			setFilterableModel(new ValueHolder<CriterionDTO>());
		}
		return filterableModel;
	}

	public void addColumn(String label, ColumnDefinition<T, ?> columnDefinition) {
		definition.addColumnDefinition(columnDefinition);
		final int count = definition.getColumnDefinitionCount();
		headerTable.setText(0, count - 1, label);

		FilterProperty filterProperty = columnDefinition.getColumnProperty(FilterProperty.TYPE);
		if (filterProperty.isFilterEnabled()) {
			// Widget filter = GWT.create(filterProperty.getWidgetType());
			Widget filter = null;

			if (filterProperty instanceof FilterEnumProperty<?>) {
				if (filterProperty.getWidgetType() != null && filterProperty.getWidgetType().equals(EnumListBoxWithValue.class)) {
					filter = new EnumListBoxWithValue(((FilterEnumProperty) filterProperty).getClazz());
					((EnumListBoxWithValue) filter).load(((FilterEnumProperty) filterProperty).getEnumMap());
				}
			}

			if (filter == null) {
				if (filterProperty.getWidgetType() != null && filterProperty.getWidgetType().equals(DateBox.class)) {
					filter = GWT.create(DateBox.class);
				} else if (DoubleBox.class.equals(filterProperty.getWidgetType())) {
					filter = new DoubleBox();
				} else if (DoubleTextBox.class.equals(filterProperty.getWidgetType())) {
					filter = new DoubleTextBox();
				} else {
					filter = new com.google.gwt.user.client.ui.TextBox();
				}
			}

			if (!(filter instanceof HasValue<?>)) {
				throw new RuntimeException("Unable to filter without possibility to get value = " + filter);
			}
			// add our filterable model
			getFilterableModel();

			filterableColumnDefinitions.add(columnDefinition);
			filterProperty.setWidget((HasValue<?>) filter);

			if (filter instanceof HasChangeHandlers) {
				((HasChangeHandlers) filter).addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						reconstructFilterable();
					}
				});
			}

			if (filter instanceof HasKeyPressHandlers) {
				HasKeyPressHandlers keypressable = (HasKeyPressHandlers) filter;
				keypressable.addKeyPressHandler(new KeyPressHandler() {

					private Timer timer = new Timer() {

						@Override
						public void run() {
							reconstructFilterable();
						}
					};

					@Override
					public void onKeyPress(KeyPressEvent event) {
						int keyCode = event.getNativeEvent().getKeyCode();
						if (KeyCodes.KEY_ENTER == keyCode) {
							reconstructFilterable();
						} else {
							timer.cancel();
							timer.schedule(FILTER_DELAY_MILLIS);
						}
					}
				});
			}

			if (filter instanceof DateBox) {
				DateBox dateBoxFilter = (DateBox) filter;
				dateBoxFilter.addValueChangeHandler(new ValueChangeHandler<Date>() {

					@Override
					public void onValueChange(ValueChangeEvent<Date> arg0) {
						reconstructFilterable();
					}
				});

				TextBox dateTextBox = dateBoxFilter.getTextBox();
				dateTextBox.addBlurHandler(new BlurHandler() {

					@Override
					public void onBlur(BlurEvent arg0) {
						reconstructFilterable();
					}
				});
				dateTextBox.addKeyPressHandler(new KeyPressHandler() {

					@Override
					public void onKeyPress(KeyPressEvent event) {
						if (KeyCodes.KEY_ENTER == event.getCharCode()) {
							reconstructFilterable();
						}
					}
				});

				dateBoxFilter.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)));
			}

			if (filter instanceof EnumListBoxWithValue<?>) {
				((EnumListBoxWithValue<?>) filter).addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						reconstructFilterable();
					}
				});
			}

			headerTable.setWidget(1, count - 1, filter);
		}
		addProjectable(checkAndGetDomainObjectProperty(columnDefinition).getField());
	}

	private void addProjectable(String property) {
		if (projectables == null) {
			// create and add ID because it is usually needed in bean
			projectables = new LinkedList<String>();
			if (getIdProperty() != null) {
				projectables.add(getIdProperty());
			}
		}
		projectables.add(property);
	}

	public void setFilterableModel(ValueHolder<CriterionDTO> filterableModel) {
		if (this.filterableModel != null) {
			this.filterableModel.removePropertyChangeListener(getFilterableListener());
		}
		this.filterableModel = filterableModel;
		filterableModel.addPropertyChangeListener(getFilterableListener());
	}

	private sk.seges.sesam.handler.ValueChangeHandler<CriterionDTO> getFilterableListener() {
		if (filterableListener == null) {
			filterableListener = new sk.seges.sesam.handler.ValueChangeHandler<CriterionDTO>() {
				
				@Override
				public void onValueChanged(CriterionDTO oldValue, CriterionDTO newValue) {
					// reload everytime filterable object changes
					reload();
				}
			};
		}
		return filterableListener;
	}

	private DomainObjectProperty checkAndGetDomainObjectProperty(ColumnDefinition<T, ?> columnDef) {
		DomainObjectProperty domainObjectProperty = columnDef.getColumnProperty(DomainObjectProperty.TYPE);
		if (domainObjectProperty == null) {
			throw new RuntimeException("Unable to read field name for bean from the column definition = " + columnDef);
		}
		return domainObjectProperty;
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return dataTable.addDoubleClickHandler(handler);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return dataTable.addClickHandler(handler);
	}

	/**
	 * use addClickHandler
	 * 
	 * @param handler
	 * @return
	 */
	@Deprecated
	public HandlerRegistration addRowSelectionHandler(RowSelectionHandler handler) {
		return dataTable.addRowSelectionHandler(handler);
	}

	private class BeanTableModel<E> extends MutableTableModel<E> {
		private IAsyncDataRemoteLoader<List<E>> loader;

		public void setLoader(IAsyncDataRemoteLoader<List<E>> loader) {
			this.loader = loader;
		}

		@Override
		protected boolean onRowInserted(int beforeRow) {
			return false;
		}

		@Override
		protected boolean onRowRemoved(int row) {
			return false;
		}

		@Override
		protected boolean onSetRowValue(int row, E rowValue) {
			return true;
		}

		@Override
		public void requestRows(final Request request, final Callback<E> callback) {
			if (loader == null) {
				return;
			}
			PageDTO page = new PageDTO(request.getStartRow(), request.getNumRows());
			enrichWithSortables(request, page);
			enrichWithProjectables(page);
			enrichWithFilterable(page);

			loader.load(page, new ICallback<PagedResultDTO<List<E>>>() {
				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
					if (null != additionalLoadCallbacks) {
						for (Callback callback : additionalLoadCallbacks) {
							callback.onFailure(caught);
						}
					}
				}

				@Override
				public void onSuccess(PagedResultDTO<List<E>> result) {
					SerializableResponse response = new SerializableResponse(result.getResult());
					response.setRowCount(result.getTotalResultCount());
					setRowCount(result.getTotalResultCount());
					callback.onRowsReady(request, response);
					if (null != additionalLoadCallbacks) {
						for (Callback callback : additionalLoadCallbacks) {
							callback.onRowsReady(request, response);
						}
					}
				}
			});
		}

		private void enrichWithFilterable(PageDTO page) {
			if (filterableModel == null && staticFilterables.size() == 0) {
				return;
			}
			CriterionDTO filterable = getFilterableModel().getValue();
			if (staticFilterables.size() > 0) {
				if (filterable == null) {
					// no filter columns yet
					filterable = FilterDTO.conjunction();
				}
				// add all static filterables
				JunctionDTO junction = (JunctionDTO) filterable;
				for (CriterionDTO crit : staticFilterables) {
					junction.add(crit);
				}
			}
			page.setFilterable(filterable);
		}

		private void enrichWithProjectables(PageDTO page) {
			if (projectables == null) {
				return;
			}
			page.setProjectables(projectables);
			page.setProjectableResult(getProjectableResult().getName());
		}

		private void enrichWithSortables(final Request request, PageDTO page) {
			if (request.getColumnSortList() == null) {
				return;
			}
			List<SortInfo> infoSet = new ArrayList<SortInfo>();
			for (ColumnSortInfo sortInfo : request.getColumnSortList()) {
				ColumnDefinition<T, ?> columnDef = definition.getColumnDefinition(sortInfo.getColumn());
				DomainObjectProperty domainObjectProperty = checkAndGetDomainObjectProperty(columnDef);
				SortInfo info = new SortInfo(sortInfo.isAscending(), domainObjectProperty.getField());
				infoSet.add(info);
			}
			for (SortInfo info : staticSortables) {
				infoSet.add(info);
			}

			if (page.getSortables() == null) {
				page.setSortables(infoSet);
			} else {
				page.getSortables().addAll(infoSet);
			}
//			for (SortInfo sortInfo : infoSet) {
//				page.addSortable(sortInfo);
//			}
		}
	}

	/* *******************************
	 * ****** Begin of delegates **** ******************************
	 */

	public void setSelectionEnabled(boolean enabled) {
		dataTable.setSelectionEnabled(enabled);
	}

	public void setSelectionPolicy(SelectionPolicy policy) {
		dataTable.setSelectionPolicy(policy);
	}

	/* *******************************
	 * ****** End of delegates **** ******************************
	 */

	/**
	 * A column property bound to a bean as underlying source of the information. Especially relation between column ID and field name/expression is
	 * provided. It should ease manipulation with table representation of the bean.
	 * 
	 * @see BeanTable
	 * 
	 * @author eldzi
	 */
	public static class DomainObjectProperty extends ColumnProperty {
		public static final Type<DomainObjectProperty> TYPE = new Type<DomainObjectProperty>() {
			private DomainObjectProperty instance = null;

			@Override
			public DomainObjectProperty getDefault() {
				if (instance == null) {
					instance = new DomainObjectProperty(null);
				}
				return instance;
			}
		};

		private String field;

		public DomainObjectProperty(String field) {
			this.field = field;
		}

		/** Bean's field name/expression representing the column in a table. */
		public String getField() {
			return field;
		}
	}

	/**
	 * used if column filter is listbox, which should be filled by enum items<br />
	 * On UI {@link EnumListBoxWithValue} is used, for it's initialization we need to specified enum class and list of enum items (usually
	 * MyEnum.values()) or map of enum items and their translated values
	 * 
	 * @author marta
	 * 
	 */
	public static class FilterEnumProperty<T extends Enum<T>> extends FilterProperty {
		private Class<T> clazz = null;
		private Map<T, String> enumMap = null;

		public FilterEnumProperty(Class<? extends Widget> widgetType, CriterionDTO filterable, Class<T> enumClazz, List<T> enumList) {
			super(widgetType, filterable);
			this.clazz = enumClazz;
			enumMap = new HashMap<T, String>();
			for (T enum1 : enumList) {
				enumMap.put(enum1, enum1.name());
			}
		}

		public FilterEnumProperty(Class<? extends Widget> widgetType, CriterionDTO filterable, Class<T> enumClazz, Map<T, String> enumMap) {
			super(widgetType, filterable);
			this.clazz = enumClazz;
			this.enumMap = enumMap;
		}

		public Class<T> getClazz() {
			return clazz;
		}

		public void setClazz(Class<T> clazz) {
			this.clazz = clazz;
		}

		public Map<T, String> getEnumMap() {
			return enumMap;
		}

		public void setEnumMap(Map<T, String> enumMap) {
			this.enumMap = enumMap;
		}

	}

	public static class FilterProperty extends ColumnProperty {
		public static final Type<FilterProperty> TYPE = new Type<FilterProperty>() {
			private FilterProperty instance = null;

			@Override
			public FilterProperty getDefault() {
				if (instance == null) {
					instance = new FilterProperty();
				}
				return instance;
			}
		};

		private final Class<? extends Widget> widgetType;
		private final CriterionDTO filterable;
		private HasValue<?> widget;

		public FilterProperty() {
			this(null, null);
		}

		public FilterProperty(Class<? extends Widget> widgetType, CriterionDTO filterable) {
			this.widgetType = widgetType;
			this.filterable = filterable;
		}

		/**
		 * @return true if filter is enabled
		 */
		public boolean isFilterEnabled() {
			return (widgetType != null);
		}

		public Class<? extends Widget> getWidgetType() {
			return widgetType;
		}

		public CriterionDTO getFilterable() {
			return filterable;
		}

		public HasValue<?> getWidget() {
			return widget;
		}

		public void setWidget(HasValue<?> widget) {
			this.widget = widget;
		}
	}

	public PagingOptions getPager() {
		return pager;
	}

	public boolean isReloadOnEveryOnLoadCall() {
		return reloadOnEveryOnLoadCall;
	}

	public void setReloadOnEveryOnLoadCall(boolean reloadOnEverySetVisibleTrue) {
		this.reloadOnEveryOnLoadCall = reloadOnEverySetVisibleTrue;
	}

	public void showGlass(boolean show) {
		if (show) {
			glassDialog.show();
		} else {
			glassDialog.hide();
		}
	}
}
