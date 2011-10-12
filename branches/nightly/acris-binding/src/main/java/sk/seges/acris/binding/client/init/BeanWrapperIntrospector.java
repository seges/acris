/**
 * 
 */
package sk.seges.acris.binding.client.init;

import com.google.gwt.core.client.GWT;

/**
 * Fills introspector with bean infos for all wrappers used in the project. It
 * is executed only in production mode (because of several reasons)...
 * 
 * <p>
 * Reasons:
 * <ul>
 * <li>java.lang.reflect.Method is final class and cannot be overriden in devel
 * mode because there is JRE runtime directly</li>
 * <li>in devel mode there is GenericBeanInfo generated but in prod. mode there
 * is GwtBeanInfo, they are not compatible so GwtIntrospector has problem with
 * it (casting issues, class incompatibility)</li>
 * </ul>
 * </p>
 * 
 * @author eldzi
 */
public class BeanWrapperIntrospector {
	private static boolean initialized = false;

	public static void init() {
		if (!GWT.isScript() || initialized) {
			return;
		}

		try {
			GWT.create(BeanWrapperIntrospector.class);
			initialized = true;
		} catch (Throwable t) {
			throw new RuntimeException("Unable to create bean wrapper introspector", t);
		}
	}
}
