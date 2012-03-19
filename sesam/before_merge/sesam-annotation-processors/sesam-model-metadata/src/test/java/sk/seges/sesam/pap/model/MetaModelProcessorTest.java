package sk.seges.sesam.pap.model;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.metadata.MetaModelProcessor;
import sk.seges.sesam.pap.metadata.model.MetaModelTypeElement;
import sk.seges.sesam.shared.model.mock.MockBirdGetterInterface;
import sk.seges.sesam.shared.model.mock.MockBirdInterface;
import sk.seges.sesam.shared.model.mock.MockEntity;
import sk.seges.sesam.shared.model.mock.MockHierarchyEntityData;
import sk.seges.sesam.shared.model.mock.MockiestEntity;
import sk.seges.sesam.shared.model.mock.MultipleConverterEntity;

public class MetaModelProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntity() {
		assertCompilationSuccessful(compileFiles(MockEntity.class));
		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
	}

	@Test
	public void testMultipleConverters() {
		assertCompilationSuccessful(compileFiles(MultipleConverterEntity.class));
		assertOutput(getResourceFile(MultipleConverterEntity.class), getOutputFile(MultipleConverterEntity.class));
	}

	@Test
	public void testInheritance() {
		assertCompilationSuccessful(compileFiles(MockiestEntity.class));
		assertOutput(getResourceFile(MockiestEntity.class), getOutputFile(MockiestEntity.class));
	}

	@Test
	public void testMockHierarchyEntity() {
		assertCompilationSuccessful(compileFiles(MockHierarchyEntityData.class));
		assertOutput(getResourceFile(MockHierarchyEntityData.class), getOutputFile(MockHierarchyEntityData.class));
	}

	@Test
	public void testCreateMetaModelOnInterfaceWithPureStrategy() {
		assertCompilationSuccessful(compileFiles(MockBirdInterface.class));
		assertOutput(getResourceFile(MockBirdInterface.class), getOutputFile(MockBirdInterface.class));
	}

	@Test
	public void testCreateMetaModelOnInterfaceWithGetterSetterStrategy() {
		assertCompilationSuccessful(compileFiles(MockBirdGetterInterface.class));
		assertOutput(getResourceFile(MockBirdGetterInterface.class), getOutputFile(MockBirdGetterInterface.class));
	}
	
	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(MetaModelTypeElement.META_MODEL_SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new MetaModelProcessor()
		};
	}		
}