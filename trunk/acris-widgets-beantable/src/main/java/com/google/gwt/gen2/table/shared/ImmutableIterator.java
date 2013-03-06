package com.google.gwt.gen2.table.shared;

import java.util.Iterator;

/**
 * Wrap an {@link Iterator} in an immutable iterator.
 */
public class ImmutableIterator<E> implements Iterator<E> {
  private Iterator<E> iterator;

  public ImmutableIterator(Iterator<E> iterator) {
    this.iterator = iterator;
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public E next() {
    return iterator.next();
  }

  public void remove() {
    throw (new UnsupportedOperationException());
  }
}