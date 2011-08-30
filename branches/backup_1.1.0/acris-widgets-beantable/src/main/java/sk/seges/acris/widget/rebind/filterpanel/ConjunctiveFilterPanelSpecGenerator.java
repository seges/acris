/**
 * 
 */
package sk.seges.acris.widget.rebind.filterpanel;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * @author mig
 */
public class ConjunctiveFilterPanelSpecGenerator extends Generator {
	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeClass) throws UnableToCompleteException {
		ConjunctiveFilterPanelSpecCreator binder = new ConjunctiveFilterPanelSpecCreator(logger,
				context, typeClass);
		String className = binder.createSpec();
		return className;
	}
}
