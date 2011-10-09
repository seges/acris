/**
 * 
 */
package sk.seges.sesam.dao;

import java.io.Serializable;

/**
 * @author eldzi
 *
 */
public class BetweenExpression<T extends Comparable<? extends Serializable>> extends SimpleExpression<T> {
	private static final long serialVersionUID = -8218796834016497508L;

	private T hiValue;
	
	public BetweenExpression() {}
	
	public BetweenExpression(String property, String operation) {
		super(property, operation);
	}
	
	public BetweenExpression(String property, String operation, T loValue, T hiValue) {
		super(property, loValue, operation);
		this.hiValue = hiValue;
	}

	public BetweenExpression<T> setLoValue(T loValue) {
		setValue(loValue);
		return this;
	}
	
	public BetweenExpression<T> setHiValue(T hiValue) {
		this.hiValue = hiValue;
		return this;
	}
	
	public T getLoValue() {
		return getValue();
	}
	
	public T getHiValue() {
		return hiValue;
	}
}
