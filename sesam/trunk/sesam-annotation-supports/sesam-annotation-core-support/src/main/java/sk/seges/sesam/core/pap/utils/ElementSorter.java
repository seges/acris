package sk.seges.sesam.core.pap.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ElementSorter {

	public static void sort(List<? extends Element> elements) {
		Collections.sort(elements, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
			}
		});
	}
	
	public static void sortMutableTypes(List<? extends MutableTypeMirror> elements) {
		Collections.sort(elements, new Comparator<MutableTypeMirror>() {

			@Override
			public int compare(MutableTypeMirror o1, MutableTypeMirror o2) {
				return o1.toString(ClassSerializer.SIMPLE).compareTo(o2.toString(ClassSerializer.SIMPLE));
			}
		});
	}

}
