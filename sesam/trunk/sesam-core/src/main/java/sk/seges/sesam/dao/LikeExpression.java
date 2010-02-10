/**
 * 
 */
package sk.seges.sesam.dao;

import java.io.Serializable;

/**
 * @author eldzi
 */
public class LikeExpression<T extends Comparable<? extends Serializable>> extends SimpleExpression<T> {
	private static final String LIKE = "like";
	private static final long serialVersionUID = -8623109501033052L;

	private MatchMode mode = MatchMode.ANYWHERE;
	private boolean caseSensitive = true;
	
	public LikeExpression() {
		operation = LIKE;
	}

	public LikeExpression(String property) {
		super(property, LIKE);
	}
	
	public LikeExpression(String property, T value) {
		super(property, value, LIKE);
	}

	public LikeExpression(String property, boolean caseSensitive) {
		super(property, LIKE);
		this.caseSensitive = caseSensitive;
	}
	
	public LikeExpression(String property, MatchMode mode) {
		super(property, LIKE);
		this.mode = mode;
	}

	public LikeExpression(String property, MatchMode mode, boolean caseSensitive) {
		super(property, LIKE);
		this.mode = mode;
		this.caseSensitive = caseSensitive;
	}

	public LikeExpression(String property, T value, MatchMode mode) {
		super(property, value, LIKE);
		this.mode = mode;
	}
	
	public LikeExpression(String property, T value, MatchMode mode, boolean caseSensitive) {
		super(property, value, LIKE);
		this.mode = mode;
		this.caseSensitive = caseSensitive;
	}
	
	public MatchMode getMode() {
		return mode;
	}
	
	public void setMode(MatchMode mode) {
		this.mode = mode;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	public static enum MatchMode {
		EXACT, START, END, ANYWHERE
	}
}
