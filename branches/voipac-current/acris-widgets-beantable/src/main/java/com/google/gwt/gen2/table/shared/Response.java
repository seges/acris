package com.google.gwt.gen2.table.shared;

import java.util.Iterator;

/**
 * A response from the {@link TableModelHelper}.
 * 
 * @param <RowType> the data type of the row values
 */
public abstract class Response<RowType> {
  /**
   * Get the objects associated with the retrieved rows.
   * 
   * @return the objects associated with the retrieved row
   */
  public abstract Iterator<RowType> getRowValues();

  /**
   * Get the number of objects available in total. This can differ from the
   * number of row values in case of paging
   * 
   * @return the number of objects available in total
   */
  public abstract int getRowCount();
}