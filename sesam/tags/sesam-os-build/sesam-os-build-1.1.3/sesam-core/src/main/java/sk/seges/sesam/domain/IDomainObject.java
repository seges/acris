/**
 * 
 */
package sk.seges.sesam.domain;

import java.io.Serializable;

/**
 * Marker interface for all domain objects forcing equals and hashCode
 * implementation and making object automatically serializable.
 * 
 * @author eldzi
 */
public interface IDomainObject<T> extends Serializable {
	/*
	 * Returns a primary key for a domain object
	 */
	T getId();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	int hashCode();
}
