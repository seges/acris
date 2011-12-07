package sk.seges.sesam.core.pap.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.lang.model.element.Element;

public class ElementSorter {

	public static void sort(List<? extends Element> elements) {
		Collections.sort(elements, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
	}
}
