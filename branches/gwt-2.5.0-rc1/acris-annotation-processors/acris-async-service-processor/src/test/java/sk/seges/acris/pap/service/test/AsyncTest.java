package sk.seges.acris.pap.service.test;

import java.io.File;

import sk.seges.acris.pap.service.model.AsyncRemoteServiceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

public abstract class AsyncTest extends AnnotationTest {

	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		RemoteServiceTypeElement remoteServiceTypeElement = new RemoteServiceTypeElement(processingEnv.getElementUtils().getTypeElement(inputClass.getCanonicalName()), processingEnv);
		AsyncRemoteServiceType asyncRemoteServiceType = new AsyncRemoteServiceType(remoteServiceTypeElement, processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(asyncRemoteServiceType.getPackageName()) + "/" + asyncRemoteServiceType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}