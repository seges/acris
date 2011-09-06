package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.printer.accessors.AccessorsPrinter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.equals.EqualsPrinter;
import sk.seges.sesam.pap.model.printer.field.FieldPrinter;
import sk.seges.sesam.pap.model.printer.hashcode.HashCodePrinter;
import sk.seges.sesam.pap.model.resolver.DefaultIdentifierResolver;
import sk.seges.sesam.pap.model.resolver.api.IdentityResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
	
	@Override
	protected void writeClassAnnotations(Element configurationElement, NamedType outputName, PrintWriter pw) {
		pw.println("@SuppressWarnings(\"serial\")");
		
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);
		
		pw.print("@" + TransferObjectMapping.class.getSimpleName() + "(");

		pw.println("dtoClass = " + getOutputClass(configurationTypeElement).getSimpleName() + ".class,");
		pw.println("		domainClassName = \"" + configurationTypeElement.getDomainTypeElement().getQualifiedName().toString() + "\", ");
		pw.println("		configurationClassName = \"" + configurationElement.toString() + "\", ");
		pw.print("		converterClassName = \"");
		pw.print(configurationTypeElement.getConverterTypeElement().getCanonicalName());
		pw.print("\"");
		pw.println(")");
		
		super.writeClassAnnotations(configurationElement, outputName, pw);
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		NamedType dtoSuperclass = toHelper.getDtoSuperclass(new ConfigurationTypeElement(typeElement, processingEnv, roundEnv));
		List<Type> result = new ArrayList<Type>();
		ListUtils.add(result, super.getImports(typeElement));
		if (dtoSuperclass != null) {
			ListUtils.add(result, new Type[] {dtoSuperclass});
		}
		ListUtils.add(result, new Type[] {TransferObjectMapping.class});
		return result.toArray(new Type[] {});
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(element, processingEnv, roundEnv);

		DtoTypeElement dto = configurationTypeElement.getDtoTypeElement();
		if (!dto.isGenerated()) {
			return supportProcessorChain();
		}
		
		return super.processElement(element, roundEnv);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement configurationElement) {
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);
		switch (type) {
			case OUTPUT_SUPERCLASS:
				NamedType dtoSuperclass = toHelper.getDtoSuperclass(configurationTypeElement);
				if (dtoSuperclass != null) {
					
					TypeElement domainObjectClass = configurationTypeElement.getDomainTypeElement().asElement();
	
					if (domainObjectClass != null) {
						TypeMirror superClassType = domainObjectClass.getSuperclass();
						return new Type[] { typeParametersSupport.applyTypeParameters(dtoSuperclass, (DeclaredType)superClassType) };
					}
					
					return new Type[] {dtoSuperclass};
				}
				break;
			case OUTPUT_INTERFACES:
				List<? extends TypeMirror> interfaces = configurationElement.getInterfaces();
				
				List<Type> interfaceTypes = new ArrayList<Type>();
				
				if (interfaces != null) {
					for (TypeMirror interfaceType: interfaces) {
						interfaceTypes.add(nameTypesUtils.toType(interfaceType));
					}
				}
				
				dtoSuperclass = toHelper.getDtoSuperclass(configurationTypeElement);
				if (dtoSuperclass == null) {
					ListUtils.add(interfaceTypes, nameTypesUtils.toType(Serializable.class) );
				}
				
				return interfaceTypes.toArray(new Type[] {});
		}
		
		return super.getOutputDefinition(type, configurationElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType immutableType) {

		TypeElement configurationElement = (TypeElement)((DeclaredType)immutableType.asType()).asElement();
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);
		
		//typeParametersSupport.applyVariableTypeParameters(immutableType, configurationTypeElement.getDomainTypeElement())
				
		return new NamedType[] {
				getOutputClass(configurationTypeElement)
		};
	}

	@Override
	protected ElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw),
				new AccessorsPrinter(processingEnv, pw),
				new EqualsPrinter(processingEnv, pw),
				new HashCodePrinter(processingEnv, pw)
		};
	}
	
	public static ImmutableType getOutputClass(ConfigurationTypeElement configurationTypeElement) {	
		return configurationTypeElement.getDtoTypeElement();
	}

	@Override
	protected IdentityResolver getIdentityResolver() {
		return new DefaultIdentifierResolver();
	}
}