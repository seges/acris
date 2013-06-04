package sk.seges.acris.pap.bean.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.pap.bean.BeanWrapperProcessor;
import sk.seges.acris.pap.bean.model.BeanWrapperImplementationType;
import sk.seges.acris.pap.bean.model.DummyBean;
import sk.seges.acris.pap.bean.model.SuperBean;
import sk.seges.acris.pap.bean.model.TypedBean;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class BeanWrapperProcessorTest extends AnnotationTest {

	@Test
	public void testDummyBean() {
		assertCompilationSuccessful(compileFiles(DummyBean.class));
		assertOutput(getResourceFile(DummyBean.class), getOutputFile(DummyBean.class));
	}

	@Test
	public void testDummyBeanInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, DummyBean.class));
		assertOutput(getResourceFile(DummyBean.class), getOutputFile(DummyBean.class));
	}

	@Test
	public void testTypedBean() {
		assertCompilationSuccessful(compileFiles(TypedBean.class));
		assertOutput(getResourceFile(TypedBean.class), getOutputFile(TypedBean.class));
	}

	@Test
	public void testTypedBeanInEclipse() {
		assertCompilationSuccessful(compileFiles(Compiler.ECLIPSE, TypedBean.class));
		assertOutput(getResourceFile(TypedBean.class), getOutputFile(TypedBean.class));
	}

	@Test
	public void testSuperBean() {
		assertCompilationSuccessful(compileFiles(SuperBean.class));
		assertOutput(getResourceFile(SuperBean.class), getOutputFile(SuperBean.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new BeanWrapperProcessor()
		};
	}
	
	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		BeanWrapperImplementationType beanWrapperImplementationType = new BeanWrapperImplementationType(inputClass, processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(beanWrapperImplementationType.getPackageName()) + "/" + beanWrapperImplementationType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

}