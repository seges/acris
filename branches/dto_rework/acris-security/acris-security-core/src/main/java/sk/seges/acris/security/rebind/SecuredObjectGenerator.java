package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * generates secured panel according to annotations in original panel, which is
 * replaced by secured panel
 */
public class SecuredObjectGenerator extends AbstractGenerator {

	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		SecuredObjectCreator securedPanelCreator = new SecuredObjectCreator(new SecuredAnnotationProcessor());
		return securedPanelCreator.doGenerate(logger, context, typeName, this.superclassName);
	}
}