/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.gen2.table.client;

import com.google.gwt.gen2.table.shared.ColumnFilterInfo;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * A column filter responsible for filtering specific columns
 */
public abstract class ColumnFilter<ColType> {
  /**
   * The {@link ColumnFilterListener} interface
   */
  public interface ColumnFilterListener {
    /**
     * Invoked whenever the user changes the filter settings
     * @param info the {@link ColumnFilterInfo} 
     */
    void onFilterChanged(ColumnFilterInfo info);
  }
  
  private int column;

  protected List<ColumnFilterListener> columnFilterListeners;

  /**
   * @param listener the listener to add
   */
  public void addColumnFilterListener(ColumnFilterListener listener) {
    if (columnFilterListeners == null) {
      columnFilterListeners = new ArrayList<ColumnFilterListener>();
    }
    columnFilterListeners.add(listener);
  }

  /**
   * @return Subclasses have to implement this method in order to return the filter widget
   */
  public abstract Widget createFilterWidget();

  /**
   * @param listener the listener to remove
   */
  public void fireColumnFilterChanged(ColumnFilterInfo<ColType> info) {
    if (columnFilterListeners != null) {
      for (ColumnFilterListener columnFilterListener : columnFilterListeners) {
        columnFilterListener.onFilterChanged(info);
      }
    }
  }

  /**
   * @return the column for which this filter applies
   */
  public int getColumn() {
    return column;
  }

  /**
   * @param column the column for which this filter applies
   */
  public void setColumn(int column) {
    this.column = column;
  }
}