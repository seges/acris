package sk.seges.sesam.dao;

import java.io.Serializable;

import sk.seges.sesam.dao.LikeExpression.MatchMode;

/**
 * @author eldzi
 */
public class Filter {
	public static final String NE = "ne";
	public static final String LE = "le";
	public static final String ILIKE = "ilike";
	public static final String GT = "gt";
	public static final String GE = "ge";
	public static final String EQ = "eq";
	public static final String BETWEEN = "between";
	public static final String LT = "lt";
	public static final String LIKE = "like";

	public static Disjunction disjunction() {
		return new Disjunction();
	}
	
	public static Conjunction conjunction() {
		return new Conjunction();
	}
	
	public static <T extends Comparable<? extends Serializable>> BetweenExpression<T> between(String property) {
		return new BetweenExpression<T>(property, BETWEEN);
	}
	
	public static <T extends Comparable<? extends Serializable>> BetweenExpression<T> between(String property, T low, T high) {
		return new BetweenExpression<T>(property, BETWEEN).setLoValue(low).setHiValue(high);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> eq(String property) {
		return new SimpleExpression<T>(property, EQ);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> eq(String property, T value) {
		return new SimpleExpression<T>(property, EQ).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ge(String property) {
		return new SimpleExpression<T>(property, GE);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ge(String property, T value) {
		return new SimpleExpression<T>(property, GE).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> gt(String property) {
		return new SimpleExpression<T>(property, GT);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> gt(String property, T value) {
		return new SimpleExpression<T>(property, GT).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> ilike(String property) {
		return new LikeExpression<T>(property, false);
	}
	
	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> ilike(String property, T value) {
		return (LikeExpression<T>) new LikeExpression<T>(property, false).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> ilike(String property, MatchMode matchMode, T value) {
		return (LikeExpression<T>) new LikeExpression<T>(property, false).setMode(matchMode).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> le(String property) {
		return new SimpleExpression<T>(property, LE);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> le(String property, T value) {
		return new SimpleExpression<T>(property, LE).setValue(value);
	}

	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> like(String property) {
		return new LikeExpression<T>(property);
	}

	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> like(String property, T value) {
		return (LikeExpression<T>) new LikeExpression<T>(property).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> lt(String property) {
		return new SimpleExpression<T>(property, LT);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> lt(String property, T value) {
		return new SimpleExpression<T>(property, LT).setValue(value);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ne(String property) {
		return new SimpleExpression<T>(property, NE);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ne(String property, T value) {
		return new SimpleExpression<T>(property, NE).setValue(value);
	}
	
	public static NotExpression not(Criterion criterion) {
		return new NotExpression(criterion);
	}
	
	public static NullExpression isNull(String property) {
		return new NullExpression(property);
	}
	
	public static NotNullExpression isNotNull(String property) {
		return new NotNullExpression(property);
	}
}