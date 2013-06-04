package sk.seges.acris.pap.security.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.acris.pap.security.SecurityProcessor;
import sk.seges.acris.pap.security.model.BasicPanel;
import sk.seges.acris.pap.security.model.PanelWithClientSession;
import sk.seges.acris.pap.security.model.PanelWithPermissions;
import sk.seges.acris.pap.security.model.PanelWithoutClientSession;
import sk.seges.acris.pap.security.model.SecuredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class SecurityProcessorTest extends AnnotationTest {

	@Test
	public void testSecuredPanel() {
		assertCompilationSuccessful(compileFiles(BasicPanel.class));
		assertOutput(getResourceFile(BasicPanel.class), getOutputFile(BasicPanel.class));
	}

	@Test
	public void testSecuredPanelWithClientSession() {
		assertCompilationSuccessful(compileFiles(PanelWithClientSession.class));
		assertOutput(getResourceFile(PanelWithClientSession.class), getOutputFile(PanelWithClientSession.class));
	}

	@Test
	public void testSecuredPanelWithoutClientSession() {
		assertCompilationSuccessful(compileFiles(PanelWithoutClientSession.class));
		assertOutput(getResourceFile(PanelWithoutClientSession.class), getOutputFile(PanelWithoutClientSession.class));
	}

	@Test
	public void testSecuredPanelWithPermissions() {
		assertCompilationSuccessful(compileFiles(PanelWithPermissions.class));
		assertOutput(getResourceFile(PanelWithPermissions.class), getOutputFile(PanelWithPermissions.class));
	}
	
	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
				new SecurityProcessor()
		};
	}
	
	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		SecuredType securedType = new SecuredType(inputClass, processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(securedType.getPackageName()) + "/" + securedType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}