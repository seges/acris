package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.pap.model.model.AbstractElementPrinter;
import sk.seges.sesam.pap.model.model.ElementPrinter;
import sk.seges.sesam.pap.model.model.PathResolver;
import sk.seges.sesam.pap.model.model.ProcessorContext;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;
import sk.seges.sesam.shared.model.converter.CachedConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectConvertorProcessor extends AbstractTransferProcessor {

	private static final String DEFAULT_SUFFIX = "Converter";	
	private static final String RESULT_NAME = "_result";
	private static final String DOMAIN_NAME = "_domain";
	private static final String DTO_NAME = "_dto";
	
	private Set<String> instances = new HashSet<String>();

	interface CopyMethodPrinter {

		void printCopyMethod(ProcessorContext context, PrintWriter pw, NamedType converterType);
	}

	class CopyFromDtoMethodPrinter implements CopyMethodPrinter {

		@Override
		public void printCopyMethod(ProcessorContext context, PrintWriter pw, NamedType converterType) {
			PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

			boolean nested = pathResolver.isNested();

			String currentPath = pathResolver.next();
			String fullPath = currentPath;
			String previousPath = RESULT_NAME;
						
			TypeElement element = context.getDomainTypeElement();

			if (nested) {
				while (pathResolver.hasNext()) {
	
					ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
					
					if (domainGetterMethod == null) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find getter method for the field " + currentPath + " in the " + element.toString(), context.getConfigurationElement());
						return;
					}
	
					if (!instances.contains(fullPath)) {
						//TODO check if getId is null

						if (domainGetterMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
							Element referenceElement = ((DeclaredType)domainGetterMethod.getReturnType()).asElement();

							Element configurationElement = toHelper.getConfigurationElement((TypeElement)referenceElement, roundEnv);

							if (configurationElement == null) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find conversion configuration for type " + referenceElement.toString(), context.getConfigurationElement());
								return;
							}
							
							pw.print(getNameTypes().toType(referenceElement) + " " + currentPath + " = ");
							printConverterInstance(pw, getOutputClass((ImmutableType)getNameTypes().toType(configurationElement), getPackageValidatorProvider(), processingEnv));
							pw.println(".getDomainInstance(" + DTO_NAME + "." + toHelper.toGetter(fullPath + toHelper.toMethod(toHelper.toField(toHelper.getIdMethod(referenceElement)))) + ");");
							instances.add(fullPath);
						}
						pw.println(previousPath + "." + toHelper.toSetter(currentPath) + "(" + currentPath + ");");
					}

					previousPath = currentPath;
					currentPath = pathResolver.next();
					fullPath += toHelper.toMethod(currentPath);
				}

				if (toHelper.getDomainSetterMethod(element, context.getDomainFieldPath()) != null) {
					pw.println(RESULT_NAME + "." + toHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME + "." + toHelper.toGetter(context.getFieldName()) + ");");					
				} else {
					ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && toHelper.isIdField(currentPath)) || !toHelper.isIdMethod(domainGetterMethod)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationElement());
					}
				}
			} else {
				boolean generated = true;
				if (converterType != null) {
					String converterName = "converter" + toHelper.toMethod("", context.getFieldName());
					pw.print(converterType.getCanonicalName() + " " + converterName + " = ");
					printConverterInstance(pw, converterType);
					pw.println(";");
					pw.print(RESULT_NAME + "." + toHelper.toSetter(context.getDomainFieldPath()) + "(" + converterName + ".fromDto(" + DTO_NAME  + "." + toHelper.toGetter(context.getFieldName()));
				} else {
					ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && toHelper.isIdField(currentPath)) || !toHelper.isIdMethod(domainGetterMethod)) {
						pw.print(RESULT_NAME + "." + toHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME  + "." + toHelper.toGetter(context.getFieldName()));
					} else {
						if ((domainGetterMethod == null && toHelper.isIdField(currentPath)) || !toHelper.isIdMethod(domainGetterMethod)) {
							processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationElement());
						}
						generated = false;
					}
				}
				
				if (generated) {
					if (converterType != null) {
						pw.print(")");
					}
					pw.println(");");
				}
			}
		}
	}
	
	class CopyToDtoMethodPrinter implements CopyMethodPrinter {

		@Override
		public void printCopyMethod(ProcessorContext context, PrintWriter pw, NamedType converterType) {
			PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

			boolean nested = pathResolver.isNested();
			
			if (nested) {
				pw.print("if (");
				int i = 0;
				
				String methodPath = DOMAIN_NAME;
				
				while (pathResolver.hasNext()) {
					
					String path = pathResolver.next();
					
					if (pathResolver.hasNext()) {
						if (i > 0) {
							pw.print(" && ");
						}
						methodPath += "." + toHelper.toGetter(path);
						
						pw.print(methodPath + " != null");
						i++;
					}
				}
				pw.println(") {");
			} 

			if (converterType != null) {
				String converterName = "converter" + toHelper.toMethod("", context.getFieldName());
				
				pw.print(converterType.getCanonicalName() + " " + converterName + " = ");
				printConverterInstance(pw, converterType);
				pw.println(";");

				pw.print(RESULT_NAME + "." + toHelper.toSetter(context.getFieldName()) + "(" + converterName + ".toDto(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
			} else {
				pw.print(RESULT_NAME + "." + toHelper.toSetter(context.getFieldName()) + "(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
			}
			
			if (converterType != null) {
				pw.print(")");
			}
			pw.println(");");

			if (nested) {
				pw.println("} else {");
				pw.println(RESULT_NAME + "." + toHelper.toSetter(context.getFieldName()) + "(null);");
				pw.println("}");
				pw.println("");
			}
		}	
	}

	class CopyToDtoPrinter extends AbstractElementPrinter {

		public CopyToDtoPrinter(PrintWriter pw) {
			super(pw);
		}
		
		@Override
		public void print(ProcessorContext context) {
			copy(context, pw, new CopyToDtoMethodPrinter());
		}
	
		@Override
		public void initialize(TypeElement typeElement) {
			
			ImmutableType dtoType = getDtoType(typeElement);
					
			TypeElement domainObjectClass = toHelper.getDomainTypeElement(typeElement);

			pw.println("public " + dtoType.getSimpleName() + " createDtoInstance(" + Serializable.class.getSimpleName() + " id) {");
			printDtoInstancer(pw, dtoType);
			pw.println("}");
			pw.println();
			pw.println("protected " + Class.class.getSimpleName() + "<" + domainObjectClass.getSimpleName() + "> getDomainClass() {");
			pw.println("return " + domainObjectClass.getSimpleName() + ".class;");
			pw.println("}");
			pw.println();
			pw.println("public " + dtoType.getSimpleName() + " toDto(" + domainObjectClass.getSimpleName().toString() + " " + DOMAIN_NAME + ") {");
			pw.println();
			pw.println("if (" + DOMAIN_NAME + "  == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();

			ExecutableElement idMethod = toHelper.getIdMethod(domainObjectClass);
			
			if (idMethod == null) {
				idMethod = toHelper.getIdMethod(typeElement);
			}
			
			if (idMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + typeElement.toString(), typeElement);
				return;
			}

			if (idMethod == null) {
				//TODO potentional cycle
				pw.println(dtoType.getSimpleName() + " " + RESULT_NAME + " = createDtoInstance(null);");
			} else {
				pw.println(dtoType.getSimpleName() + " " + RESULT_NAME + " = getDtoInstance(" + DOMAIN_NAME + "." + toHelper.toGetter(toHelper.toField(idMethod)) + ");");
			}

			pw.println();
		}
		
		@Override
		public void finish(TypeElement typeElement) {
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");
			pw.println();
		}
	}

	class CopyFromDtoPrinter extends AbstractElementPrinter {

		public CopyFromDtoPrinter(PrintWriter pw) {
			super(pw);
		}
		
		@Override
		public void print(ProcessorContext context) {
			copy(context, pw, new CopyFromDtoMethodPrinter());
		}
	
		@Override
		public void initialize(TypeElement typeElement) {
			
			ImmutableType dtoType = getDtoType(typeElement);
					
			TypeElement domainObjectClass = toHelper.getDomainTypeElement(typeElement);

			ExecutableElement idMethod = toHelper.getIdMethod(domainObjectClass);
			
			if (idMethod == null) {
				idMethod = toHelper.getIdMethod(typeElement);
			}
			
			if (idMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + typeElement.toString(), typeElement);
				return;
			}

			ImmutableType domainObjectType = (ImmutableType)getNameTypes().toType(domainObjectClass);
			
			pw.println("public " + domainObjectType.getSimpleName() + " createDomainInstance(" + Serializable.class.getSimpleName() + " id) {");
			
			printDomainInstancer(pw, domainObjectType);
			pw.println("}");
			pw.println();

			pw.println("protected " + Class.class.getSimpleName() + "<" + dtoType.getSimpleName() + "> getDtoClass() {");
			pw.println("return " + dtoType.getSimpleName() + ".class;");
			pw.println("}");
			pw.println();

			pw.println("public " + domainObjectClass.getSimpleName().toString() + " fromDto(" + dtoType.getSimpleName() + " " + DTO_NAME + ") {");
			pw.println();
			pw.println("if (" + DTO_NAME + " == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();
			
			ExecutableElement dtoIdMethod = toHelper.getDtoIdMethod(typeElement);
			
			if (dtoIdMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for DTO class " + dtoType.getCanonicalName(), typeElement);
				return;
			}
			
			if (dtoIdMethod == null) {
				//TODO potentional cycle
				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = createDomainInstance(null);");
			} else {
				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = getDomainInstance(" + DTO_NAME + "." + toHelper.toGetter(toHelper.toField(dtoIdMethod)) + ");");
			}
			pw.println();
		}
		
		@Override
		public void finish(TypeElement typeElement) {
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");
			pw.println();
		}
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(element, processingEnv);
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null) {
			return supportProcessorChain();
		}
		
		TypeElement dto = transferObjectConfiguration.getDto();
		if (dto != null) {
			return supportProcessorChain();
		}
		
		return super.processElement(element, roundEnv);
	}

	public static ImmutableType getOutputClass(ImmutableType mutableType, PackageValidatorProvider packageValidatorProvider, ProcessingEnvironment processingEnv) {	
		
		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration((TypeElement)((DeclaredType)mutableType.asType()).asElement(), processingEnv);
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null) {
			return null;
		}

		TypeElement dto = transferObjectConfiguration.getDto();
		if (dto != null) {
			return null;
		}

		//mutableType.changePackage(packageValidatorProvider.get(mutableType).moveTo(LocationType.SERVER));
		return mutableType.addClassSufix(DEFAULT_SUFFIX);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			NamedType domainObjectType = getNameTypes().toType(toHelper.getDomainTypeElement(typeElement));
			return new Type[] {
					TypedClassBuilder.get(CachedConverter.class, getDtoType(typeElement), domainObjectType)
			};
		}
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {

		ImmutableType dtoType = getDtoType(typeElement);				
		TypeElement domainObjectClass = toHelper.getDomainTypeElement(typeElement);

		TypeElement superElement = processingEnv.getElementUtils().getTypeElement(CachedConverter.class.getCanonicalName());
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(superElement.getEnclosedElements());

		List<Type> result = new ArrayList<Type>();
		
		for (ExecutableElement constructor: constructors) {
			for (VariableElement parameter: constructor.getParameters()) {
				result.add(getNameTypes().toType(parameter.asType()));
			}
		}
		
		result.add(dtoType);
		result.add(getNameTypes().toType(domainObjectClass));
		result.add(Serializable.class);

		return result.toArray(new Type[] {});
	}
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected ElementPrinter[] getElementPrinters(PrintWriter pw) {
		return new ElementPrinter[] {
				new CopyToDtoPrinter(pw),
				new CopyFromDtoPrinter(pw)
		};
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { 
				getOutputClass(mutableType, getPackageValidatorProvider(), processingEnv) };
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		printConstructor(outputName, pw);
		super.processElement(element, outputName, roundEnv, pw);
	}
	
	protected void printConstructor(NamedType outputName, PrintWriter pw) {
		TypeElement superElement = processingEnv.getElementUtils().getTypeElement(CachedConverter.class.getCanonicalName());
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(superElement.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			pw.print("public " + outputName.getSimpleName() + "(");
			int i = 0;
			for (VariableElement parameter: constructor.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
					pw.print(((DeclaredType)parameter.asType()).asElement().getSimpleName().toString() + " " + parameter.getSimpleName().toString());
				} else {
					pw.print(parameter.asType().toString() + " " + parameter.getSimpleName().toString());
				}
				i++;
			}
			pw.println(") {");
			pw.print("super(");
			i = 0;
			for (VariableElement parameter: constructor.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println("}");
			pw.println();
		}
	}
	
	protected void printConverterInstance(PrintWriter pw, NamedType type) {
		pw.print("new " + type.getCanonicalName() + "(cache)");
	}

	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.getSimpleName() + "();");
	}

	protected void printDtoInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.getSimpleName() + "();");
	}

	private boolean copy(ProcessorContext context, PrintWriter pw, CopyMethodPrinter printer) {
		
		TypeMirror domainType = context.getDomainMethodReturnType();
		
		switch (domainType.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			printer.printCopyMethod(context, pw, null);
			return true;
		case ERROR:
		case NONE:
		case NULL:
		case OTHER:
		case TYPEVAR:
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find erasure for the " + context.getDomainMethodReturnType().toString() + " in the method: " + context.getFieldName(), 
					context.getConfigurationElement());
			return false;
		case VOID:
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process " + context.getFieldName() + ". Unsupported result type: " + 
					domainType.getKind());
			return false;
		case ARRAY:
			return copyArray((ArrayType)domainType, context, pw, printer);
		case DECLARED:
			return copyDeclared((DeclaredType)domainType, context, pw, printer);
		default:
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process " + context.getFieldName() + ". Unsupported result type: " + 
					domainType.getKind());
			return false;
		}
	}
	
	private NamedType getDtoConverterType(TypeElement typeElement) {

		TypeElement converter = new TransferObjectConfiguration(typeElement, processingEnv).getConverter();

		if (converter != null) {
			return getNameTypes().toType(converter);
		}
		
		return null;	
	}

	private boolean copyDeclared(DeclaredType type, ProcessorContext context, PrintWriter pw, CopyMethodPrinter printer) {
		Element element = type.asElement();

		switch (element.getKind()) {
		case CLASS:
		case INTERFACE:
			TypeElement typeElement = (TypeElement)element;
			
			Element configurationElement = toHelper.getConfigurationElement(typeElement, roundEnv);
			
			if (configurationElement != null) {
				NamedType converterType = getDtoConverterType((TypeElement)configurationElement);
				
				if (converterType == null) {
					converterType = getTargetClassNames((ImmutableType)getNameTypes().toType(configurationElement))[0];
				}
				
				printer.printCopyMethod(context, pw, converterType);
			} else if (toHelper.isUnboxedType(type) || toHelper.isAllowedClass(type)) {
				printer.printCopyMethod(context, pw, null);
			}

			//TODO handle list, maps, ...
			break;
		case ENUM:
			printer.printCopyMethod(context, pw, null);
			break;			
		case OTHER:
		default:
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to process " + context.getFieldName() + ". Unsupported result type: " + type.getKind());
			return false;
		}
		return true;
	}

	private boolean copyArray(ArrayType arrayType, ProcessorContext context, PrintWriter pw, CopyMethodPrinter printer) {
		pw.println("{");

		NamedType convertType = toHelper.convertType(arrayType.getComponentType());
		
		//TODO add array support, don't forget to multidimensional arrays
		//pw.print(convertType.getCanoninalName() + "[] result = new " + convertType.getCanoninalName() + "[];");
		pw.println("}");
		return true;
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		//This method should be overriden by the specific implementations - like hibernate, appengine, etc.
		return false;
	}
}