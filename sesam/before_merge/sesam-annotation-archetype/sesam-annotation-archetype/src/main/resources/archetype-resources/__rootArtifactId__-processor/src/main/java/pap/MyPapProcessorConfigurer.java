package ${package}.pap;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

import ${package}.pap.annotation.MyPapAnnotation;

public class MyPapProcessorConfigurer extends DefaultProcessorConfigurer {

        @Override
        protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
                switch (element) {
                case PROCESSING_ANNOTATIONS:
                        return new Type[] { MyPapAnnotation.class };
                }
                return super.getConfigurationElement(element);
        }
}

