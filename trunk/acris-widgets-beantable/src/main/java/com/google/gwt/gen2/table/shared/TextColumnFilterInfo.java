package com.google.gwt.gen2.table.shared;


/**
 * 
 */
public class TextColumnFilterInfo extends ColumnFilterInfo<String> {
  public enum Operator {
    STARTS_WITH("xx.."),
    STARTS_WITH_CASE_SENSITIVE("Xx.."),
    CONTAINS("..xx.."),
    CONTAINS_CASE_SENSITIVE("..Xx.."),
    ENDS_WITH("..xx"),
    ENDS_WITH_CASE_SENSITIVE("..Xx");
    
    private final String symbol;

    Operator(String symbol) {
      this.symbol = symbol;
    }

    public String getSymbol() {
      return symbol;
    }
  };      

  private String filterText;
  private Operator operator;

  public TextColumnFilterInfo() {
    super();
  }

  public TextColumnFilterInfo(int column, String filterText, Operator operator) {
    super(column);
    this.filterText = filterText;
    this.operator = operator;
  }

  public String getFilterText() {
    return filterText;
  }

  public Operator getOperator() {
    return operator;
  }

  public String parse(String cellContent) {
    return cellContent;
  }

  public boolean isFilterMatching(String value) {
    if (operator == Operator.STARTS_WITH) {
      return value.toLowerCase().startsWith(filterText.toLowerCase());
    } else if (operator == Operator.STARTS_WITH_CASE_SENSITIVE) {
      return value.startsWith(filterText);
    } else if (operator == Operator.CONTAINS) {
      return value.toLowerCase().contains(filterText.toLowerCase());
    } else if (operator == Operator.CONTAINS_CASE_SENSITIVE) {
      return value.contains(filterText);
    } else if (operator == Operator.ENDS_WITH) {
      return value.toLowerCase().endsWith(filterText.toLowerCase());
    } else if (operator == Operator.ENDS_WITH_CASE_SENSITIVE) {
      return value.endsWith(filterText);
    }
    return false;
  }

  public ColumnFilterInfo<String> copy() {
    return new TextColumnFilterInfo(getColumn(), filterText, operator);
  }
}