/**
 * Copyright (c) 2011 Seges s.r.o.
 * All Rights Reserved.
 *
 * Any usage, duplication or redistribution of this software is allowed only
 * according to separate agreement prepared in written between Seges
 * and authorized party.
 */
package sk.seges.acris.widget.client.uibinder;


import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author ladislav.gazo
 */
public abstract class DynamicUiPanel<U, O> implements DynamicUiBinder<U, O> {
	protected String html;
	
	public void setHtml(String html) {
		this.html = html;
	}
	
	protected abstract void assign(Element child, O owner);
	
	private void parseTemplate(Element root, O owner) {
		process(root, owner);
	}
	
	protected String getFieldName(Element element) {
		return element.getAttribute("ui:field");
	}
	
	private void process(Element root, O owner) {
		Element child = root.getFirstChildElement();
		while(child != null) {
			assign(child, owner);
			
			if(child.hasChildNodes()) {
				process(child, owner);
			}
			
			child = child.getNextSiblingElement();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public U createAndBindUi(O owner) {
		HTML html2 = new HTML(html);
		Document.get().getBody().appendChild(html2.getElement());
		parseTemplate(html2.getElement(), owner);
		html2.getElement().removeFromParent();
		return (U)html2;
	}

}
