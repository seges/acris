package com.google.gwt.gen2.table.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.gen2.table.shared.DateColumnFilterInfo;
import com.google.gwt.gen2.table.shared.DateColumnFilterInfo.Operator;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public abstract class DateColumnDefinition<RowType> extends
    AbstractColumnDefinition<RowType, Date> {
  /**
   * DateColumnFilter filters columns based on dates
   */
  public static class DateColumnFilter extends ColumnFilter<Date> {
    private Operator operator;
    private Operator[] supportedOperators;
    private DateTimeFormat dateTimeFormat;
    private DateColumnResources resources;

    private DateBox primaryDateBox, secondaryDateBox;
    private PushButton operatorButton;
    private HorizontalPanel horizontalPanel = new HorizontalPanel();

    private ClickHandler clickHandler = new ClickHandler() {
      public void onClick(ClickEvent sender) {
        for (int i = 0; i < supportedOperators.length; i++) {
          if (operator == supportedOperators[i]) {
            if (i < supportedOperators.length - 1) {
              operator = supportedOperators[i + 1];
            } else {
              operator = supportedOperators[0];
            }
            break;
          }
        }
        if (operator == Operator.BETWEEN) {
          secondaryDateBox.setVisible(true);
          horizontalPanel.setCellWidth(primaryDateBox, "50%");
          horizontalPanel.setCellWidth(secondaryDateBox, "50%");
        } else {
          horizontalPanel.setCellWidth(primaryDateBox, "100%");
          secondaryDateBox.setVisible(false);
        }
        setButtonText(((PushButton) operatorButton), operator);
        Date primaryDate = primaryDateBox.getDatePicker().getValue();
        Date secondaryDate = secondaryDateBox.getDatePicker().getValue();
        fireColumnFilterChangedEvent(primaryDate, secondaryDate);
      }
    };
    private ValueChangeHandler<Date> valueChangeHandler = new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        fireColumnFilterChangedEvent();
      }
    };

    public DateColumnFilter(DateTimeFormat dateTimeFormat) {
      this(dateTimeFormat, Operator.values());
    }

    public DateColumnFilter(DateTimeFormat dateTimeFormat, Operator[] supportedOperators) {
      this(dateTimeFormat, supportedOperators, new DefaultDateColumnResources());
    }
    
    /**
     * Creates a filter suitable for filtering columns containing dates
     * 
     * @param dateTimeFormat the dateTimeFormat used for the formatting of the
     *          filter text
     */
    public DateColumnFilter(DateTimeFormat dateTimeFormat,
        DateColumnResources resources) {
      this(dateTimeFormat, Operator.values(), resources);
    }

    /**
     * Creates a filter suitable for filtering columns containing dates
     * 
     * @param dateTimeFormat the dateTimeFormat used for the formatting of the
     *          filter text
     */
    public DateColumnFilter(DateTimeFormat dateTimeFormat,
        Operator[] supportedOperators, DateColumnResources resources) {
      this.dateTimeFormat = dateTimeFormat;
      this.supportedOperators = supportedOperators;
      this.resources = resources;
      this.operator = supportedOperators[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.google.gwt.widgetideas.table.client.filter.ColumnFilter#
     * createFilterWidget()
     */
    public Widget createFilterWidget() {
      DatePicker datePicker = new DatePicker();

      primaryDateBox = new DateBox();
      primaryDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
      primaryDateBox.setWidth("100%");
      primaryDateBox.addStyleName("dateBox");
      primaryDateBox.addValueChangeHandler(valueChangeHandler);
      secondaryDateBox = new DateBox();
      secondaryDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
      secondaryDateBox.setWidth("100%");
      secondaryDateBox.addStyleName("dateBox");
      secondaryDateBox.addValueChangeHandler(valueChangeHandler);
      operatorButton = new PushButton();
      setButtonText(operatorButton, operator);
      operatorButton.addClickHandler(clickHandler);
      operatorButton.addStyleName("operatorButton");
      horizontalPanel.addStyleName("columnDateFilter");
      horizontalPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
      horizontalPanel.add(operatorButton);
      horizontalPanel.add(primaryDateBox);
      horizontalPanel.add(secondaryDateBox);
      horizontalPanel.setCellWidth(operatorButton, "30px");
      horizontalPanel.setCellWidth(primaryDateBox, "100%");
      secondaryDateBox.setVisible(false);
      return horizontalPanel;
    }

    private void fireColumnFilterChangedEvent() {
      Date primaryDate = primaryDateBox.getValue();
      Date secondaryDate = secondaryDateBox.getValue();
      fireColumnFilterChangedEvent(primaryDate, secondaryDate);
    }

    private void fireColumnFilterChangedEvent(Date primaryDate,
        Date secondaryDate) {
      fireColumnFilterChanged(new DateColumnFilterInfo(getColumn(),
          dateTimeFormat.getPattern(), primaryDate, secondaryDate, operator));
    }

    private void setButtonText(PushButton pushButton, Operator operator) {
      pushButton.getUpFace().setText(operator.getSymbol());
      pushButton.getUpHoveringFace().setText(operator.getSymbol());
      pushButton.getDownFace().setText(operator.getSymbol());
      pushButton.setTitle(getOperatorTooltip(operator));
    }

    private String getOperatorTooltip(Operator operator) {
      if (operator == Operator.EQUAL) {
        return resources.getMessages().equalTooltip();
      } else if (operator == Operator.UNEQUAL) {
        return resources.getMessages().unequalTooltip();
      } else if (operator == Operator.BEFORE) {
        return resources.getMessages().beforeTooltip();
      } else if (operator == Operator.AFTER) {
        return resources.getMessages().afterTooltip();
      } else if (operator == Operator.BETWEEN) {
        return resources.getMessages().betweenTooltip();
      }
      return "";
    }
  }

  class DateCellRenderer implements CellRenderer<RowType, Date> {
    private final DateTimeFormat dateTimeFormat;

    public DateCellRenderer(DateTimeFormat dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
    }

    public void renderRowValue(RowType rowValue,
        ColumnDefinition<RowType, Date> columnDef,
        AbstractCellView<RowType> view) {
      Date cellValue = columnDef.getCellValue(rowValue);
      if (cellValue == null) {
        view.setHTML("&nbsp;");
      } else {
        if (dateTimeFormat != null) {
          view.setHTML(dateTimeFormat.format(cellValue));
        } else {
          view.setHTML(cellValue.toString());
        }
      }
    }
  }

  /**
   * An {@link InlineCellEditor} that can be used to edit {@link Date Dates}.
   */
  class DateCellEditor extends InlineCellEditor<Date> {
    /**
     * The date box used in this editor.
     */
    private DateBox dateBox;

    /**
     * Construct a new {@link DateCellEditor} using a {@link DateBox}.
     * 
     * @param dateTimeFormat the {@link DateTimeFormat} used for inline editing
     */
    public DateCellEditor(DateTimeFormat dateTimeFormat) {
      this(new DateBox(), dateTimeFormat);
    }

    /**
     * Construct a new {@link DateCellEditor} using a {@link DateBox}.
     * 
     * @param dateBox the {@link DateBox} used for inline editing
     * @param dateTimeFormat the {@link DateTimeFormat} used for inline editing
     */
    public DateCellEditor(DateBox dateBox, DateTimeFormat dateTimeFormat) {
      super(dateBox);
      dateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, Date cellValue,
        Callback<Date> callback) {
      super.editCell(cellEditInfo, cellValue, callback);
      dateBox.setFocus(true);
    }

    /**
     * @return the date box used in the editor
     */
    protected DateBox getDateBox() {
      return dateBox;
    }

    @Override
    protected Date getValue() {
      return dateBox.getDatePicker().getValue();
    }

    @Override
    protected void setValue(Date cellValue) {
      if (cellValue == null) {
        cellValue = new Date();
      }
      dateBox.getDatePicker().setValue(cellValue);
    }
  }

  /**
   * Resources used.
   */
  public interface DateColumnStyle extends ClientBundle {
  }

  public interface DateColumnMessages extends Messages {
    @DefaultMessage("Shows only dates that are equal")
    String equalTooltip();

    @DefaultMessage("Shows only dates that not equal")
    String unequalTooltip();

    @DefaultMessage("Show only dates before the given date")
    String beforeTooltip();

    @DefaultMessage("Show only dates after the given date")
    String afterTooltip();

    @DefaultMessage("Show only dates between the given dates")
    String betweenTooltip();
  }

  public interface DateColumnResources {
    DateColumnStyle getStyle();

    DateColumnMessages getMessages();
  }

  protected static class DefaultDateColumnResources implements
      DateColumnResources {
    private DateColumnStyle style;
    private DateColumnMessages constants;

    public DateColumnStyle getStyle() {
      if (style == null) {
        style = ((DateColumnStyle) GWT.create(DateColumnStyle.class));
      }
      return style;
    }

    public DateColumnMessages getMessages() {
      if (constants == null) {
        constants = ((DateColumnMessages) GWT.create(DateColumnMessages.class));
      }
      return constants;
    }
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled, boolean editingEnabled) {
    this(header, dateTimeFormat, sortingEnabled, filterEnabled, Operator.values(), editingEnabled);
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled,
      Operator[] supportedOperators, boolean editingEnabled) {
    this(header, dateTimeFormat, sortingEnabled, filterEnabled, supportedOperators,
        editingEnabled, new DefaultDateColumnResources());
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled, boolean editingEnabled,
      DateColumnResources resources) {
    this(header, dateTimeFormat, sortingEnabled, filterEnabled, Operator.values(), editingEnabled,
        resources);
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled,
      Operator[] supportedOperators, boolean editingEnabled,
      DateColumnResources resources) {
    this(dateTimeFormat, sortingEnabled, filterEnabled, supportedOperators,
        editingEnabled, resources);
    setHeader(0, header);
  }

  public DateColumnDefinition(Widget headerWidget,
      DateTimeFormat dateTimeFormat, boolean sortingEnabled,
      boolean filterEnabled, Operator[] supportedOperators,
      boolean editingEnabled) {
    this(headerWidget, dateTimeFormat, sortingEnabled, filterEnabled, supportedOperators,
        editingEnabled, new DefaultDateColumnResources());
  }

  public DateColumnDefinition(Widget headerWidget,
      DateTimeFormat dateTimeFormat, boolean sortingEnabled,
      boolean filterEnabled, boolean editingEnabled,
      DateColumnResources resources) {
    this(headerWidget, dateTimeFormat, sortingEnabled, filterEnabled, Operator.values(), editingEnabled,
        resources);
  }
  
  public DateColumnDefinition(Widget headerWidget,
      DateTimeFormat dateTimeFormat, boolean sortingEnabled,
      boolean filterEnabled, Operator[] supportedOperators,
      boolean editingEnabled, DateColumnResources resources) {
    this(dateTimeFormat, sortingEnabled, filterEnabled, supportedOperators,
        editingEnabled, resources);
    setHeader(0, headerWidget);
  }
 
  /**
   * Column definition used for columns containing {@link Date} objects
   * 
   * @param dateTimeFormat
   * @param sortingEnabled
   * @param filterEnabled
   * @param editingEnabled
   */
  public DateColumnDefinition(DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled, boolean editingEnabled) {
    this(dateTimeFormat, sortingEnabled, filterEnabled, editingEnabled,
        new DefaultDateColumnResources());
  }

  /**
   * Column definition used for columns containing {@link Date} objects
   * 
   * @param dateTimeFormat
   * @param sortingEnabled
   * @param filterEnabled
   * @param editingEnabled
   */
  public DateColumnDefinition(DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled, boolean editingEnabled, DateColumnResources resources) {
    this(dateTimeFormat, sortingEnabled, filterEnabled, Operator.values(), editingEnabled, resources);
  }

  /**
   * Column definition used for columns containing {@link Date} objects
   * 
   * @param dateTimeFormat
   * @param sortingEnabled
   * @param filterEnabled
   * @param supportedOperators
   * @param editingEnabled
   */
  public DateColumnDefinition(DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean filterEnabled,
      Operator[] supportedOperators, boolean editingEnabled,
      DateColumnResources resources) {
    setColumnSortable(sortingEnabled);
    if (filterEnabled) {
      setColumnFilter(createDateColumnFilter(dateTimeFormat,
          supportedOperators, resources));
    }
    setCellRenderer(createDateCellRenderer(dateTimeFormat));
    if (editingEnabled) {
      setCellEditor(createDateCellEditor(dateTimeFormat));
    }
  }

  /**
   * Creates the default date filter implementation. Override this method to
   * provide custom filters
   * 
   * @return the created column filter suitable for filtering date columns
   */
  protected ColumnFilter<Date> createDateColumnFilter(
      DateTimeFormat dateTimeFormat, Operator[] supportedOperators,
      DateColumnResources resources) {
    return new DateColumnFilter(dateTimeFormat, supportedOperators, resources);
  }

  /**
   * Creates the default date renderer implementation. Override this method to
   * provide custom date renderer
   * 
   * @return the created cell renderer suitable for rendering dates
   */
  protected CellRenderer<RowType, Date> createDateCellRenderer(
      DateTimeFormat dateTimeFormat) {
    return new DateCellRenderer(dateTimeFormat);
  }

  /**
   * Creates the default date editor implementation. Override this method to
   * provide custom date editor
   * 
   * @return the created cell editor suitable for editing dates
   */
  protected CellEditor<Date> createDateCellEditor(DateTimeFormat dateTimeFormat) {
    return new DateCellEditor(dateTimeFormat);
  }

  public void setCellValue(RowType rowValue, Date cellValue) {
  };
}