package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class ListUtils {

	public static boolean contains(Iterable<? extends Element> elements, Element element) {
		String elementString = element.toString();
		for (Element listElement: elements) {
			if (listElement.toString().equals(elementString)) {
				return true;
			}
		}
		
		return false;
	}

	public static  <T> List<T> addUnique(List<T> source, T t) {
		if (t == null) {
			return source;
		}
		
		String canonicalName = t.toString();
		
		if (t instanceof NamedType) {
			canonicalName = ((NamedType)t).getCanonicalName();
		} else if (t instanceof Class) {
			canonicalName = ((Class<?>)t).getCanonicalName();
		}

		for (T s : source) {
			String sourceName = t.toString();
			
			if (s instanceof NamedType) {
				sourceName = ((NamedType)s).getCanonicalName();
			} else if (s instanceof Class) {
				sourceName = ((Class<?>)s).getCanonicalName();
			}
			if (sourceName.equals(canonicalName)) {
				return source;
			}
		}

		source.add(t);
		return source;
	}

	public static  <T> List<T> addUnique(List<T> source, Collection<T> additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

	public static <T extends Type> List<T> addUnique(List<T> source, T[] additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

}
