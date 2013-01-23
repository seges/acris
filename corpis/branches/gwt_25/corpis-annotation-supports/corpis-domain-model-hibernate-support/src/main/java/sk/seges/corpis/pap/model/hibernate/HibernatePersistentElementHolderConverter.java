package sk.seges.corpis.pap.model.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;

public class HibernatePersistentElementHolderConverter implements ElementHolderTypeConverter {

	class CollectionMapping {
		Class<?> sourceClass;
		Class<?> targetClass;
		
		public CollectionMapping(Class<?> sourceClass, Class<?> targetClass) {
			this.sourceClass = sourceClass;
			this.targetClass = targetClass;
		}
	}
	
	private MutableProcessingEnvironment processingEnv;
	private List<CollectionMapping> collectionMappings = new LinkedList<CollectionMapping>();

	public HibernatePersistentElementHolderConverter(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;

		//TODO should be only lazy entities
		//LinkedLists, ArrayList, ....
		collectionMappings.add(new CollectionMapping(List.class, ArrayList.class));
		collectionMappings.add(new CollectionMapping(Map.class, HashMap.class));
		collectionMappings.add(new CollectionMapping(Set.class, HashSet.class));
		collectionMappings.add(new CollectionMapping(Collection.class, ArrayList.class));
	}

	@Override
	public TypeMirror getIterableDtoType(MutableTypeMirror collectionType) {
		//TODO handle if the collection is instance of PersistentCollection
		for (CollectionMapping collectionMapping : collectionMappings) {
			if (processingEnv.getTypeUtils().implementsType(collectionType, processingEnv.getTypeUtils().toMutableType(collectionMapping.sourceClass))) {
				TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(collectionMapping.targetClass.getCanonicalName());

				if (typeElement != null) {
					return typeElement.asType();
				}
			}

		}

		return null;
	}
}