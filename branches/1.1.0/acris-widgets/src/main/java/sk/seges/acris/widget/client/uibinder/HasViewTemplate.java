package sk.seges.acris.widget.client.uibinder;

/**
 * Determines any component which graphical representation is dynamically
 * generated based on a template. The template is usually a piece of HTML code
 * or a templating engine language.
 * 
 * @author ladislav.gazo
 */
public interface HasViewTemplate {
	void setViewTemplate(String viewTemplate);
}
