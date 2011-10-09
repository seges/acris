package sk.seges.sesam.pap.test.selenium.processor.configurer;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;

public class SeleniumTestProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { SeleniumTest.class };
		}
		return new Type[] {};
	}

}