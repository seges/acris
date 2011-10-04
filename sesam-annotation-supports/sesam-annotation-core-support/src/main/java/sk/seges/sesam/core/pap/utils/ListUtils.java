package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

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

	@SuppressWarnings("unchecked")
	public static  <T, S extends T> List<? extends T> addUnique(List<? extends T> source, S t) {
		if (t == null) {
			return source;
		}
		
		String canonicalName = t.toString();
		
		if (t instanceof MutableDeclaredType) {
			canonicalName = ((MutableDeclaredType)t).getCanonicalName();
		} else if (t instanceof Class) {
			canonicalName = ((Class<?>)t).getCanonicalName();
		}

		for (T s : source) {
			String sourceName = t.toString();
			
			if (s instanceof MutableDeclaredType) {
				sourceName = ((MutableDeclaredType)s).getCanonicalName();
			} else if (s instanceof Class) {
				sourceName = ((Class<?>)s).getCanonicalName();
			}
			if (sourceName.equals(canonicalName)) {
				return source;
			}
		}

		
		((List<T>)source).add(t);
		
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

	@SuppressWarnings("unchecked")
	public static  <T, S extends T> List<? extends T> add(List<? extends T> source, S t) {
		((List<T>)source).add(t);
		
		return source;
	}

	public static  <T> List<T> add(List<T> source, Collection<T> additions) {
		if (additions != null) {
			for (T addClass : additions) {
				source.add(addClass);
			}
		}
		return source;
	}

	public static <T extends Type> List<T> add(List<T> source, T[] additions) {
		if (additions != null) {
			for (T addClass : additions) {
				source.add(addClass);
			}
		}
		return source;
	}
}