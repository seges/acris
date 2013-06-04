/**
 * 
 */
package sk.seges.acris.scaffold.mvp;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.pap.printer.table.column.TextColumnPrinter;

/**
 * @author ladislav.gazo
 */
public class DefaultViewConfiguration {
	private Map<Class<?>, Map<String, Class<?>>> renderComponents = new HashMap<Class<?>, Map<String,Class<?>>>();
	
	public DefaultViewConfiguration() {
		HashMap<String, Class<?>> tableRenderComponents = new HashMap<String, Class<?>>();
		tableRenderComponents.put(String.class.getName(), TextColumnPrinter.class);
		renderComponents.put(Singleselect.class, tableRenderComponents);
	}
	
	public Class<?> getRenderComponent(Class<?> type, String returnType) {
		return renderComponents.get(type).get(returnType);
	}

}
