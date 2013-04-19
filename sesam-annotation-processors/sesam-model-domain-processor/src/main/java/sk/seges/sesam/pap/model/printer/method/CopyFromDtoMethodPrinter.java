package sk.seges.sesam.pap.model.printer.method;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.accessor.ReadOnlyAccessor;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectMappingAccessor;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.utils.CastUtils;

public class CopyFromDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private final Set<String> instances;
	
	public CopyFromDtoMethodPrinter(Set<String> instances, ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolverProvider, entityResolver, roundEnv, processingEnv);
		this.instances = instances;
	}

	@Override
	public void printCopyMethod(TransferObjectContext context, FormattedPrintWriter pw) {
		
		if (isIdField(context.getConfigurationTypeElement().getInstantiableDomain(), context.getDomainMethod().getSimpleName().toString())) {
			return;
		}

		if (new ReadOnlyAccessor(context.getDtoMethod(), processingEnv).isReadonly()) {
			return;
		}
		
		PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

		boolean nested = pathResolver.isNested();

		String currentPath = pathResolver.next();
		String fullPath = currentPath;
		String previousPath = TransferObjectElementPrinter.RESULT_NAME;
		
		DomainDeclaredType domainTypeElement = context.getConfigurationTypeElement().getDomain();
		DomainDeclaredType instantiableDomainTypeElement = context.getConfigurationTypeElement().getInstantiableDomain();
		
		if (nested && context.getConfigurationTypeElement().getDomain() != null) {
			
			DomainDeclaredType referenceDomainType = domainTypeElement;
			
			String dtoName = TransferObjectElementPrinter.DTO_NAME;
			
			while (pathResolver.hasNext()) {

				DomainType instantiableDomainReference = referenceDomainType.getDomainReference(entityResolver, currentPath);
				
				if (instantiableDomainReference == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find getter method for the field " + currentPath + " in the " + domainTypeElement.toString(), context.getConfigurationTypeElement().asConfigurationElement());
					return;
				}

				DomainType domainReference = instantiableDomainReference;
				
				if (instantiableDomainReference.getConfigurations().size() > 0) {
					domainReference = domainReference.getConfigurations().get(0).getDomain();
				}
				

				if (!instantiableDomainReference.getKind().isDeclared()) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid mapping specified in the field " + currentPath + ". Current path (" + 
							fullPath + ") address getter type that is not class/interfaces." +
							"You probably mistyped this field in the configuration.", context.getConfigurationTypeElement().asConfigurationElement());

					return;
				}
				
				referenceDomainType = (DomainDeclaredType)domainReference;
				
				if (!instances.contains(fullPath)) {
					//TODO check if getId is null

					if (referenceDomainType.getKind().isDeclared()) {
						MutableDeclaredType fieldType = processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), new MutableDeclaredType[] { referenceDomainType.getDto() });
						
						Field field = new Field(null, fieldType);
						field.setCastType(fieldType);
						
						//String parameterName = "(" + Class.class.getSimpleName() + "<" + referenceDomainType.getDto().getSimpleName() + ">)null";
						printCopyNested(pathResolver, fullPath, referenceDomainType, context.getDomainMethod(), pw, field, dtoName);
						instances.add(fullPath);
					}
					
					if (instances.contains(currentPath)) {
						pw.println(previousPath + "." + MethodHelper.toSetter(currentPath) + "(" + currentPath + ");");
					}
				}

				previousPath = currentPath;
				currentPath = pathResolver.next();
				fullPath += MethodHelper.toMethod(currentPath);
				
				dtoName = previousPath;
			}

			if (domainTypeElement != null && domainTypeElement.getSetterMethod(context.getDomainFieldPath()) != null) {
				printCopy(pathResolver, context, pw);
			} else if (!entityResolver.isImmutable(instantiableDomainTypeElement.asElement())) {
				ExecutableElement domainGetterMethod = instantiableDomainTypeElement.getGetterMethod(currentPath);
				
				VariableElement field = MethodHelper.getField(instantiableDomainTypeElement.asConfigurationElement(), currentPath);
				
				if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + instantiableDomainTypeElement.toString(), context.getConfigurationTypeElement().asConfigurationElement());
				}
			}
		} else {
			printCopy(pathResolver, context, pw);
		}
	}
	
	protected void printCopy(PathResolver pathResolver, TransferObjectContext context, FormattedPrintWriter pw) {
		if (context.getConverter() != null) {
			printCopyByConverter(context, pathResolver, pw);
		} else if (context.useConverter()) {
			String converterName = "converter" + MethodHelper.toMethod("", context.getDtoFieldName());
			pw.print(converterProviderPrinter.getDtoConverterType(context.getDomainMethodReturnType(), true), " " + converterName + " = ");
			converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, context.getDomainMethodReturnType(), 
					new Field(TransferObjectElementPrinter.DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()), null), context.getDomainMethod(), true);
			pw.println(";");
			printCopyByLocalConverter(converterName, pathResolver, context.getDomainMethodReturnType(), context.getDtoFieldName(), pw);
		} else if (!pathResolver.isNested()){
			printCopySimple(pathResolver, context, pw);
		}
	}
	
	protected void printCopyNested(PathResolver domainPathResolver, String fullPath, DomainDeclaredType referenceDomainType, ExecutableElement method, FormattedPrintWriter pw, Field field, String dtoName) {
		if (referenceDomainType.getId(entityResolver) != null) {
			pw.print(referenceDomainType, " " + domainPathResolver.getCurrent() + " = ");
			if (referenceDomainType.getConverter() == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] No converter/configuration for " + referenceDomainType + " was found. Please, define configuration for " + referenceDomainType);
			}
			//TODO add NPE check
			//converterProviderPrinter.printDtoEnsuredConverterMethodName(referenceDomainType.getDto(), field, method, pw, false);
			converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, referenceDomainType, field, method, true);

			pw.println(".createDomainInstance(" + dtoName + "." + MethodHelper.toGetter(fullPath + MethodHelper.toMethod(MethodHelper.toField(referenceDomainType.getIdMethod(entityResolver)))) + ");");
		} else {
			pw.println(referenceDomainType, " " + domainPathResolver.getCurrent() + " = " + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ";");
			pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + " == null) {");
			pw.print(domainPathResolver.getCurrent() + " = ");
			//TODO do not cast here
			//converterProviderPrinter.printDtoEnsuredConverterMethodName(referenceDomainType.getDto(), field, method, pw, false);
			//TODO add NPE check
			converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, referenceDomainType, field, method, true);
			pw.println(".createDomainInstance(null);");
			pw.println("}");
		}
	}
	
	protected void printCopySimple(PathResolver domainPathResolver, TransferObjectContext context, FormattedPrintWriter pw) {
		
		DomainDeclaredType domainTypeElement = context.getConfigurationTypeElement().getInstantiableDomain();
		
		Boolean isMethod = false;
		ExecutableElement domainGetterMethod;
		if (domainTypeElement.asElement() != null && ProcessorUtils.hasMethod(MethodHelper.toMethod(MethodHelper.GETTER_IS_PREFIX, domainPathResolver.getCurrent()), domainTypeElement.asElement())) {
			isMethod = true;
			domainGetterMethod = domainTypeElement.getGetterMethod(domainPathResolver.getCurrent()); 
		} else {
			domainGetterMethod = domainTypeElement.getGetterMethod(domainPathResolver.getCurrent());
		}

		ConfigurationTypeElement configurationTypeElement = context.getConfigurationTypeElement();

		if (configurationTypeElement.getInstantiableDomain().getSetterMethod(domainPathResolver.getPath()) != null) {
			boolean castToInstance = configurationTypeElement.getDomain().getSetterMethod(domainPathResolver.getPath()) == null;
			
			if (castToInstance) {
				pw.print("((", configurationTypeElement.getInstantiableDomain(), ")");
			}
			pw.print(TransferObjectElementPrinter.RESULT_NAME);
			
			if (castToInstance) {
				pw.print(")");
			}
			pw.print("." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");
			
			if (context.getDomainMethodReturnType() instanceof MutableTypeVariable) {
				pw.print("(" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((MutableTypeVariable)context.getDomainMethodReturnType()).getVariable() + ")");
			}

			String dtoField = context.getDtoFieldName();

			pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + ((isMethod) ? MethodHelper.toIsGetter(dtoField) : MethodHelper.toGetter(dtoField)));
			pw.println(");");
		} else if (!entityResolver.isImmutable(domainTypeElement.asElement())) {
			
			VariableElement field = MethodHelper.getField(domainTypeElement.asConfigurationElement(), domainPathResolver.getCurrent());
			
			if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + domainPathResolver.getCurrent() + " in the class " + domainTypeElement.toString(), configurationTypeElement.asConfigurationElement());
				return;
			}
		}
	}

    protected void printCopyByConverter(TransferObjectContext context, PathResolver domainPathResolver, FormattedPrintWriter pw) {
		String converterName = "converter" + MethodHelper.toMethod("", context.getDtoFieldName());
		pw.print(context.getConverter(), " " + converterName + " = ");

		Field field = new Field(
				(context.getDomainMethodReturnType().getKind().equals(MutableTypeKind.TYPEVAR) ? "(" + context.getConverter().getDto() + ")" : "") + 
				TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()), context.getConverter().getDto());
		TransferObjectMappingAccessor transferObjectMappingAccessor = new TransferObjectMappingAccessor(context.getDtoMethod(), processingEnv);
		if (transferObjectMappingAccessor.isValid() && transferObjectMappingAccessor.getConverter() != null) {
			converterProviderPrinter.printDtoGetConverterMethodName(context.getConverter().getDto(), field, context.getDtoMethod(), pw, false);
		} else {
			converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, context.getConverter().getDomain(), field, context.getDomainMethod(), true);
		}
		pw.println(";");
		
		pw.println("if (" + converterName + " != null) {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");

		if (context.getDomainMethodReturnType().getKind().equals(MutableTypeKind.TYPEVAR)) {
			pw.print("(" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((MutableTypeVariable)context.getDomainMethodReturnType()).getVariable().toString() + ")");
		}
		
		pw.print(CastUtils.class, ".cast(");
		pw.print("(", getWildcardDelegate(context.getConverter().getDomain()), ")");
		pw.print(converterName + ".fromDto(");
		//TODO check for the nested
		//TODO: only if necessary
		pw.print(CastUtils.class, ".cast(");
		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()) + ", ");
		pw.print(getTypeVariableDelegate(getDelegateCast(context.getConverter().getDto(), true)), ".class)");
		pw.println("), ", getTypeVariableDelegate(getDelegateCast(context.getConverter().getDomain(), true)), ".class));");
		pw.println("}");
    }
	
	protected void printCopyByLocalConverter(String localConverterName, PathResolver domainPathResolver, DomainType domainMethodReturnType, String dtoField, FormattedPrintWriter pw) {
		pw.println("if (" + localConverterName + " != null) {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(" + 
				localConverterName + ".fromDto(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField) + ")");
		pw.println(");");
		pw.println("} else {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()));
		if (domainMethodReturnType.getKind().equals(MutableTypeKind.TYPEVAR)) {
			pw.print("((" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((MutableTypeVariable)domainMethodReturnType).getVariable() + ")");
		} else {
			pw.print("((" + domainMethodReturnType + ")");
		}
		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
		pw.println(");");
		pw.println("}");
	}	
}