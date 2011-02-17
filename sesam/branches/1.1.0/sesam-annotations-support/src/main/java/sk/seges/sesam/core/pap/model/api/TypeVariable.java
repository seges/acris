package sk.seges.sesam.core.pap.model.api;

import java.lang.reflect.Type;

public interface TypeVariable {
		
//		Type getLowerBound(); Lower bound is not supported
		
		Type getUpperBound();
	}