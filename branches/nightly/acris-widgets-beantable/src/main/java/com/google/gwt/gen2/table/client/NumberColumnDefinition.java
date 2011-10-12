package com.google.gwt.gen2.table.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.gen2.table.shared.NumberColumnFilterInfo;
import com.google.gwt.gen2.table.shared.NumberColumnFilterInfo.Operator;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public abstract class NumberColumnDefinition<RowType> extends
    AbstractColumnDefinition<RowType, Double> {
  /**
   * ColumnTextFilter filters columns containing numbers
   */
  public static class NumberColumnFilter extends ColumnFilter<Double> {
    private Operator operator;
    private Operator[] supportedOperators;
    private NumberColumnResources resources;

    private NumberFormat numberFormat;
    private TextBox primaryNumberBox, secondaryNumberBox;
    private PushButton operatorButton;
    private HorizontalPanel horizontalPanel = new HorizontalPanel();

    private ClickListener clickListener = new ClickListener() {
      public void onClick(Widget sender) {
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
          secondaryNumberBox.setVisible(true);
          horizontalPanel.setCellWidth(primaryNumberBox, "50%");
          horizontalPanel.setCellWidth(secondaryNumberBox, "50%");
        } else {
          horizontalPanel.setCellWidth(primaryNumberBox, "100%");
          secondaryNumberBox.setVisible(false);
        }
        setButtonText(((PushButton) operatorButton), operator);
        fireColumnFilterChangedEvent();
      }
    };
    private KeyboardListener keyboardListener = new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        fireColumnFilterChangedEvent();
      }
    };

    /**
     * Creates a filter suitable for filtering columns containing numbers
     * @param supportedOperators 
     */
    public NumberColumnFilter(NumberFormat numberFormat) {
      this(numberFormat, Operator.values(), new DefaultNumberColumnResources());
    }

    /**
     * Creates a filter suitable for filtering columns containing numbers
     * @param supportedOperators 
     */
    public NumberColumnFilter(NumberFormat numberFormat, NumberColumnResources resources) {
      this(numberFormat, Operator.values(), resources);
    }
    /**
     * Creates a filter suitable for filtering columns containing numbers
     * @param supportedOperators 
     */
    public NumberColumnFilter(NumberFormat numberFormat, Operator[] supportedOperators, NumberColumnResources resources) {
      this.numberFormat = numberFormat;
      this.supportedOperators = supportedOperators;
      this.operator = supportedOperators[0];
      this.resources = resources;
    }

    public Widget createFilterWidget() {
      primaryNumberBox = new TextBox();
      primaryNumberBox.setWidth("100%");
      primaryNumberBox.addStyleName("numberBox");
      secondaryNumberBox = new TextBox();
      secondaryNumberBox.setWidth("100%");
      secondaryNumberBox.addStyleName("numberBox");
      operatorButton = new PushButton();
      setButtonText(operatorButton, operator);
      operatorButton.addClickListener(clickListener);
      operatorButton.addStyleName("operatorButton");
      horizontalPanel.addStyleName("columnNumberFilter");
      horizontalPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
      horizontalPanel.add(operatorButton);
      horizontalPanel.add(primaryNumberBox);
      horizontalPanel.add(secondaryNumberBox);
      horizontalPanel.setCellWidth(operatorButton, "30px");
      horizontalPanel.setCellWidth(primaryNumberBox, "100%");
      secondaryNumberBox.setVisible(false);
      horizontalPanel.setSpacing(2);
      primaryNumberBox.addKeyboardListener(keyboardListener);
      secondaryNumberBox.addKeyboardListener(keyboardListener);
      return horizontalPanel;
    }

    private void fireColumnFilterChangedEvent() {
      Double primaryNumber = null, secondaryNumber = null;
      if (primaryNumberBox.getText().length() > 0) {
        try {
          primaryNumber = numberFormat.parse(primaryNumberBox.getText());
          try {
            secondaryNumber = numberFormat.parse(secondaryNumberBox.getText());
          } catch (NumberFormatException e) {
          }
        } catch (NumberFormatException e) {
        }
      }
      fireColumnFilterChanged(new NumberColumnFilterInfo(numberFormat,
          getColumn(), primaryNumber, secondaryNumber, operator));
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
      } else if (operator == Operator.NOT_EQUAL) {
        return resources.getMessages().notEqualTooltip();
      } else if (operator == Operator.LESS_THAN) {
        return resources.getMessages().lessThanTooltip();
      } else if (operator == Operator.GREATER_THAN) {
        return resources.getMessages().greaterThanTooltip();
      } else if (operator == Operator.BETWEEN) {
        return resources.getMessages().betweenTooltip();
      }
      return "";
    }
  }

  /**
   * The {@link NumberCellRenderer} used by the {@link NumberColumnDefinition}
   * when the user does not specify one.
   * 
   * @param <RowType> the type of the row value
   */
  class NumberCellRenderer implements CellRenderer<RowType, Double> {
    private final NumberFormat numberFormat;

    public NumberCellRenderer(NumberFormat numberFormat) {
      this.numberFormat = numberFormat;
    }

    public void renderRowValue(RowType rowValue,
        ColumnDefinition<RowType, Double> columnDef,
        AbstractCellView<RowType> view) {
      Double cellValue = columnDef.getCellValue(rowValue);
      if (cellValue == null) {
        view.setText("");
      } else {
        view.setText(numberFormat.format(cellValue));
      }
    }
  }

  /**
   * An {@link InlineCellEditor} that can be used to edit {@link Number Numbers}
   * .
   */
  class NumberCellEditor extends InlineCellEditor<Double> {
    /**
     * The text field used in this editor.
     */
    private TextBoxBase textBox;
    private NumberFormat numberFormat;

    /**
     * Construct a new {@link TextCellEditor} using a normal {@link TextBox} .
     */
    public NumberCellEditor(NumberFormat numberFormat) {
      this(new TextBox(), numberFormat);
    }

    /**
     * Construct a new {@link TextCellEditor} using the specified
     * {@link TextBox}.
     * 
     * @param textBox the text box to use
     */
    public NumberCellEditor(TextBoxBase textBox, NumberFormat numberFormat) {
      super(textBox);
      this.textBox = textBox;
      this.numberFormat = numberFormat;
    }

    /**
     * Construct a new {@link TextCellEditor} using the specified
     * {@link TextBox} and images.
     * 
     * @param textBox the text box to use
     * @param images the images to use for the accept/cancel buttons
     */
    public NumberCellEditor(TextBoxBase textBox, InlineCellEditorImages images,
        NumberFormat numberFormat) {
      super(textBox, images);
      this.textBox = textBox;
      this.numberFormat = numberFormat;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, Double cellValue,
        Callback<Double> callback) {
      super.editCell(cellEditInfo, cellValue, callback);
      textBox.setFocus(true);
    }

    /**
     * @return the text box used in the editor
     */
    protected TextBoxBase getTextBox() {
      return textBox;
    }

    @Override
    protected Double getValue() {
      return numberFormat.parse(textBox.getText());
    }

    @Override
    protected void setValue(Double cellValue) {
      if (cellValue == null) {
        textBox.setText("");
      } else {
        textBox.setText(numberFormat.format(cellValue));
      }
    }
  }

  /**
   * Resources used.
   */
  public interface NumberColumnStyle extends ClientBundle {
  }

  public interface NumberColumnMessages extends Messages {
    @DefaultMessage("Shows numbers that are equal")
    String equalTooltip();

    @DefaultMessage("Shows numbers that not equal")
    String notEqualTooltip();

    @DefaultMessage("Show numbers less than given value")
    String lessThanTooltip();

    @DefaultMessage("Show numbers greater than given value")
    String greaterThanTooltip();

    @DefaultMessage("Show numbers between the given values")
    String betweenTooltip();
  }

  public interface NumberColumnResources {
    NumberColumnStyle getStyle();

    NumberColumnMessages getMessages();
  }

  protected static class DefaultNumberColumnResources implements
      NumberColumnResources {
    private NumberColumnStyle style;
    private NumberColumnMessages constants;

    public NumberColumnStyle getStyle() {
      if (style == null) {
        style = ((NumberColumnStyle) GWT.create(NumberColumnStyle.class));
      }
      return style;
    }

    public NumberColumnMessages getMessages() {
      if (constants == null) {
        constants = ((NumberColumnMessages) GWT.create(NumberColumnMessages.class));
      }
      return constants;
    }
  }

  protected NumberFormat numberFormat;

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean filterEnabled, boolean editingEnabled) {
    this(headerWidget, numberFormat, filterEnabled, Operator.values(), editingEnabled);
  }

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean filterEnabled, boolean editingEnabled, NumberColumnResources resources) {
    this(headerWidget, numberFormat, filterEnabled, Operator.values(), editingEnabled, resources);
  }

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled) {
    this(headerWidget, numberFormat, filterEnabled, supportedOperators, editingEnabled, new DefaultNumberColumnResources());
  }

  public NumberColumnDefinition(Widget headerWidget, NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled, NumberColumnResources resources) {
    this(numberFormat, filterEnabled, supportedOperators, editingEnabled, resources);
    setHeader(0, headerWidget);
  }

  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean filterEnabled, boolean editingEnabled) {
    this(header, numberFormat, filterEnabled, Operator.values(), editingEnabled);
  }

  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean filterEnabled, boolean editingEnabled, NumberColumnResources resources) {
    this(header, numberFormat, filterEnabled, Operator.values(), editingEnabled, resources);
  }

  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled) {
    this(header, numberFormat, filterEnabled, supportedOperators, editingEnabled, new DefaultNumberColumnResources());
  }
    
  public NumberColumnDefinition(String header, NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled, NumberColumnResources resources) {
    this(numberFormat, filterEnabled, supportedOperators, editingEnabled, resources);
    setHeader(0, header);
  }

  public NumberColumnDefinition(NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled) {
    this(numberFormat, filterEnabled, supportedOperators, editingEnabled, new DefaultNumberColumnResources());
  }
    
  public NumberColumnDefinition(NumberFormat numberFormat,
      boolean filterEnabled, Operator[] supportedOperators, boolean editingEnabled, NumberColumnResources resources) {
    this.numberFormat = numberFormat;
    if (filterEnabled) {
      setColumnFilter(createNumberColumnFilter(numberFormat, supportedOperators, resources));
    }
    setCellRenderer(createNumberCellRenderer(numberFormat));
    if (editingEnabled) {
      setCellEditor(createNumberCellEditor(numberFormat));
    }
  }

  /**
   * Creates the default text editor implementation. Override this method to
   * provide custom text editor
   * 
   * @return the created cell editor suitable for editing text
   */
  protected CellEditor<Double> createNumberCellEditor(NumberFormat numberFormat) {
    return new NumberCellEditor(numberFormat);
  }

  /**
   * Creates the default text renderer implementation. Override this method to
   * provide custom text renderer
   * 
   * @return the created cell renderer suitable for rendering text
   */
  protected CellRenderer<RowType, Double> createNumberCellRenderer(
      NumberFormat numberFormat) {
    return new NumberCellRenderer(numberFormat);
  }

  /**
   * Creates the default text filter implementation. Override this method to
   * provide custom filters
   * 
   * @return the created column filter suitable for filtering text columns
   */
  protected ColumnFilter<Double> createNumberColumnFilter(
      NumberFormat numberFormat, Operator []supportedOperators, NumberColumnResources resources) {
    return new NumberColumnFilter(numberFormat, supportedOperators, resources);
  }
  
  public void setCellValue(RowType rowValue, Double cellValue) {};
}