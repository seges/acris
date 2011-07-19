package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.annotation.Converter;

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
	
					ExecutableElement domainGetterMethod = getDomainGetterMethod(element, currentPath);
					
					if (domainGetterMethod == null) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find getter method for field " + currentPath + " in the " + element.toString(), context.getConfigurationElement());
						return;
					}
	
					if (!instances.contains(fullPath)) {
						//TODO check if getId is null

						if (domainGetterMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
							Element referenceElement = ((DeclaredType)domainGetterMethod.getReturnType()).asElement();

							Element configurationElement = getConfigurationElement((TypeElement)referenceElement, roundEnv);

							if (configurationElement == null) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find conversion configuration for type " + referenceElement.toString(), context.getConfigurationElement());
								return;
							}
							
							pw.print(getNameTypes().toType(referenceElement) + " " + currentPath + " = ");
							printConverterInstance(pw, getOutputClass(getNameTypes().toType(configurationElement), getPackageValidatorProvider()));
							//printDomainInstancer(pw, , currentPath, getIdMethod(referenceElement));
							pw.println(".createDomainInstance(" + DTO_NAME + "." + toGetter(fullPath + toMethod(toField(getIdMethod(referenceElement)))) + ");");
							instances.add(fullPath);
						}
						pw.println(previousPath + "." + toSetter(currentPath) + "(" + currentPath + ");");
					}

					previousPath = currentPath;
					currentPath = pathResolver.next();
					fullPath += toMethod(currentPath);
				}

				if (getDomainSetterMethod(element, context.getDomainFieldPath()) != null) {
					pw.println(RESULT_NAME + "." + toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME + "." + toGetter(context.getFieldName()) + ");");					
				} else {
					ExecutableElement domainGetterMethod = getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && isIdField(currentPath)) || !isIdMethod(domainGetterMethod)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationElement());
					}
				}
			} else {
				boolean generated = true;
				if (converterType != null) {
					String converterName = "converter" + toMethod("", context.getFieldName());
					pw.println(converterType.getCanonicalName() + " " + converterName + " = new " + converterType.getCanonicalName() + "();");
					pw.print(RESULT_NAME + "." + toSetter(context.getDomainFieldPath()) + "(" + converterName + ".fromDto(" + DTO_NAME  + "." + toGetter(context.getFieldName()));
				} else {
					ExecutableElement domainGetterMethod = getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && isIdField(currentPath)) || !isIdMethod(domainGetterMethod)) {
						pw.print(RESULT_NAME + "." + toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME  + "." + toGetter(context.getFieldName()));
					} else {
						if ((domainGetterMethod == null && isIdField(currentPath)) || !isIdMethod(domainGetterMethod)) {
							processingEnv.getMessager().printMessage(Kind.ERROR, "Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationElement());
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
						methodPath += "." + toGetter(path);
						
						pw.print(methodPath + " != null");
						i++;
					}
				}
				pw.println(") {");
			} 

			if (converterType != null) {
				String converterName = "converter" + toMethod("", context.getFieldName());
				pw.println(converterType.getCanonicalName() + " " + converterName + " = new " + converterType.getCanonicalName() + "();");
				pw.print(RESULT_NAME + "." + toSetter(context.getFieldName()) + "(" + converterName + ".toDto(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
			} else {
				pw.print(RESULT_NAME + "." + toSetter(context.getFieldName()) + "(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
			}
			
			if (converterType != null) {
				pw.print(")");
			}
			pw.println(");");

			if (nested) {
				pw.println("} else {");
				pw.println(RESULT_NAME + "." + toSetter(context.getFieldName()) + "(null);");
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
			
			MutableType dtoType = getDtoType(typeElement);
					
			TypeElement domainObjectClass = getDomainTypeElement(typeElement);

			pw.println("public " + dtoType.getSimpleName() + " createDtoInstance() {");
			printDtoInstancer(pw, dtoType);
			pw.println("}");
			pw.println();
			pw.println("public " + dtoType.getSimpleName() + " toDto(" + domainObjectClass.getSimpleName().toString() + " " + DOMAIN_NAME + ") {");
			pw.println();
			pw.println("if (" + DOMAIN_NAME + "  == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();

//			ExecutableElement dtoIdMethod = getDtoIdMethod(typeElement);
//			
//			if (dtoIdMethod == null) {
//				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find id method for DTO class " + dtoType.getCanonicalName(), typeElement);
//				return;
//			}

			pw.println(dtoType.getSimpleName() + " " + RESULT_NAME + " = createDtoInstance();");
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
			
			MutableType dtoType = getDtoType(typeElement);
					
			TypeElement domainObjectClass = getDomainTypeElement(typeElement);

			ExecutableElement idMethod = getIdMethod(domainObjectClass);
			
			if (idMethod == null) {
				idMethod = getIdMethod(typeElement);
			}
			
			if (idMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find id method for " + typeElement.toString(), typeElement);
				return;
			}

			MutableType domainObjectType = getNameTypes().toType(domainObjectClass);
			
			if (idMethod == null) {
				pw.println("public " + domainObjectType.getCanonicalName() + " createDomainInstance() {");
			} else {
				pw.println("public " + domainObjectType.getCanonicalName() + " createDomainInstance(" + 
						getNameTypes().toType(erasure(domainObjectClass, idMethod.getReturnType())).getCanonicalName() + " id) {");
			}
			
			printDomainInstancer(pw, domainObjectType);
			pw.println("}");
			pw.println("");

			pw.println("public " + domainObjectClass.getSimpleName().toString() + " fromDto(" + dtoType.getSimpleName() + " " + DTO_NAME + ") {");
			pw.println();
			pw.println("if (" + DTO_NAME + " == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();
			
			ExecutableElement dtoIdMethod = getDtoIdMethod(typeElement);
			
			if (dtoIdMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find id method for DTO class " + dtoType.getCanonicalName(), typeElement);
				return;
			}
			
			if (dtoIdMethod == null) {
				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = createDomainInstance();");
			} else {
				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = createDomainInstance(" + DTO_NAME + "." + toGetter(toField(dtoIdMethod)) + ");");
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

	public static MutableType getOutputClass(MutableType mutableType, PackageValidatorProvider packageValidatorProvider) {	
		//mutableType.changePackage(packageValidatorProvider.get(mutableType).moveTo(LocationType.SERVER));
		return mutableType.addClassSufix(DEFAULT_SUFFIX);
	}

	//TODO add interface TransferObjectConverter<T, S>
	@Override
	protected Type[] getImports(TypeElement typeElement) {

		MutableType dtoType = getDtoType(typeElement);				
		TypeElement domainObjectClass = getDomainTypeElement(typeElement);

		return new Type[] {
				dtoType, getNameTypes().toType(domainObjectClass)
		};
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
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		if (element.getAnnotation(Converter.class) != null) {
			return supportProcessorChain();
		}
		return super.processElement(element, roundEnv);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {

		return new NamedType[] { 
				getOutputClass(mutableType, getPackageValidatorProvider()) };

//		return new NamedType[] { 
//				getOutputClass(getNameTypes().toType(
//						getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))),
//							getPackageValidatorProvider()) };
	}

	protected void printConverterInstance(PrintWriter pw, NamedType type) {
		pw.print("new " + type.getCanonicalName() + "()");
	}

	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.getCanonicalName() + "();");
	}

	protected void printDtoInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.getCanonicalName() + "();");
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
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find erasure for the " + context.getDomainMethodReturnType().toString() + " in the method: " + context.getFieldName(), 
					context.getConfigurationElement());
			return false;
		case VOID:
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process " + context.getFieldName() + ". Unsupported result type: " + 
					domainType.getKind());
			return false;
		case ARRAY:
			return copyArray((ArrayType)domainType, context, pw, printer);
		case DECLARED:
			return copyDeclared((DeclaredType)domainType, context, pw, printer);
		default:
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process " + context.getFieldName() + ". Unsupported result type: " + 
					domainType.getKind());
			return false;
		}
	}
	
	private MutableType getDtoConverterType(TypeElement typeElement) {
		Converter converterAnnotation = typeElement.getAnnotation(Converter.class);
		
		if (converterAnnotation != null) {
			return getNameTypes().toType(AnnotationClassPropertyHarvester.getTypeOfClassProperty(converterAnnotation, new AnnotationClassProperty<Converter>() {

				@Override
				public Class<?> getClassProperty(Converter converter) {
					return converter.value();
				}
				
			}));
		}
		
		return null;	
	}

	private boolean copyDeclared(DeclaredType type, ProcessorContext context, PrintWriter pw, CopyMethodPrinter printer) {
		Element element = type.asElement();

		switch (element.getKind()) {
		case CLASS:
		case INTERFACE:
			TypeElement typeElement = (TypeElement)element;
			
			Element configurationElement = getConfigurationElement(typeElement, roundEnv);
			
			if (configurationElement != null) {
				NamedType converterType = getDtoConverterType((TypeElement)configurationElement);
				
				if (converterType == null) {
					converterType = getTargetClassNames(getNameTypes().toType(configurationElement))[0];
				}
				
				printer.printCopyMethod(context, pw, converterType);
			} else if (isUnboxedType(type) || isAllowedClass(type)) {
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

		NamedType convertType = convertType(arrayType.getComponentType());
		
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