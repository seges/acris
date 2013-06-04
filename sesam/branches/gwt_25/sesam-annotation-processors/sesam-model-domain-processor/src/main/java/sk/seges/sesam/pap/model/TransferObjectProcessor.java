package sk.seges.sesam.pap.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.Source;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.printer.ConstantsPrinter;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.accessor.CopyAccessor;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.accessors.AccessorsPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EmptyConstructorPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.model.printer.equals.EqualsPrinter;
import sk.seges.sesam.pap.model.printer.field.FieldPrinter;
import sk.seges.sesam.pap.model.printer.hashcode.HashCodePrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
	
	protected ConfigurationProvider[] getConfigurationProviders() {
		return new ConfigurationProvider[] {
				new ClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext())
		};
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		//TODO context.getOutputClass is DTO, so use it!
		ConfigurationTypeElement configurationTypeElement = getConfigurationElement(context);
		if (!configurationTypeElement.getDto().isGenerated()) {
			return false;
		}
		return super.checkPreconditions(context, alreadyExists);
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				getConfigurationElement(context).getDto()
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		new ConstantsPrinter(context.getPrintWriter(), processingEnv).copyConstants(context.getTypeElement());
		super.processElement(context);
	}
	
	@Override
	protected void printContexts(ConfigurationTypeElement configurationTypeElement, List<TransferObjectContext> contexts, List<String> generated, TransferObjectElementPrinter printer) {
		if (configurationTypeElement.asConfigurationElement() != null && configurationTypeElement.asConfigurationElement().asType().getKind().equals(TypeKind.DECLARED)) {
			TypeElement configurationType = (TypeElement) configurationTypeElement.asConfigurationElement();
			List<? extends TypeMirror> interfaces = configurationType.getInterfaces();
			
			for (TypeMirror interfaceType: interfaces) {
				if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
					for (ExecutableElement method: ElementFilter.methodsIn(((DeclaredType)interfaceType).asElement().getEnclosedElements())) {
						
						if (MethodHelper.isGetterMethod(method)) {
							String fieldName = TransferObjectHelper.getFieldPath(method);
	
							if (!isProcessed(generated, fieldName)) {
	
								TransferObjectContext context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PUBLIC, method, null, getConfigurationProviders());
								if (context == null) {
									continue;
								}
								contexts.add(context);
							}
						}
					}
				}
			}
		}

		super.printContexts(configurationTypeElement, contexts, generated, printer);
	}

	@Override
	protected void printAdditionalMethods(ProcessorContext context) {
		
		ConfigurationTypeElement configurationTypeElement = getConfigurationElement(context);

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());

		for (ExecutableElement overridenMethod: overridenMethods) {
		
			CopyAccessor copyAccessor = new CopyAccessor(overridenMethod, processingEnv);
			
			if (copyAccessor.isMethodBodyCopied()) {
				Source elementSourceFile = getClassPathSources().getElementSourceFile(configurationTypeElement.getInstantiableDomain().asElement());
				
				if (elementSourceFile == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "No source code available for class " + 
								configurationTypeElement.getInstantiableDomain().getCanonicalName() + ". Please add source class on the classpath also.");
				} else {
					String methodBody = elementSourceFile.getMethodBody(overridenMethod);
					
					if (methodBody.trim().startsWith("{")) {
						methodBody = methodBody.trim().substring(1);
					}

					if (methodBody.endsWith("}")) {
						methodBody = methodBody.substring(0, methodBody.length() - 1);
					}

					methodBody = methodBody.trim();
					
					MutableExecutableType copiedMethod = processingEnv.getElementUtils().toMutableElement(overridenMethod).asType();

					List<MutableVariableElement> parameters = copiedMethod.getParameters();
					List<MutableVariableElement> dtoParameters = new ArrayList<MutableVariableElement>();
					
					for (MutableVariableElement parameter: parameters) {
						//TODO copy parameters annotations
						DtoType dto = processingEnv.getTransferObjectUtils().getDomainType(parameter.asType()).getDto();
						
						List<? extends MutableTypeVariable> typeVariables = null;
						
						if (parameter.asType().getKind().equals(MutableTypeKind.CLASS) || parameter.asType().getKind().equals(MutableTypeKind.INTERFACE)) {
							MutableDeclaredType typeParameter = (MutableDeclaredType)parameter.asType();
							typeVariables = typeParameter.getTypeVariables();
						}
						
						MutableVariableElement parameterElement = processingEnv.getElementUtils().getParameterElement(dto, parameter.getSimpleName());
						
						if (typeVariables != null) {
							((MutableDeclaredType)parameterElement.asType()).setTypeVariables(typeVariables.toArray(new MutableTypeVariable[] {}));
						}
						
						dtoParameters.add(parameterElement);
					}
					
					copiedMethod.setParameters(dtoParameters);
					copiedMethod.setAnnotations((AnnotationMirror)null);
					copiedMethod.addModifier(Modifier.PUBLIC);
					
					context.getOutputType().addMethod(copiedMethod);
					
					copiedMethod.getPrintWriter().println(methodBody);
				}
			}
		}
	};

	@Override
	protected TransferObjectElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new TransferObjectElementPrinter[] {
				new FieldPrinter(pw),
				new EmptyConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw),
				new EnumeratedConstructorBodyPrinter(pw),
				new AccessorsPrinter(processingEnv, pw),
				new EqualsPrinter(getEntityResolver(), processingEnv, pw),
				new HashCodePrinter(getEntityResolver(), processingEnv, pw)
		};
	}
}