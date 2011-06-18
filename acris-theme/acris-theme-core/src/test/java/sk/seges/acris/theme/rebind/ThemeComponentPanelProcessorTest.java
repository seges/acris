package sk.seges.acris.theme.rebind;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.theme.component.StyledTestWidget;
import sk.seges.acris.theme.pap.ThemeComponentPanelProcessor;
import sk.seges.acris.theme.pap.ThemeComponentProcessor;
import sk.seges.sesam.core.pap.AnnotationTest;
import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class ThemeComponentPanelProcessorTest extends AnnotationTest {

	@Test
	public void basicTest() {
		assertCompilationSuccessful(compileFiles(StyledTestWidget.class));
		assertOutput(getResourceFile(StyledTestWidget.class), getOutputFile(StyledTestWidget.class));
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = ThemeComponentPanelProcessor.getOutputClass(inputClass);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { 
				new ThemeComponentProcessor(),
				new ThemeComponentPanelProcessor()
		};
	}	
}