package sk.seges.sesam.pap.model.provider;

import java.util.Set;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;

public class ClasspathConfigurationProvider extends RoundEnvConfigurationProvider {

	private ClassPathTypes classpathUtils;

	public ClasspathConfigurationProvider(ClassPathTypes classpathUtils, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(envContext);
		this.classpathUtils = classpathUtils;
	}

	protected Set<? extends Element> getConfigurationElements() {
		return classpathUtils.getElementsAnnotatedWith(TransferObjectMapping.class, envContext.getRoundEnv());
	}
}