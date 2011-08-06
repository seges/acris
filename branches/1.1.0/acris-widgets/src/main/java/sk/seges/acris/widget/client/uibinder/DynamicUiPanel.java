package sk.seges.acris.widget.client.uibinder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Helper class containing common methods for parsing view template into bound
 * components.
 * 
 * @author ladislav.gazo
 */
public abstract class DynamicUiPanel<U, O> implements DynamicUiBinder<U, O> {
	protected String viewTemplate;

	public void setViewTemplate(String viewTemplate) {
		this.viewTemplate = viewTemplate;
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
		while (child != null) {
			assign(child, owner);

			if (child.hasChildNodes()) {
				process(child, owner);
			}

			child = child.getNextSiblingElement();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public U createAndBindUi(O owner) {
		SimplePanel html2 = new SimplePanel();
		html2.getElement().setInnerHTML(viewTemplate);
		Document.get().getBody().appendChild(html2.getElement());
		parseTemplate(html2.getElement(), owner);
		html2.getElement().removeFromParent();
		return (U) html2;
	}

}
