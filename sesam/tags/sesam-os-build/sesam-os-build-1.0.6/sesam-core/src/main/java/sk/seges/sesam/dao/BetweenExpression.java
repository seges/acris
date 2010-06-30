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

	public void setLoValue(T loValue) {
		setValue(loValue);
	}
	
	public void setHiValue(T hiValue) {
		this.hiValue = hiValue;
	}
	
	public T getLoValue() {
		return getValue();
	}
	
	public T getHiValue() {
		return hiValue;
	}
}
