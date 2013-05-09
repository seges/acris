package sk.seges.acris.widget.client.celltable.filterable;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;

public abstract class AbstractFilterableCell<C> extends AbstractCell<C> {

	protected abstract String valueToString(C value, int index);
	
	protected boolean initialzied = false;

	protected AbstractFilterableCell(String... consumedEvents) {
		super(consumedEvents);
	}
	
	@Override
	public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, C value) {
		int inputElementCount = getInputElementCount(parent);
		for (int i = 0; i < inputElementCount; i++) {
			getInputElement(parent, i).setAttribute("value", valueToString(value, i));
		}
	}

	protected int getInputElementCount(Element parent) {
		return parent.getElementsByTagName("input").getLength();
	}
	
	protected InputElement getInputElement(Element parent, int inputIndex) {
		Element elem = parent.getElementsByTagName("input").getItem(inputIndex);
		assert (elem.getClass() == InputElement.class);
		return elem.cast();
	}

	protected static native boolean hasFocus(String id) /*-{
		return id == $doc.activeElement.getAttribute("id");
	}-*/;

}
