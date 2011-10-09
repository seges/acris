package sk.seges.sesam.pap.model.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;

public class HibernatePersistentElementHolderConverter implements ElementHolderTypeConverter {

	private ProcessingEnvironment processingEnv;
	private Map<Class<?>, Class<?>> collectionMappings = new HashMap<Class<?>, Class<?>>();

	public HibernatePersistentElementHolderConverter(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;

		//TODO should be only lazy entities
		//LinkedLists, ArrayList, ....
		collectionMappings.put(List.class, ArrayList.class);
		collectionMappings.put(Map.class, HashMap.class);
		collectionMappings.put(Set.class, HashSet.class);
	}

	@Override
	public TypeMirror getIterableDtoType(TypeMirror collectionType) {
		for (Entry<Class<?>, Class<?>> collections : collectionMappings.entrySet()) {
			if (ProcessorUtils.implementsType(collectionType,
					processingEnv.getElementUtils().getTypeElement(collections.getKey().getCanonicalName()).asType())) {
				TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(collections.getValue().getCanonicalName());

				if (typeElement != null) {
					return typeElement.asType();
				}
			}

		}

		return null;
	}
}