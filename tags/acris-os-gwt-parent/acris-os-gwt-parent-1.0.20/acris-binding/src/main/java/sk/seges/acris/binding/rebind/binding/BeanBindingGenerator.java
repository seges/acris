package sk.seges.acris.binding.rebind.binding;

import sk.seges.acris.binding.rebind.configuration.DefaultBindingNamingStrategy;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class BeanBindingGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		DefaultBindingNamingStrategy typeStrategy = new DefaultBindingNamingStrategy(null);
		BeanBindingCreator creator = new BeanBindingCreator(typeStrategy);
		return creator.generate(logger, context, typeName);
	}
}