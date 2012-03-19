package sk.seges.sesam.core.test.selenium.junit.runner;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;

public class SeleniumTestRunner extends BlockJUnit4ClassRunner {

	public SeleniumTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void validateTestMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(SeleniumTest.class, false, errors);
	}

	protected List<FrameworkMethod> add(List<FrameworkMethod> list, FrameworkMethod method) {
		for (FrameworkMethod fm: list) {
			if (fm.getName().equals(method.getName())) {
				return list;
			}
		}
		
		list.add(method);
		return list;
	}
	
	protected List<FrameworkMethod> addAll(List<FrameworkMethod> list, List<FrameworkMethod> methods) {
		for (FrameworkMethod method: methods) {
			add(list, method);
		}
		
		return list;
	}
	
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> annotatedMethods = new ArrayList<FrameworkMethod>();
		addAll(annotatedMethods, getTestClass().getAnnotatedMethods(SeleniumTest.class));		
		return annotatedMethods;
	}
}