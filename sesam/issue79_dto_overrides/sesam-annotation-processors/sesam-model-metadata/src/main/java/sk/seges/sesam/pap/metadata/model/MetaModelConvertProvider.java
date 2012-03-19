package sk.seges.sesam.pap.metadata.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;
import sk.seges.sesam.pap.metadata.accessor.MetaModelAccessor;

public class MetaModelConvertProvider {

	private final MetaModelAccessor metaModelAccessor;
	
	public MetaModelConvertProvider(Element element, MutableProcessingEnvironment processingEnv) {
		this.metaModelAccessor = new MetaModelAccessor(element, processingEnv);
	}
	
	public Set<ModelPropertyConverter> getSamePolicyNotProcessedConverters(MetaModelContext context, Set<ModelPropertyConverter> processedConverters) {
		Set<ModelPropertyConverter> result = new HashSet<ModelPropertyConverter>();

		if (context.getProperty() == null) {
			result.add(context.getConverter());
		} else {
			String convertedProperty = context.getFieldName();
			String property = context.getProperty();
			
			for (ModelPropertyConverter beanPropertyConverter : getConverters()) {
				if (!(processedConverters.contains(beanPropertyConverter))) {
					if (convertedProperty.equals(beanPropertyConverter.getConvertedPropertyName(property))) {
						result.add(beanPropertyConverter);
					}
				}
			}
		}
	
		return result;
	}

	public List<ModelPropertyConverter> getConverters() {
		List<ModelPropertyConverter> converterInstances = metaModelAccessor.getPropertyConverters();
	
		if (converterInstances.size() == 0) {
			converterInstances.add(new PojoPropertyConverter());
		}
		
		Collections.sort(converterInstances, new Comparator<ModelPropertyConverter>() {

			@Override
			public int compare(ModelPropertyConverter o1, ModelPropertyConverter o2) {
				return o1.getClass().getName().compareTo(o2.getClass().getName());
			}

		});

		return converterInstances;
	}
}