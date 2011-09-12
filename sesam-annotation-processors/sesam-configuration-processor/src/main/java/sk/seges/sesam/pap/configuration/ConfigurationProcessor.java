package sk.seges.sesam.pap.configuration;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.configuration.ConfigurationProcessorConfigurer;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.AccessorPrinter;
import sk.seges.sesam.pap.configuration.printer.ConfigurationValueConstructorPrinter;
import sk.seges.sesam.pap.configuration.printer.CopyConstructorDefinitionPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.configuration.printer.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.configuration.printer.FieldPrinter;
import sk.seges.sesam.pap.configuration.printer.GroupPrinter;
import sk.seges.sesam.pap.configuration.printer.JavaDocPrinter;
import sk.seges.sesam.pap.configuration.printer.MergePrinter;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConfigurationProcessor extends AbstractConfigurableProcessor {

	public static final String SUFFIX = "Holder";

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConfigurationProcessorConfigurer();
	}
	
	@Override
	protected Type[] getImports() {
		return new Type[] {
				ConfigurationValue.class,
				ConfigurationUtils.class
		};
	}
	
	public static NamedType getOutputClass(ImmutableType inputClass) {
		return  inputClass.addClassSufix(SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType inputClass) {
		return new NamedType[] { getOutputClass(inputClass) };
	}

	protected ElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw, processingEnv),
				new GroupPrinter(new JavaDocPrinter(pw, processingEnv),
								 new AccessorPrinter(pw, processingEnv)),
				new ConfigurationValueConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw, processingEnv),
				new EnumeratedConstructorBodyPrinter(pw),
				new CopyConstructorDefinitionPrinter(pw, processingEnv),
				new MergePrinter(pw, processingEnv)
		};
	}
	
	private boolean initializeContext(ProcessorContext context) {
		String fieldName = context.getMethod().getSimpleName().toString();
		context.setFieldName(fieldName);
		
		return true;
	}
		
	@Override
	protected boolean isSupportedKind(ElementKind kind) {
		return kind.equals(ElementKind.ANNOTATION_TYPE);
	}

	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());

		for (ElementPrinter printer: getElementPrinters(pw)) {
			printer.initialize(typeElement, outputName);
			for (ExecutableElement method: methods) {
				Parameter parameterAnnotation = method.getAnnotation(Parameter.class);
				
				if (parameterAnnotation != null && !method.getReturnType().getKind().equals(TypeKind.VOID)) {
					ProcessorContext context = new ProcessorContext();
					context.setMethod(method);
					context.setConfigurationElement(typeElement);
					context.setParameter(parameterAnnotation);
					if (initializeContext(context)) {
						printer.print(context);
					} else {
						//TODO print error message
					}
				}
			}
			printer.finish(typeElement);
		}
	}
}