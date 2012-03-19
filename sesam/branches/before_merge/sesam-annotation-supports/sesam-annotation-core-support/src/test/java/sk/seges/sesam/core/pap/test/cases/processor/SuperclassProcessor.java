package sk.seges.sesam.core.pap.test.cases.processor;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport.TypePrinterSupport;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.test.cases.annotation.SuperclassTestAnnotation;

@PrintSupport(autoIdent = true, printer = @TypePrinterSupport(printSerializer = ClassSerializer.SIMPLE))
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SuperclassProcessor extends MutableAnnotationProcessor {

	public static class SuperclassAwareType extends DelegateMutableDeclaredType {

		public static final String SUFFIX = "Generated";

		private final RoundContext context;

		public SuperclassAwareType(RoundContext context) {
			this.context = context;
		}

		@Override
		protected MutableDeclaredType getDelegate() {
			return context.getMutableType().setSuperClass(context.getMutableType().clone()).addClassSufix(SUFFIX);
		}
	}

	@Override
	protected void processElement(ProcessorContext context) {}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new SuperclassAwareType(context) };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DefaultProcessorConfigurer() {
			@Override
			protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
				switch (element) {
				case PROCESSING_ANNOTATIONS:
					return new Type[] { SuperclassTestAnnotation.class };
				}
				return new Type[] {};
			}
		};
	}
}