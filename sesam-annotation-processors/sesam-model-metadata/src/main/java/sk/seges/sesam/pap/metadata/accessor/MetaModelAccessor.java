package sk.seges.sesam.pap.metadata.accessor;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.MetamodelMethodStrategy;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;

public class MetaModelAccessor extends AnnotationAccessor {

	private final Element element;
	private final MetaModel metaModelAnnotation;
	
	public MetaModelAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.element = element;
		this.metaModelAnnotation = this.getAnnotation(element, MetaModel.class);
	}
	
	@Override
	public boolean isValid() {
		return metaModelAnnotation != null;
	}
	
	public MetamodelMethodStrategy getMethodStrategy() {
		if (!isValid()) {
			return MetamodelMethodStrategy.GETTER_SETTER;
		}
		
		MetamodelMethodStrategy methodStrategy = metaModelAnnotation.methodStrategy();
		
		if (methodStrategy != null) {
			return methodStrategy;
		}
		
		return MetamodelMethodStrategy.GETTER_SETTER;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends ModelPropertyConverter>[] getPropertyConverterClasses() {
		if (!isValid()) {
			return (Class<? extends ModelPropertyConverter>[]) new Class<?>[0];
		}
		
		Class<? extends ModelPropertyConverter>[] beanPropertyConverter = metaModelAnnotation.beanPropertyConverter();
		
		if (beanPropertyConverter != null) {
			return beanPropertyConverter;
		}

		return (Class<? extends ModelPropertyConverter>[]) new Class<?>[0];
	}
	
	public List<ModelPropertyConverter> getPropertyConverters() {
		List<ModelPropertyConverter> converterInstances = new LinkedList<ModelPropertyConverter>();

		for (Class<? extends ModelPropertyConverter> converter : getPropertyConverterClasses()) {
			try {
				converterInstances.add(converter.getConstructor().newInstance());
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to instantiate " + converter.getName() + " using default constructor. Converter will be skipped!", element);
			}
		}

		return converterInstances;
	}
}