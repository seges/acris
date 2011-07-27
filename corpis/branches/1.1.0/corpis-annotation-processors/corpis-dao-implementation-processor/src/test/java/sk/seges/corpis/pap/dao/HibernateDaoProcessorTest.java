package sk.seges.corpis.pap.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.HibernateDaoProcessor;
import sk.seges.corpis.shared.model.mock.api.MockEntity;
import sk.seges.corpis.shared.model.mock.jpa.JpaMockEntity;
import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class HibernateDaoProcessorTest extends AnnotationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntity.class));
		
		List<File> files = new ArrayList<File>();
		File file = getInterfaceOutputFile(MockEntity.class);
		files.add(file);
		
		addCollection(files, JpaMockEntity.class);
		
		assertCompilationSuccessful(compileFiles(files));
		assertOutput(getResourceFile(JpaMockEntity.class), getOutputFile(JpaMockEntity.class));
	}

	private File getInterfaceOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = DaoApiProcessor.getOutputClass(inputClass, new DefaultPackageValidatorProvider());
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = HibernateDaoProcessor.getOutputClass(inputClass, new DefaultPackageValidatorProvider());
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor(), new HibernateDaoProcessor() };
	}
}