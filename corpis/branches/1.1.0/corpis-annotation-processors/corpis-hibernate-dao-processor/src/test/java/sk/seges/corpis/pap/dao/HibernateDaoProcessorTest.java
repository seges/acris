package sk.seges.corpis.pap.dao;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.HibernateDaoProcessor;
import sk.seges.corpis.shared.model.mock.api.MockEntity;
import sk.seges.corpis.shared.model.mock.jpa.JpaMockEntity;
import sk.seges.sesam.core.pap.AnnotationTest;
import sk.seges.sesam.core.pap.model.InputClass.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;

public class HibernateDaoProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileTestCase(MockEntity.class, JpaMockEntity.class));
		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
		assertOutput(getResourceFile(JpaMockEntity.class), getOutputFile(JpaMockEntity.class));
	}
 
	private String toPath(Package packageName) {
		return toPath(packageName.getName());
	}

	private String toPath(String packageName) {
		return packageName.replace(".", "/");
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = DaoApiProcessor.getOutputClass(inputClass, new DefaultPackageValidatorProvider());
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	private File getResourceFile(Class<?> clazz) {
		return new File(getClass().getResource("/" + toPath(clazz.getPackage()) + "/" + 
				clazz.getSimpleName() + ".output").getFile());
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor(), new HibernateDaoProcessor() };
	}
	
	private static final String OUTPUT_DIRECTORY = "target/generated-test";
	
	protected File ensureOutputDirectory() {
		File file = new File(OUTPUT_DIRECTORY);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return file;
	}
	
	@Override
	protected String[] getCompilerOptions() {
		return CompilerOptions.GENERATED_SOURCES_DIRECTORY.getOption(ensureOutputDirectory().getAbsolutePath());
	}
}