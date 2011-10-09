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

import com.google.gwt.gen2.table.shared.Request;
import com.google.gwt.gen2.table.shared.TreeRequest;
import com.google.gwt.gen2.table.shared.TreeTableItem;

/**
 * A tree table version of the {@link TableModel}.
 * 
 * @param <R> the data type of the row values
 */
public abstract class TreeTableModel<R extends TreeTableItem> extends TableModel<R> {
  public void requestRows(Request request, Callback<R> callback) {
    requestTreeItems((TreeRequest)request, callback);
  }

  public abstract void requestTreeItems(TreeRequest request,
      Callback<R> callback);
}