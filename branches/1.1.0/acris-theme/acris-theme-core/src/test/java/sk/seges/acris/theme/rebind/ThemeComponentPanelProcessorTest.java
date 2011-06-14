package sk.seges.acris.theme.rebind;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.theme.component.TestComponent;
import sk.seges.sesam.core.pap.AnnotationTest;

public class ThemeComponentPanelProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(TestComponent.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { 
				new ThemeComponentPanelProcessor()
		};
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