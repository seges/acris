package sk.seges.sesam.pap.test.selenium.processor.model;

import java.util.ArrayList;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;

public class SeleniumSettingsProviderTypeElement extends AbstractSeleniumType {

	public static final String SUFFIX = "SettingsProvider";

	private SeleniumSuiteType seleniumSuite;
	
	private SeleniumSettingsProviderTypeElement toProvider(MutableDeclaredType mutableDeclaredType) {
		return new SeleniumSettingsProviderTypeElement(new SeleniumSuiteType(mutableDeclaredType.asElement(), processingEnv), processingEnv);
	}
	
	public SeleniumSettingsProviderTypeElement(SeleniumSuiteType seleniumSuite, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.seleniumSuite = seleniumSuite;

		setInterfaces(new ArrayList<MutableTypeMirror>());
		
		if (seleniumSuite.getSuperClass() != null && seleniumSuite.getSuperClass().getAnnotation(SeleniumSuite.class) != null) {
			setSuperClass(toProvider(seleniumSuite.getSuperClass()));
		} else {
			
			boolean found = false;
			
			for (MutableTypeMirror interfaceType: seleniumSuite.getInterfaces()) {
				if (interfaceType.getKind().isDeclared() && ((MutableDeclaredType) interfaceType).getAnnotation(SeleniumSuite.class) != null) {
					setSuperClass(toProvider((MutableDeclaredType) interfaceType));
					found = true;
					break;
				}
			}
			
			if (!found) {
				setSuperClass(processingEnv.getTypeUtils().toMutableType(CoreSeleniumSettingsProvider.class));
			}
		}

		setKind(MutableTypeKind.INTERFACE);
	}

	private MutableDeclaredType getOutputClass(MutableDeclaredType inputClass) {
		return inputClass.clone().addClassSufix(SUFFIX);
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return getOutputClass(seleniumSuite);
	}
}