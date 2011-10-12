package com.google.gwt.gen2.table.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An ordered list of sorting info where each entry tells us how to sort a
 * single column. The first entry is the primary sort order, the second entry
 * is the first tie-breaker, and so on.
 */
public class ColumnSortList implements IsSerializable,
    Iterable<ColumnSortInfo> {
  /**
   * A List used to manage the insertion/removal of {@link ColumnSortInfo}.
   */
  private List<ColumnSortInfo> infos = new ArrayList<ColumnSortInfo>();

  /**
   * Add a {@link ColumnSortInfo} to this list. If the column already exists,
   * it will be removed from its current position and placed at the start of
   * the list, becoming the primary sort info.
   * 
   * This add method inserts an entry at the beginning of the list. It does
   * not append the entry to the end of the list.
   * 
   * @param sortInfo the {@link ColumnSortInfo} to add
   */
  public void add(ColumnSortInfo sortInfo) {
    add(0, sortInfo);
  }

  /**
   * Inserts the specified {@link ColumnSortInfo} at the specified position in
   * this list. If the column already exists in the sort info, the index will
   * be adjusted to account for any removed entries.
   * 
   * @param sortInfo the {@link ColumnSortInfo} to add
   */
  public void add(int index, ColumnSortInfo sortInfo) {
    // Remove sort info for duplicate columns
    int column = sortInfo.getColumn();
    for (int i = 0; i < infos.size(); i++) {
      ColumnSortInfo curInfo = infos.get(i);
      if (curInfo.getColumn() == column) {
        infos.remove(i);
        i--;
        if (column < index) {
          index--;
        }
      }
    }

    // Insert the new sort info
    infos.add(index, sortInfo);
  }

  /**
   * Removes all of the elements from this list.
   */
  public void clear() {
    infos.clear();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ColumnSortList) {
      return equals((ColumnSortList) obj);
    }
    return false;
  }

  /**
   * Check if this object is equal to another.
   * 
   * @param csl the other object
   * @return true if objects are equal
   */
  public boolean equals(ColumnSortList csl) {
    // Object is null
    if (csl == null) {
      return false;
    }

    // Check the size of the lists
    int size = size();
    if (size != csl.size()) {
      return false;
    }

    // Compare the entries
    for (int i = 0; i < size; i++) {
      if (!infos.get(i).equals(csl.infos.get(i))) {
        return false;
      }
    }

    // Everything is equal
    return true;
  }

  /**
   * Get the primary (first) {@link ColumnSortInfo}'s column index.
   * 
   * @return the primary column or -1 if not sorted
   */
  public int getPrimaryColumn() {
    ColumnSortInfo primaryInfo = getPrimaryColumnSortInfo();
    if (primaryInfo == null) {
      return -1;
    }
    return primaryInfo.getColumn();
  }

  /**
   * Get the primary (first) {@link ColumnSortInfo}.
   * 
   * @return the primary column sort info
   */
  public ColumnSortInfo getPrimaryColumnSortInfo() {
    if (infos.size() > 0) {
      return infos.get(0);
    }
    return null;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Get the primary (first) {@link ColumnSortInfo}'s sort order.
   * 
   * @return true if ascending, false if descending
   */
  public boolean isPrimaryAscending() {
    ColumnSortInfo primaryInfo = getPrimaryColumnSortInfo();
    if (primaryInfo == null) {
      return true;
    }
    return primaryInfo.isAscending();
  }

  public Iterator<ColumnSortInfo> iterator() {
    return new ImmutableIterator<ColumnSortInfo>(infos.iterator());
  }

  /**
   * Remove a {@link ColumnSortInfo} from the list.
   * 
   * @param sortInfo the {@link ColumnSortInfo} to remove
   */
  public boolean remove(Object sortInfo) {
    return infos.remove(sortInfo);
  }

  /**
   * @return the number of {@link ColumnSortInfo} in the list
   */
  public int size() {
    return infos.size();
  }

  /**
   * @return a duplicate of this list
   */
  public ColumnSortList copy() {
    ColumnSortList copy = new ColumnSortList();
    for (ColumnSortInfo info : this) {
      copy.infos.add(new ColumnSortInfo(info.getColumn(), info.isAscending()));
    }
    return copy;
  }
}