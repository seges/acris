package sk.seges.acris.security.client;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Extension to the {@link ISecuredObject} with the possibility to set the visibility and the enabled flag to child
 * widgets.
 * 
 * @author mig
 */
public interface IManageableSecuredObject extends ISecuredObject {
	
	void setVisible(UIObject widget, boolean visible);

	void setEnabled(HasEnabled widget, boolean enabled);
}
