/**
 * 
 */
package sk.seges.acris.binding.rebind.bean;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * @author eldzi
 */
public class BeanWrapperGenerator extends Generator {
	/* (non-Javadoc)
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeClass) throws UnableToCompleteException {
		BeanWrapperCreator binder = new BeanWrapperCreator(logger,
				context, typeClass, typeClass);
		String className = binder.createWrapper();
		return className;
	}
}
