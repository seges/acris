package com.google.gwt.gen2.table.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A {@link TableModelHelper} request.
 */
public class Request implements IsSerializable {
  /**
   * The number of rows to request.
   */
  private int numRows;

  /**
   * An ordered list of ColumnSortInfo.
   */
  private ColumnSortList columnSortList;

  /**
   * An ordered list of ColumnFilterInfo.
   */
  private ColumnFilterList columnFilterList;

  /**
   * The first row of table data to request.
   */
  private int startRow;

  /**
   * Default constructor.
   */
  public Request() {
    this(0, 0, null);
  }

  /**
   * Constructor.
   * 
   * @param startRow the first row to request
   * @param numRows the number of rows to request
   */
  public Request(int startRow, int numRows) {
    this(startRow, numRows, null);
  }

  /**
   * Constructor.
   * 
   * @param startRow the first row to request
   * @param numRows the number of rows to request
   * @param columnSortList column sort orders
   */
  public Request(int startRow, int numRows, ColumnSortList columnSortList) {
    this.startRow = startRow;
    this.numRows = numRows;
    this.columnSortList = columnSortList;
  }

  /**
   * Constructor.
   * 
   * @param startRow the first row to request
   * @param numRows the number of rows to request
   * @param columnSortList column sort orders
   * @param columnFilterList column filters
   */
  public Request(int startRow, int numRows, ColumnSortList columnSortList,
      ColumnFilterList columnFilterList) {
    this.startRow = startRow;
    this.numRows = numRows;
    this.columnSortList = columnSortList;
    this.columnFilterList = columnFilterList;
  }

  /**
   * Get the ColumnSortInfo, which includes the sort indexes and ascending or
   * descending order info.
   * 
   * @return the sort info
   */
  public ColumnSortList getColumnSortList() {
    return columnSortList;
  }

  /**
   * Get the ColumnFilterInfo, which includes the filter information
   * 
   * @return the sort info
   */
  public ColumnFilterList getColumnFilterList() {
    return columnFilterList;
  }

  /**
   * Get the number of rows to request.
   * 
   * @return the number of requested rows
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * Get the first row to request.
   * 
   * @return the first requested row
   */
  public int getStartRow() {
    return startRow;
  }
}