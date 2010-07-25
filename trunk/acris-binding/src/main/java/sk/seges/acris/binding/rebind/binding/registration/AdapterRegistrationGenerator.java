package sk.seges.acris.binding.rebind.binding.registration;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class AdapterRegistrationGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		AdapterRegistrationCreator creator = new AdapterRegistrationCreator();
		return creator.generate(logger, context, typeName);
	}
}