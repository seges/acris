package sk.seges.corpis.pap.service;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.InputClass.OutputClass;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({ServiceProcessor.CONFIG_FILE_LOCATION})
public class ServiceProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/service.properties";

	static final String DAO_API_CLASS_PREFIX = "Abstract";

	@Override
	protected OutputClass[] getTargetClassNames(InputClass arg0) {
		return null;
	}
}