package sk.seges.acris.widget.client.uibinder;

import com.google.gwt.uibinder.client.UiBinder;

/**
 * Represents a panel which components are bound to a dynamic template.
 * 
 * @author ladislav.gazo
 */
public interface DynamicUiBinder<U, O> extends UiBinder<U, O> {
	void setViewTemplate(String viewTemplate);
}
