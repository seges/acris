package sk.seges.corpis.pap.model.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;

public class HibernatePersistentElementHolderConverter implements ElementHolderTypeConverter {

	private MutableProcessingEnvironment processingEnv;
	private Map<Class<?>, Class<?>> collectionMappings = new TreeMap<Class<?>, Class<?>>();

	public HibernatePersistentElementHolderConverter(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;

		//TODO should be only lazy entities
		//LinkedLists, ArrayList, ....
		collectionMappings.put(List.class, ArrayList.class);
		collectionMappings.put(Map.class, HashMap.class);
		collectionMappings.put(Set.class, HashSet.class);
		collectionMappings.put(Collection.class, ArrayList.class);
	}

	@Override
	public TypeMirror getIterableDtoType(MutableTypeMirror collectionType) {
		//TODO handle if the collection is instance of PersistentCollection
		for (Entry<Class<?>, Class<?>> collections : collectionMappings.entrySet()) {
			if (processingEnv.getTypeUtils().implementsType(collectionType, processingEnv.getTypeUtils().toMutableType(collections.getKey()))) {
				TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(collections.getValue().getCanonicalName());

				if (typeElement != null) {
					return typeElement.asType();
				}
			}

		}

		return null;
	}
}