package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

public class MutableProcessingEnvironment implements ProcessingEnvironment {

	private ProcessingEnvironment processingEnvironment;
	private MutableElements elements;
	private MutableTypes types;
	private PrintSupport printSupport;
	
	private List<MutableDeclaredType> usedTypes;
	
	public MutableProcessingEnvironment(ProcessingEnvironment processingEnvironment, Class<?> clazz, List<MutableDeclaredType> usedTypes) {
		this.processingEnvironment = processingEnvironment;
		this.printSupport = getPrintSupport(clazz);
		this.usedTypes = usedTypes;
		this.elements = new MutableElements(this, processingEnvironment.getElementUtils());
		this.types = new MutableTypes(this, elements, processingEnvironment.getTypeUtils());
	}

	public List<MutableDeclaredType> getUsedTypes() {
		return usedTypes;
	}
	
	private PrintSupport getPrintSupport(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		
		PrintSupport printerSupport = clazz.getAnnotation(PrintSupport.class);
		if (printerSupport != null) {
			return printerSupport;
		}

		if (clazz.getSuperclass() != null) {
			return getPrintSupport(clazz.getSuperclass());
		}
		
		return null;
	}

	public PrintSupport getPrintSupport() {
		return printSupport;
	};
	
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