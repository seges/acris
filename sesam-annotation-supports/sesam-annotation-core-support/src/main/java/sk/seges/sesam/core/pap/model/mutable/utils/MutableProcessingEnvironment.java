package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;

public class MutableProcessingEnvironment implements ProcessingEnvironment {

	private ProcessingEnvironment processingEnvironment;
	private MutableElements elements;
	private MutableTypes types;
	
	public MutableProcessingEnvironment(ProcessingEnvironment processingEnvironment) {
		this.processingEnvironment = processingEnvironment;
		this.elements = new MutableElements(processingEnvironment.getElementUtils());
		this.types = new MutableTypes(this, elements, processingEnvironment.getTypeUtils());
	}

	@Override
	public Map<String, String> getOptions() {
		return this.processingEnvironment.getOptions();
	}

	@Override
	public Messager getMessager() {
		return this.processingEnvironment.getMessager();
	}

	@Override
	public Filer getFiler() {
		return this.processingEnvironment.getFiler();
	}

	@Override
	public MutableElements getElementUtils() {
		return elements;
	}

	@Override
	public MutableTypes getTypeUtils() {
		return types;
	}

	@Override
	public SourceVersion getSourceVersion() {
		return this.processingEnvironment.getSourceVersion();
	}

	@Override
	public Locale getLocale() {
		return this.processingEnvironment.getLocale();
	}
}