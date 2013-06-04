package com.google.gwt.gen2.table.client;

public interface RowValue {
  public enum DateColumnFormat {
    PATTERN, SHORT_DATE_FORMAT, MEDIUM_DATE_FORMAT, LONG_DATE_FORMAT, 
    SHORT_DATE_TIME_FORMAT, MEDIUM_DATE_TIME_FORMAT, LONG_DATE_TIME_FORMAT, 
    SHORT_TIME_FORMAT, MEDIUM_TIME_FORMAT, LONG_TIME_FORMAT;
  };
  
  public enum NumberColumnFormat {
    PATTERN, SCIENTIFIC_FORMAT, CURRENCY_FORMAT, PERCENT_FORMAT, DECIMAL_FORMAT; 
  };
  
  public @interface ColumnDefinition {
    int column();
    String header();
    boolean filterable() default true;
    boolean sortable() default true;
    boolean editable() default false;
    DateColumnFormat dateTimeFormat() default DateColumnFormat.SHORT_DATE_FORMAT; 
    String dateTimePattern() default "";
    NumberColumnFormat numberFormat() default NumberColumnFormat.DECIMAL_FORMAT; 
    String numberPattern() default "";
  }
}
