package sk.seges.sesam.core.pap.test.model.utils;

import java.util.ArrayList;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class TestMutableProcessingEnvironment extends MutableProcessingEnvironment {

	public TestMutableProcessingEnvironment(Class<?> clazz) {
		super(new TestProcessingEnvironment(), clazz, new ArrayList<MutableDeclaredType>());
	}
}
