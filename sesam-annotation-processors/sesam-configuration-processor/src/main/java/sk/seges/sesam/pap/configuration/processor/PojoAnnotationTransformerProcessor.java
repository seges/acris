package sk.seges.sesam.pap.configuration.processor;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.configurer.PojoAnnotationTransformerProcessorConfigurer;
import sk.seges.sesam.pap.configuration.model.parameter.ModelType;
import sk.seges.sesam.pap.configuration.model.parameter.ParametersIterator;
import sk.seges.sesam.pap.configuration.printer.AbstractSettingsElementPrinter;
import sk.seges.sesam.pap.configuration.printer.AccessorPrinter;
import sk.seges.sesam.pap.configuration.printer.DefaultConstructorPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.configuration.printer.FieldWithDefaultsPrinter;
import sk.seges.sesam.pap.configuration.printer.NestedParameterPrinter;
import sk.seges.sesam.pap.configuration.processor.api.NestableProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class PojoAnnotationTransformerProcessor extends MutableAnnotationProcessor implements NestableProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new PojoAnnotationTransformerProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new ModelType((DeclaredType)context.getTypeElement().asType(), processingEnv) };
	}
		
	protected AbstractSettingsElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new AbstractSettingsElementPrinter[] {
				new NestedParameterPrinter(pw, this, processingEnv),
				new FieldWithDefaultsPrinter(pw, processingEnv),
				new AccessorPrinter(pw, processingEnv),
				new DefaultConstructorPrinter(pw, processingEnv),
				new EnumeratedConstructorDefinitionPrinter(pw, processingEnv),
				new EnumeratedConstructorBodyPrinter(pw, processingEnv)
		};
	}
			
	@Override
	protected void processElement(ProcessorContext context) {
		processAnnotation(context.getTypeElement(), context.getOutputType(), context.getPrintWriter());
	}

	public void processAnnotation(TypeElement typeElement, MutableDeclaredType outputName, FormattedPrintWriter pw) {

		for (AbstractSettingsElementPrinter printer: getElementPrinters(pw)) {
			printer.initialize(typeElement, outputName);

			ParametersIterator parametersIterator = new ParametersIterator(typeElement, processingEnv);
			while (parametersIterator.hasNext()) {
				parametersIterator.next().handle(printer);
			}

			printer.finish(typeElement);
		}
	}
}
