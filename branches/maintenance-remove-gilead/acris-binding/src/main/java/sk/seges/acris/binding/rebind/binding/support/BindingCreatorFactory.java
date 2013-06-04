package sk.seges.acris.binding.rebind.binding.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.binding.client.providers.support.generic.IBindingBeanAdapterProvider;
import sk.seges.acris.binding.rebind.GeneratorException;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;
import sk.seges.acris.core.rebind.RebindUtils;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.SourceWriter;

public class BindingCreatorFactory {

	private static final List<AbstractBindingCreator<? extends Annotation>> bindingCreators = new ArrayList<AbstractBindingCreator<? extends Annotation>>();

	static {
		registerBindingCreator(new OneToOneBindingCreator());
		registerBindingCreator(new OneToManyBeansBindingCreator());
		registerBindingCreator(new ManyToManyBindingCreator());
	}

	private static Map<String, BindingComponent> supportedTypes;

	public static BindingComponent getBindingComponent(JClassType classType) {
		return supportedTypes.get(classType.getQualifiedSourceName());
	}

	public static AbstractBindingCreator<? extends Annotation> getBindingCreator(JField field) throws UnableToCompleteException {

		if (field == null) {
			return null;
		}

		for (AbstractBindingCreator<? extends Annotation> abstractBindingCreator : bindingCreators) {
			if (abstractBindingCreator.isSupported(field)) {
				return abstractBindingCreator;
			}
		}

		return null;
	}

	public static void setGeneratorContext(GeneratorContext context, SourceWriter sourceWriter, TreeLogger logger) {
		for (AbstractBindingCreator<? extends Annotation> abstractBindingCreator : bindingCreators) {
			abstractBindingCreator.setGeneratorContext(context, sourceWriter, logger);
		}
	}

	public static void setBindingContext(JClassType parentBeanClassType, String packageName, BindingNamingStrategy namingStrategy) {
		for (AbstractBindingCreator<? extends Annotation> abstractBindingCreator : bindingCreators) {
			abstractBindingCreator.setBindingContext(parentBeanClassType, packageName, namingStrategy);
		}
	}

	public static void setBindingHolder(String bindingHolderField) {
		for (AbstractBindingCreator<? extends Annotation> abstractBindingCreator : bindingCreators) {
			abstractBindingCreator.setBindingHolder(bindingHolderField);
		}
	}

	static void registerBindingCreator(AbstractBindingCreator<? extends Annotation> bindingCreator) {
		bindingCreators.add(bindingCreator);
	}

	public static <M extends Annotation> void fillSupportedTypes(TypeOracle typeOracle) throws GeneratorException {

		if (supportedTypes != null) {
			return;
		}

		supportedTypes = new HashMap<String, BindingComponent>();

		JClassType type = null;
		try {
			type = typeOracle.getType(IBindingBeanAdapterProvider.class.getName());
		} catch (NotFoundException e) {
			throw new GeneratorException("Cannot find class " + IBindingBeanAdapterProvider.class.getName(), e);
		}

		JClassType[] types = type.getSubtypes();

		List<Class<? extends Annotation>> supportedAnnotations = new ArrayList<Class<? extends Annotation>>();

		for (AbstractBindingCreator<? extends Annotation> bindingCreator : bindingCreators) {
			supportedAnnotations.add(bindingCreator.getSupportedClass());
		}

//		ClassFinder classFinder = new ClassFinder();
//		Vector<Class<?>> bindingAdapterProviders = classFinder.findSubclasses(IBindingBeanAdapterProvider.class.getName());

//		JClassType changeHandlerClassType = typeOracle.findType(AbstractBindingChangeHandlerAdapterProvider.class.getName());
//		JClassType valueChangeHandlerClassType = typeOracle.findType(AbstractBindingValueChangeHandlerAdapterProvider.class.getName());
//		JClassType depProviderClassType = typeOracle.findType(AbstractBindingChangeListenerAdapterProvider.class.getName());
//		JClassType clickHandlerClassType = typeOracle.findType(AbstractBindingClickHandlerAdapterProvider.class.getName());

//		JClassType[] bindingAdapterProviderClassTypes = new JClassType[bindingAdapterProviders.size()];
//		
//		int i = 0;
//		for (Class<?> clazz : bindingAdapterProviders) {
//			bindingAdapterProviderClassTypes[i] = typeOracle.findType(clazz.getName());
//			i++;
//		}
		
		for (JClassType classType : types) {
			if (classType.isAbstract()) {
				continue;
			}

			String property = getBindingProperty(classType);

			if (property == null) {
				continue;
			}

			JClassType widgetClassType = null;
			try {
//				widgetClassType = RebindUtils.getGenericsFromSuperclassType(classType, new JClassType[] {valueChangeHandlerClassType, changeHandlerClassType,
//						depProviderClassType, clickHandlerClassType}, 0);
				widgetClassType = RebindUtils.getGenericsFromSuperclassType(classType, 0);

				supportedTypes.put(widgetClassType.getQualifiedSourceName(), new BindingComponent(property, getBindingType(classType)));
			} catch (NotFoundException e) {
				System.out.println("Cannot extract generics from superclass of class " + classType.getQualifiedSourceName());
				e.printStackTrace();
			}
		}
	}

	private static Class<? extends Annotation> getBindingType(JClassType classType) {
		for (AbstractBindingCreator<? extends Annotation> bindingCreator : bindingCreators) {
			Class<? extends Annotation> clazz = bindingCreator.getSupportedClass();

			if (classType.getAnnotation(clazz) != null) {
				return clazz;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <M extends Annotation> String getBindingProperty(JClassType classType) {
		for (AbstractBindingCreator<? extends Annotation> bindingCreator : bindingCreators) {
			Annotation annotation = classType.getAnnotation(bindingCreator.getSupportedClass());
			if (annotation == null) {
				continue;
			}
			AbstractBindingCreator<M> typedBindingCreator = (AbstractBindingCreator<M>) bindingCreator;
			return typedBindingCreator.getPropertyValue((M) annotation);
		}

		return null;
	}
}