/**
 * 
 */
package sk.seges.acris.security.showcase.client;

import sk.seges.acris.security.client.IRuntimeSecuredObject;
import sk.seges.acris.security.client.ui.SecuredComposite;

import com.google.gwt.user.client.ui.Label;

/**
 * @author ladislav.gazo
 */
public class MenuItem extends SecuredComposite implements IRuntimeSecuredObject {
	protected Label text;
	
	public MenuItem() {
		text = new Label("Secured item in runtime");
		initWidget(text);
	}
}
