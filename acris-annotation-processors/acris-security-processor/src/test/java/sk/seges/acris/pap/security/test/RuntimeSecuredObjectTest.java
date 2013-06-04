package sk.seges.acris.pap.security.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.pap.security.RuntimeSecuredObjectProcessor;
import sk.seges.acris.pap.security.model.BasicPanel;
import sk.seges.acris.pap.security.model.PanelWithClientSession;
import sk.seges.acris.pap.security.model.PanelWithPermissions;
import sk.seges.acris.pap.security.model.PanelWithoutClientSession;
import sk.seges.acris.pap.security.model.RuntimeSecuredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class RuntimeSecuredObjectTest extends AnnotationTest  {

	@Test
	public void testSecuredPanel() {
		assertCompilationSuccessful(compileFiles(BasicPanel.class));
		assertOutput(getResourceFile(getDirectorySuffix(), BasicPanel.class), getOutputFile(BasicPanel.class));
	}

	protected String getDirectorySuffix() {
		return "runtime";
	}
	
	@Test
	public void testSecuredPanelWithClientSession() {
		assertCompilationSuccessful(compileFiles(PanelWithClientSession.class));
		assertOutput(getResourceFile(getDirectorySuffix(), PanelWithClientSession.class), getOutputFile(PanelWithClientSession.class));
	}

	@Test
	public void testSecuredPanelWithoutClientSession() {
		assertCompilationSuccessful(compileFiles(PanelWithoutClientSession.class));
		assertOutput(getResourceFile(getDirectorySuffix(), PanelWithoutClientSession.class), getOutputFile(PanelWithoutClientSession.class));
	}

	@Test
	public void testSecuredPanelWithPermissions() {
		assertCompilationSuccessful(compileFiles(PanelWithPermissions.class));
		assertOutput(getResourceFile(getDirectorySuffix(), PanelWithPermissions.class), getOutputFile(PanelWithPermissions.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new RuntimeSecuredObjectProcessor()
		};
	}
	
	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		RuntimeSecuredType runtimeSecuredType = new RuntimeSecuredType(inputClass, processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(runtimeSecuredType.getPackageName()) + "/" + runtimeSecuredType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}