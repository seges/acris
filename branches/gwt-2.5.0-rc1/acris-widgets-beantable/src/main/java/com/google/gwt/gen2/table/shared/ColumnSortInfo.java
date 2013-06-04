package com.google.gwt.gen2.table.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Information about the sort order of a specific column in a table.
 */
public class ColumnSortInfo implements IsSerializable {
  /**
   * True if the sort order is ascending.
   */
  private boolean ascending;

  /**
   * The column index.
   */
  private int column;

  /**
   * Default constructor used for RPC.
   */
  public ColumnSortInfo() {
    this(0, true);
  }

  /**
   * Construct a new {@link ColumnSortInfo}.
   * 
   * @param column the column index
   * @param ascending true if sorted ascending
   */
  public ColumnSortInfo(int column, boolean ascending) {
    this.column = column;
    this.ascending = ascending;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ColumnSortInfo) {
      return equals((ColumnSortInfo) obj);
    }
    return false;
  }

  /**
   * Check if this object is equal to another. The objects are equal if the
   * column and ascending values are equal.
   * 
   * @param csi the other object
   * @return true if objects are the same
   */
  public boolean equals(ColumnSortInfo csi) {
    if (csi == null) {
      return false;
    }
    return getColumn() == csi.getColumn()
        && isAscending() == csi.isAscending();
  }

  /**
   * @return the column index
   */
  public int getColumn() {
    return column;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * @return true if ascending, false if descending
   */
  public boolean isAscending() {
    return ascending;
  }

  /**
   * Set whether or not the sorting is ascending or descending.
   * 
   * @param ascending true if ascending, false if descending
   */
  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  /**
   * Set the column index.
   * 
   * @param column the column index
   */
  public void setColumn(int column) {
    this.column = column;
  }
}