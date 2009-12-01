package sk.seges.sesam.dao;

import java.io.Serializable;

public interface Criterion extends Serializable {
	/** Hibernate's Restrictions method name counterpart. */
	String getOperation();
}
