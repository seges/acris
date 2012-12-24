package sk.seges.acris.widget.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * @author ladislav.gazo
 */
public class DynamicUiBinderGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		return new DynamicUiBinderCreator(logger, context, typeName).create();
	}

}
