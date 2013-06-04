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
	
	public static final String ID = "id";
	
	/*
	 * Returns a primary key for a domain object
	 */
	T getId();
	
}