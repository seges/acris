/**
 * 
 */
package sk.seges.acris.widget.rebind.table;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * @author eldzi
 */
public class BeanTableSpecGenerator extends Generator {
	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeClass) throws UnableToCompleteException {
		BeanTableSpecCreator binder = new BeanTableSpecCreator(logger,
				context, typeClass);
		String className = binder.createSpec();
		return className;
	}
}
