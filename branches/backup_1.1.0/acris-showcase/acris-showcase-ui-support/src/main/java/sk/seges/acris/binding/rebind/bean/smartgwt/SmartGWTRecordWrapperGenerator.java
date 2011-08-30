package sk.seges.acris.binding.rebind.bean.smartgwt;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.jsr269.BeanWrapperProcessor;
import sk.seges.acris.binding.rebind.bean.core.EnhancedWrapperCreator;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;
import sk.seges.acris.binding.rebind.configuration.DefaultBindingNamingStrategy;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.validation.rebind.InterfaceTypeNameStrategy;

public class SmartGWTRecordWrapperGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeClass) throws UnableToCompleteException {
		BindingNamingStrategy nameStrategy = new DefaultBindingNamingStrategy(new InterfaceTypeNameStrategy(BeanWrapper.class,
				BeanWrapperProcessor.BEAN_WRAPPER_SUFFIX));
		EnhancedWrapperCreator binder = new SmartGWTRecordWrapperCreator(nameStrategy);
		String className = binder.generate(logger, context, typeClass);
		return className;
	}
}