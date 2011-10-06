package sk.seges.acris.theme.rebind;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.theme.component.StyledCheckBox;
import sk.seges.acris.theme.component.StyledTestWidget;
import sk.seges.acris.theme.pap.ThemeComponentPanelProcessor;
import sk.seges.acris.theme.pap.ThemeComponentProcessor;
import sk.seges.acris.theme.pap.model.ThemeComponentType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class ThemeComponentPanelProcessorTest extends AnnotationTest {

	//@Test
	public void basicTest() {
		assertCompilationSuccessful(compileFiles(StyledTestWidget.class));
		assertOutput(getResourceFile(StyledTestWidget.class), getOutputFile(StyledTestWidget.class));
	}

	@Test
	public void checkBoxTest() {
		assertCompilationSuccessful(compileFiles(StyledCheckBox.class));
//		assertOutput(getResourceFile(StyledTestWidget.class), getOutputFile(StyledTestWidget.class));
	}

	private File getOutputFile(Class<?> clazz) {
		MutableDeclaredType componentType = toMutable(clazz).addClassSufix(ThemeComponentType.SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(componentType.getPackageName()) + "/" + componentType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { 
				new ThemeComponentProcessor(),
				new ThemeComponentPanelProcessor()
		};
	}	
}