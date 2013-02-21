package sk.seges.sesam.pap.core;

import java.io.File;
import java.util.ArrayList;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;

public abstract class DtoTestCase extends AnnotationTest {

	protected File getOutputFile(Class<?> clazz) {
		MutableDeclaredType inputClass = toMutable(clazz);
		
		TransferObjectProcessingEnvironment tope = new TransferObjectProcessingEnvironment(processingEnv, roundEnv, new ConfigurationCache(), getProcessors()[0].getClass(), new ArrayList<MutableDeclaredType>());
		DtoDeclaredType dto = new ConfigurationTypeElement(processingEnv.getElementUtils().getTypeElement(inputClass.getCanonicalName()), tope.getEnvironmentContext(), null).getDto();

		return new File(OUTPUT_DIRECTORY, toPath(dto.getPackageName()) + "/" + dto.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

}
