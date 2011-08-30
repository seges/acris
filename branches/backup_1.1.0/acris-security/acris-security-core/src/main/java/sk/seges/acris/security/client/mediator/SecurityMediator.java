/**
 * 
 */
package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.client.CheckableSecuredObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * Mediates various calls to methods available after security generators run and
 * takes into account implementations of generated classes (like implemented
 * interface by generator).
 * 
 * @author ladislav.gazo
 */
public class SecurityMediator {
	/**
	 * Checks a secured widget and reevaluates its secureness. If the widget
	 * isn't implementing {@link CheckableSecuredObject} nothing happens.
	 * 
	 * @param widget
	 */
	public static void checkSecuredWidget(Widget widget) {
		if (widget instanceof CheckableSecuredObject) {
			((CheckableSecuredObject) widget).check();
		}
	}
}
