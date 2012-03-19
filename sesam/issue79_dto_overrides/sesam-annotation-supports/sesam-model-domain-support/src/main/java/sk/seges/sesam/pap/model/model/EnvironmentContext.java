package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public class EnvironmentContext<T extends ProcessingEnvironment> {

	private final T processingEnv;
	private final RoundEnvironment roundEnv;
	private final ConfigurationEnvironment configurationEnv;

	public EnvironmentContext(T processingEnv, RoundEnvironment roundEnv, ConfigurationEnvironment configurationEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.configurationEnv = configurationEnv;
	}
	
	public T getProcessingEnv() {
		return processingEnv;
	}
	
	public RoundEnvironment getRoundEnv() {
		return roundEnv;
	}
	
	public ConfigurationEnvironment getConfigurationEnv() {
		return configurationEnv;
	}
}