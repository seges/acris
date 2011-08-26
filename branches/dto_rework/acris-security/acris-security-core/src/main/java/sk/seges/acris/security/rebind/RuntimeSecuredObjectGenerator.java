package sk.seges.acris.security.rebind;

import sk.seges.acris.core.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class RuntimeSecuredObjectGenerator extends AbstractGenerator {

	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		RuntimeSecuredObjectCreator runtimeSecuredObjectCreator = new RuntimeSecuredObjectCreator(
				new SecuredAnnotationProcessor());
		return runtimeSecuredObjectCreator.doGenerate(logger, context,
				typeName, this.superclassName);
	}
}