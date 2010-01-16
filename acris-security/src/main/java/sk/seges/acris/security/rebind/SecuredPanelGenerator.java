package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * generates secured panel according to annotations in original panel, which is
 * replaced by secured panel
 */
public class SecuredPanelGenerator extends AbstractGenerator {

	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		SecuredPanelCreator securedPanelCreator = new SecuredPanelCreator(new SecuredAnnotationProcessor());
		return securedPanelCreator.doGenerate(logger, context, typeName, this.superclassName);
	}
}