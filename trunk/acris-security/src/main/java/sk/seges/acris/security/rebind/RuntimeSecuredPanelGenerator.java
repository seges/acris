package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class RuntimeSecuredPanelGenerator extends AbstractGenerator {

	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		RuntimeSecuredPanelCreator runtimeSecuredPanelCreator = new RuntimeSecuredPanelCreator(new SecuredAnnotationProcessor());
		return runtimeSecuredPanelCreator.doGenerate(logger, context, typeName, this.superclassName);
	}
}