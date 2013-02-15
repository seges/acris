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

package com.google.gwt.gen2.table.shared;

import java.io.Serializable;

/**
 * Wrapper for displaying arbitrary objects in tree table
 * 
 * @param <T>
 *            the type of the wrapped object
 */
public class TreeTableItemWrapper<T extends Serializable> implements TreeTableItem {
	private boolean children = false;
	private TreeTableItem parent = null;
	private int row;

	private String id, displayName;
	private T userObject;

	/**
	 * Default constructor for RPC use
	 */
	public TreeTableItemWrapper() {
	}

	/**
	 * @param userObject
	 *            the object to be wrapped
	 */
	public TreeTableItemWrapper(T userObject) {
		this.userObject = userObject;
	}

	public String getId() {
		return id;
	}

	public TreeTableItem getParent() {
		return parent;
	}

	public T getUserObject() {
		return userObject;
	}

	public boolean hasChildren() {
		return children;
	}

	public void setChildren(boolean children) {
		this.children = children;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParent(TreeTableItem parent) {
		this.parent = parent;
	}

	public void setUserObject(T userObject) {
		this.userObject = userObject;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}