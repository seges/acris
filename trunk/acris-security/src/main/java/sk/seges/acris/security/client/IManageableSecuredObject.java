package sk.seges.acris.security.client;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extension to the {@link ISecuredObject} with the possibility to set the visibility 
 * and the enabled flag to child widgets.
 * 
 * @author mig
 */
public interface IManageableSecuredObject extends ISecuredObject {
    public void setVisible(Widget widget, boolean visible);
    public void setEnabled(FocusWidget widget, boolean enabled);
}
