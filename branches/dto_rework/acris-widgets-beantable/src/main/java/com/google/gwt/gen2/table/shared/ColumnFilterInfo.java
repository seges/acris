package com.google.gwt.gen2.table.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Information about the filtering of a specific column in a table.
 */
public abstract class ColumnFilterInfo<ColType> implements
    IsSerializable {
  /**
   * The column index.
   */
  private int column;

  /**
   * Default Constructor.
   */
  public ColumnFilterInfo() {
    this(0);
  }

  /**
   * Constructor.
   * 
   * @param column the column index
   */
  public ColumnFilterInfo(int column) {
    this.column = column;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ColumnFilterInfo) {
      return equals((ColumnFilterInfo<ColType>) obj);
    }
    return false;
  }

  /**
   * Check if this object is equal to another. The objects are equal if the
   * column and filter values are equal.
   * 
   * @param csi the other object
   * @return true if objects are the same
   */
  public boolean equals(ColumnFilterInfo<ColType> cfi) {
    if (cfi == null) {
      return false;
    }
    return getColumn() == cfi.getColumn();
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

  public abstract ColType parse(String cellContent);

  public abstract boolean isFilterMatching(ColType object);

  public abstract ColumnFilterInfo<ColType> copy();

  /**
   * Set the column index.
   * 
   * @param column the column index
   */
  public void setColumn(int column) {
    this.column = column;
  }
}