package sk.seges.sesam.pap.model.provider;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;

public class ClasspathConfigurationProvider extends RoundEnvConfigurationProvider {

	private ClassPathTypes classpathUtils;

	public ClasspathConfigurationProvider(ClassPathTypes classpathUtils, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.classpathUtils = classpathUtils;
	}

	protected Set<? extends Element> getConfigurationElements() {
		return classpathUtils.getElementsAnnotatedWith(TransferObjectMapping.class, roundEnv);
	}
}