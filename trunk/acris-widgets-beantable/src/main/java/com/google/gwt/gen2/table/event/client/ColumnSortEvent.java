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

package com.google.gwt.gen2.table.event.client;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.gen2.table.shared.ColumnSortList;

/**
 * Logical event fired when a column is sorted.
 */
public class ColumnSortEvent extends GwtEvent<ColumnSortHandler> {
	/**
	 * Event Key for {@link ColumnSortEvent}.
	 */
	public static final Type<ColumnSortHandler> TYPE = new Type<ColumnSortHandler>();

	/**
	 * Information about the column sorting.
	 */
	private ColumnSortList sortList;

	/**
	 * Construct a new {@link ColumnSortEvent}.
	 * 
	 * @param sortList
	 *            information about the sort order
	 */
	public ColumnSortEvent(ColumnSortList sortList) {
		this.sortList = sortList;
	}

	/**
	 * @return information about the sort order of columns
	 */
	public ColumnSortList getColumnSortList() {
		return sortList;
	}

	@Override
	protected void dispatch(ColumnSortHandler handler) {
		handler.onColumnSorted(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ColumnSortHandler> getAssociatedType() {
		return TYPE;
	}
}
