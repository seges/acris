package sk.seges.sesam.dao;

import java.io.Serializable;

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
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> eq(String property) {
		return new SimpleExpression<T>(property, EQ);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ge(String property) {
		return new SimpleExpression<T>(property, GE);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> gt(String property) {
		return new SimpleExpression<T>(property, GT);
	}
	
	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> ilike(String property) {
		return new LikeExpression<T>(property, false);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> le(String property) {
		return new SimpleExpression<T>(property, LE);
	}

	public static <T extends Comparable<? extends Serializable>> LikeExpression<T> like(String property) {
		return new LikeExpression<T>(property);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> lt(String property) {
		return new SimpleExpression<T>(property, LT);
	}
	
	public static <T extends Comparable<? extends Serializable>> SimpleExpression<T> ne(String property) {
		return new SimpleExpression<T>(property, NE);
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