package com.google.gwt.gen2.table.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.gen2.table.shared.AbstractTreeTableItem;
import com.google.gwt.gen2.table.shared.TreeTableItem;

public class TreeItem<T extends TreeTableItem> {
	private List<TreeItem<T>> children = null;
	private final T treeTableItem;
	
	public TreeItem(T treeTableItem) {
		this.treeTableItem = treeTableItem;
	}
	
	public void addChild(TreeItem<T> child) {
	  if ( treeTableItem instanceof AbstractTreeTableItem ) {
	    ((AbstractTreeTableItem) treeTableItem).setChildren(true);
	    ((AbstractTreeTableItem)child.getTreeTableItem()).setParent(treeTableItem);
	  }
	  if ( children == null ) {
			children = new ArrayList<TreeItem<T>>();
		}
		children.add(child);
	}
	
	public boolean hasChildren() {
		return children != null;
	}
	
	public List<TreeItem<T>> getChildren() {
		return children;
	}

	public T getTreeTableItem() {
		return treeTableItem;
	}
}