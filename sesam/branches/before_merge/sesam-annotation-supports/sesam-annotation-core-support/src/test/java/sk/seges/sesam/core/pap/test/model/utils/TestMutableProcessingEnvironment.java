package sk.seges.sesam.core.pap.test.model.utils;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class TestMutableProcessingEnvironment extends MutableProcessingEnvironment {

	public TestMutableProcessingEnvironment() {
		super(new TestProcessingEnvironment());
	}
}
