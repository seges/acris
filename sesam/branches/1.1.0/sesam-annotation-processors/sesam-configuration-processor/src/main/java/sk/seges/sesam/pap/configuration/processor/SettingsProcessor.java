package sk.seges.sesam.pap.configuration.processor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.configuration.ConfigurationProcessorConfigurer;
import sk.seges.sesam.pap.configuration.model.SettingsIterator;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;
import sk.seges.sesam.pap.configuration.printer.AccessorPrinter;
import sk.seges.sesam.pap.configuration.printer.ConfigurationValueConstructorPrinter;
import sk.seges.sesam.pap.configuration.printer.CopyConstructorDefinitionPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.configuration.printer.FieldPrinter;
import sk.seges.sesam.pap.configuration.printer.GroupPrinter;
import sk.seges.sesam.pap.configuration.printer.HelperPrinter;
import sk.seges.sesam.pap.configuration.printer.JavaDocPrinter;
import sk.seges.sesam.pap.configuration.printer.MergePrinter;
import sk.seges.sesam.pap.configuration.printer.NestedParameterPrinter;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SettingsProcessor extends AbstractConfigurableProcessor {


	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConfigurationProcessorConfigurer();
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType inputClass) {
		return new NamedType[] { new SettingsTypeElement(inputClass, processingEnv) };
	}

	protected SettingsElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new SettingsElementPrinter[] {
				new NestedParameterPrinter(pw, this, processingEnv),
				new FieldPrinter(pw, processingEnv),
				new GroupPrinter(new JavaDocPrinter(pw, processingEnv),
								 new AccessorPrinter(pw, processingEnv)),
				new ConfigurationValueConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw, processingEnv),
				new EnumeratedConstructorBodyPrinter(pw),
				new CopyConstructorDefinitionPrinter(pw, processingEnv),
				new MergePrinter(pw, processingEnv),
				new HelperPrinter(pw, processingEnv)
		};
	}
			
	//TODO read this from configuration
	@Override
	protected boolean isSupportedKind(ElementKind kind) {
		return kind.equals(ElementKind.ANNOTATION_TYPE);
	}
	
	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		processAnnotation(typeElement, outputName, pw);
	}

	public void processAnnotation(TypeElement typeElement, NamedType outputName, FormattedPrintWriter pw) {

		for (SettingsElementPrinter printer: getElementPrinters(pw)) {
			printer.initialize(typeElement, outputName);

			SettingsIterator settingsIterator = new SettingsIterator(typeElement, processingEnv);
			while (settingsIterator.hasNext()) {
				settingsIterator.next().handle(printer);
			}

			printer.finish(typeElement);
		}
	}	
}