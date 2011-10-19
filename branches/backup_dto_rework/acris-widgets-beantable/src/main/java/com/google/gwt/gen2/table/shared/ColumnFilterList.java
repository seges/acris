package com.google.gwt.gen2.table.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An ordered list of filter info where each entry tells us how to filter a
 * single column.
 */
public class ColumnFilterList implements IsSerializable,
    Iterable<ColumnFilterInfo<?>> {
  /**
   * A List used to manage the insertion/removal of {@link ColumnFilterInfo}.
   */
  private List<ColumnFilterInfo<?>> infos = new ArrayList<ColumnFilterInfo<?>>();

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ColumnFilterList) {
      return equals((ColumnFilterList) obj);
    }
    return false;
  }

  /**
   * Check if this object is equal to another.
   * 
   * @param csl the other object
   * @return true if objects are equal
   */
  public boolean equals(ColumnFilterList cfl) {
    // Object is null
    if (cfl == null) {
      return false;
    }

    // Check the size of the lists
    int size = size();
    if (size != cfl.size()) {
      return false;
    }

    // Compare the entries
    for (int i = 0; i < size; i++) {
      if (!infos.get(i).equals(cfl.infos.get(i))) {
        return false;
      }
    }

    // Everything is equal
    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public Iterator<ColumnFilterInfo<?>> iterator() {
    return new ImmutableIterator<ColumnFilterInfo<?>>(infos.iterator());
  }

  /**
   * Returns the number of {@link ColumnFilterInfo} in the list.
   * 
   * @return the size of the list
   */
  public int size() {
    return infos.size();
  }

  /**
   * Add a {@link ColumnFilterInfo} to this list. If the column already
   * exists, it will be removed from its current position and placed at the
   * start of the list, becoming the primary sort info.
   * 
   * This add method inserts an entry at the beginning of the list. It does
   * not append the entry to the end of the list.
   * 
   * @param column the column index
   * @param filter is the filter
   */
  public void add(ColumnFilterInfo<?> info) {
    // Remove sort info for duplicate columns
    for (int i = 0; i < infos.size(); i++) {
      ColumnFilterInfo<?> curInfo = infos.get(i);
      if (curInfo.getColumn() == info.getColumn()) {
        infos.remove(i);
        i--;
      }
    }

    // Insert the new sort info
    infos.add(0, info);
  }

  /**
   * @return a duplicate of this list
   */
  ColumnFilterList copy() {
    ColumnFilterList copy = new ColumnFilterList();
    for (ColumnFilterInfo<?> info : this) {
      copy.infos.add(info.copy());
    }
    return copy;
  }

}