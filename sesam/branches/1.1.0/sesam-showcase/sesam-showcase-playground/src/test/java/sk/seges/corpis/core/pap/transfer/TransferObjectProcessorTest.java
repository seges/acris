package sk.seges.corpis.core.pap.transfer;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.dto.ParametrizedEntity;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class TransferObjectProcessorTest extends AnnotationTest {

	@Test
	public void testTransferObject() {
//		assertCompilationSuccessful(compileFiles(MockEntity.class));
		assertCompilationSuccessful(compileFiles(ParametrizedEntity.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new TransferObjectProcessor()
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