package sk.seges.acris.widget.client;

import sk.seges.acris.widget.client.uibinder.HasViewTemplate;

/**
 * @author ladislav.gazo
 */
public interface HasMicroTemplate extends HasViewTemplate {
	void render(String data);
	void setStyleName(String styleName);
}
