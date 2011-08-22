package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.mutable.MutableVariableElement;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.AbstractElementPrinter;
import sk.seges.sesam.pap.model.model.ElementPrinter;
import sk.seges.sesam.pap.model.model.ProcessorContext;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;
import sk.seges.sesam.shared.model.converter.CachedConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectConvertorProcessor extends AbstractTransferProcessor {

	private static final String DEFAULT_SUFFIX = "Converter";	
	private static final String DEFAULT_CONFIGURATION_SUFFIX = "Configuration";
	private static final String RESULT_NAME = "_result";
	private static final String DOMAIN_NAME = "_domain";
	private static final String DTO_NAME = "_dto";
	
	private List<String> parameterNames = new ArrayList<String>();
	
	private Set<String> instances = new HashSet<String>();

	interface CopyMethodPrinter {

		void printCopyMethod(ProcessorContext context, PrintWriter pw, NamedType converterType);
	}

	class CopyFromDtoMethodPrinter implements CopyMethodPrinter {

		private void printCollectionCastIfNecessary(ProcessorContext context, PrintWriter pw) {
			if (ProcessorUtils.isCollection(context.getDomainMethodReturnType(), processingEnv)) {

				DeclaredType declaredList = ((DeclaredType)context.getDomainMethodReturnType());
				
				if (declaredList.getTypeArguments() != null && declaredList.getTypeArguments().size() == 1) {
					TypeMirror typeMirror = declaredList.getTypeArguments().get(0);
					HasTypeParameters modifiedList = TypedClassBuilder.get(nameTypesUtils.toType(processingEnv.getTypeUtils().erasure(declaredList)), nameTypesUtils.toType(typeMirror));
					pw.print("(" + modifiedList.toString(null, ClassSerializer.CANONICAL, true) + ")");
				} else {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type " + context.getDomainMethodReturnType().toString() +
							" in the method " + context.getDomainMethod().getSimpleName().toString() + " should have " +
							" defined a type parameter", context.getConfigurationElement());
				}
			}
			//TODO handle maps
		}

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
							
							if (toHelper.getIdMethod(referenceElement) != null) {
								pw.print(getNameTypes().toType(referenceElement) + " " + currentPath + " = ");
								printConverterInstance(pw, getOutputClass((ImmutableType)getNameTypes().toType(configurationElement), getPackageValidatorProvider(), processingEnv), context.getConfigurationElement());
								pw.println(".getDomainInstance(" + DTO_NAME + "." + methodHelper.toGetter(fullPath + methodHelper.toMethod(methodHelper.toField(toHelper.getIdMethod(referenceElement)))) + ");");
								instances.add(fullPath);
							} else {
								pw.println(getNameTypes().toType(referenceElement) + " " + currentPath + " = " + RESULT_NAME + "." + methodHelper.toGetter(currentPath) + ";");
								pw.println("if (" + RESULT_NAME + "." + methodHelper.toGetter(currentPath) + " == null) {");
								pw.print(currentPath + " = ");
								printConverterInstance(pw, getOutputClass((ImmutableType)getNameTypes().toType(configurationElement), getPackageValidatorProvider(), processingEnv), context.getConfigurationElement());
								pw.println(".createDomainInstance(null);");
								instances.add(fullPath);
								pw.println("}");
								
							}
						}
						
						if (instances.contains(currentPath)) {
							pw.println(previousPath + "." + methodHelper.toSetter(currentPath) + "(" + currentPath + ");");
						}
					}

					previousPath = currentPath;
					currentPath = pathResolver.next();
					fullPath += methodHelper.toMethod(currentPath);
				}

				if (toHelper.getDomainSetterMethod(element, context.getDomainFieldPath()) != null) {
					if (converterType != null) {
						String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
						pw.print(converterType.getCanonicalName() + " " + converterName + " = ");
						printConverterInstance(pw, converterType, context.getConfigurationElement());
						pw.println(";");
						//TODO printCollectionCastIfNecessary
						pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "("+ converterName + ".fromDto(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
					} else {
						pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME + "." + methodHelper.toGetter(context.getFieldName()));
					}
					if (converterType != null) {
						pw.print(")");
					}
					pw.println(");");
				} else {
					ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && toHelper.isIdField(currentPath)) || !toHelper.isIdMethod(domainGetterMethod)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationElement());
					}
				}
			} else {
				boolean generated = true;
				if (converterType != null) {
					String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
					pw.print(converterType.getCanonicalName() + " " + converterName + " = ");
					printConverterInstance(pw, converterType, context.getConfigurationElement());
					pw.println(";");
					pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(");
					printCollectionCastIfNecessary(context, pw);
					pw.print(converterName + ".fromDto(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
				} else {
					ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
					if ((domainGetterMethod == null && toHelper.isIdField(currentPath)) || !toHelper.isIdMethod(domainGetterMethod)) {
						pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
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

		private void printCastToCollection(ProcessorContext context, PrintWriter pw) {
			if (ProcessorUtils.implementsType(context.getDomainMethodReturnType(), processingEnv.getElementUtils() .getTypeElement(Collection.class.getCanonicalName()).asType())) {
				
				DeclaredType declaredList = ((DeclaredType)context.getDomainMethodReturnType());

				//TODO use getTargetEntity for the return type
				if (declaredList.getTypeArguments() != null && declaredList.getTypeArguments().size() == 1) {

					if (context.getFieldType() instanceof HasTypeParameters) {
						TypeParameter typeParameter = ((HasTypeParameters)context.getFieldType()).getTypeParameters()[0];
						HasTypeParameters modifiedList = TypedClassBuilder.get(nameTypesUtils.toType(processingEnv.getTypeUtils().erasure(declaredList)), typeParameter);
						pw.print("(" + modifiedList.toString(null, ClassSerializer.CANONICAL, true) + ")");
					};
					
				} else {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type " + context.getDomainMethodReturnType().toString() +
							" in the method " + context.getDomainMethod().getSimpleName().toString() + " should have " +
							" defined a type parameter", context.getConfigurationElement());
				}
			}
			//TODO handle maps
		}
		
		private void printCastToDomainType(ProcessorContext context, PrintWriter pw) {
			
			TypeMirror domainReturnType = getTargetEntityType(context.getDomainMethod());

			if (!processingEnv.getTypeUtils().isSameType(processingEnv.getTypeUtils().erasure(context.getDomainMethod().getReturnType()),
														processingEnv.getTypeUtils().erasure(domainReturnType))) {
				pw.print("(" + context.getDomainTypeElement().toString() +")");
			}
		}
				
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
						methodPath += "." + methodHelper.toGetter(path);
						
						pw.print(methodPath + " != null");
						i++;
					}
				}
				pw.println(") {");
			} 

			if (converterType != null) {
				String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
				
				pw.print(converterType.getCanonicalName() + " " + converterName + " = ");
				printConverterInstance(pw, converterType, context.getConfigurationElement());
				pw.println(";");

				pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(");
				printCastToCollection(context, pw);
				pw.print(converterName + ".toDto(");
				printCastToDomainType(context, pw);
				pw.print(DOMAIN_NAME  + "." + context.getDomainFieldName());
			} else {
				pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
			}
			
			if (converterType != null) {
				pw.print(")");
			}
			pw.println(");");

			if (nested) {
				pw.println("} else {");
				pw.println(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(null);");
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
		public void initialize(TypeElement configurationElement) {
			
			ImmutableType dtoType = getDtoType(configurationElement);
					
			TypeElement domainObjectClass = toHelper.getDomainTypeElement(configurationElement);

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
				idMethod = toHelper.getIdMethod(configurationElement);
			}
			
			if (idMethod == null && shouldHaveIdMethod(configurationElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement);
				return;
			}

			if (idMethod == null) {
				//TODO potential cycle
				pw.println(dtoType.getSimpleName() + " " + RESULT_NAME + " = createDtoInstance(null);");
			} else {
								
				boolean useIdConverter = false;

				pw.print(dtoType.getSimpleName() + " " + RESULT_NAME + " = getDtoInstance(");

				if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					TypeElement domainType = (TypeElement)((DeclaredType)idMethod.getReturnType()).asElement();
					Element idConfigurationElement = toHelper.getConfigurationElement(domainType, roundEnv);
					NamedType idConverter = getDomainConverter(domainType);
					if (idConverter != null) {
						printConverterInstance(pw, idConverter, (TypeElement)idConfigurationElement);
						pw.print(".toDto(");
						useIdConverter = true;
					}
				}
				
				pw.print(DOMAIN_NAME + "." + methodHelper.toGetter(methodHelper.toField(idMethod)) + ")");

				if (useIdConverter) {
					pw.print(")");
				}
				pw.println(";");
			}

			pw.println("return convertToDto(" + RESULT_NAME + ", " + DOMAIN_NAME + ");");
			pw.println("}");
			pw.println();
			pw.println("public " + dtoType.getSimpleName() + " convertToDto(" + dtoType.getSimpleName() + " " + RESULT_NAME + ", " + domainObjectClass.getSimpleName().toString() + " " + DOMAIN_NAME + ") {");
			pw.println();
			pw.println("if (" + DOMAIN_NAME + "  == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();

			NamedType dtoSuperclass = toHelper.getDtoSuperclass(configurationElement);
			if (dtoSuperclass != null) {
				TypeElement superClassElement = (TypeElement)((DeclaredType)domainObjectClass.getSuperclass()).asElement();
				
				Element superClassConfigurationElement = toHelper.getConfigurationElement(superClassElement, roundEnv);
				
				NamedType domainConverter = getDomainConverter(superClassElement);
				
				if (domainConverter != null && superClassConfigurationElement != null) {
					printConverterInstance(pw, domainConverter, (TypeElement)superClassConfigurationElement);
					pw.println(".convertToDto(" + RESULT_NAME + ", " + DOMAIN_NAME + ");");
					pw.println();
				}
			}
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
		public void initialize(TypeElement configurationElement) {
			
			ImmutableType dtoType = getDtoType(configurationElement);
					
			TypeElement domainObjectClass = toHelper.getDomainTypeElement(configurationElement);

			ExecutableElement idMethod = toHelper.getIdMethod(domainObjectClass);
			
			if (idMethod == null) {
				idMethod = toHelper.getIdMethod(configurationElement);
			}
			
			if (idMethod == null && shouldHaveIdMethod(configurationElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement);
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
			
			ExecutableElement dtoIdMethod = toHelper.getDtoIdMethod(configurationElement);
			
			if (dtoIdMethod == null && shouldHaveIdMethod(configurationElement, domainObjectClass)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for DTO class " + dtoType.getCanonicalName(), configurationElement);
				return;
			}
			
			if (dtoIdMethod == null) {
				//TODO potential cycle
				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = createDomainInstance(null);");
			} else {
				
				boolean useIdConverter = false;

				pw.println(domainObjectType.getCanonicalName() + " " + RESULT_NAME + " = getDomainInstance(");

				if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					TypeElement domainType = (TypeElement)((DeclaredType)idMethod.getReturnType()).asElement();
					Element idConfigurationElement = toHelper.getConfigurationElement(domainType, roundEnv);
					NamedType idConverter = getDomainConverter(domainType);
					if (idConverter != null) {
						printConverterInstance(pw, idConverter, (TypeElement)idConfigurationElement);
						pw.print(".fromDto(");
						useIdConverter = true;
					}
				}
				
				pw.print(DTO_NAME + "." + methodHelper.toGetter(methodHelper.toField(dtoIdMethod)) + ")");

				if (useIdConverter) {
					pw.print(")");
				}
				pw.println(";");
			}
			pw.println();
			pw.println("return convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
			pw.println("}");
			pw.println();
			pw.println("public " + domainObjectClass.getSimpleName().toString() + " convertFromDto(" + domainObjectClass.getSimpleName().toString() + " " + RESULT_NAME + ", " + dtoType.getSimpleName() + " " + DTO_NAME + ") {");
			pw.println();
			pw.println("if (" + DTO_NAME + "  == null) {");
			pw.println("return null;");
			pw.println("}");
			pw.println();

			NamedType dtoSuperclass = toHelper.getDtoSuperclass(configurationElement);
			if (dtoSuperclass != null) {
				TypeElement superClassElement = (TypeElement)((DeclaredType)domainObjectClass.getSuperclass()).asElement();
				
				Element superClassConfigurationElement = toHelper.getConfigurationElement(superClassElement, roundEnv);
				
				NamedType domainConverter = getDomainConverter(superClassElement);
				
				if (domainConverter != null && superClassConfigurationElement != null) {
					printConverterInstance(pw, domainConverter, (TypeElement)superClassConfigurationElement);
					pw.println(".convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
					pw.println();
				}
			}
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

		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(DEFAULT_CONFIGURATION_SUFFIX) && simpleName.length() > DEFAULT_CONFIGURATION_SUFFIX.length()) {
			simpleName = simpleName.substring(0, simpleName.lastIndexOf(DEFAULT_CONFIGURATION_SUFFIX));
			mutableType = mutableType.setName(simpleName);
		}
		
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
		
		for (MutableVariableElement element: getAdditionalConstructorParameters()) {
			result.add(getNameTypes().toType(element.asType()));
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
	protected NamedType[] getTargetClassNames(ImmutableType immutableType) {
		return new NamedType[] { 
				getOutputClass(immutableType, getPackageValidatorProvider(), processingEnv) };
	}

	protected MutableVariableElement[] getAdditionalConstructorParameters() {
		return new MutableVariableElement[] {};
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(CachedConverter.class.getCanonicalName());
		
		methodHelper.copyConstructors(outputName, cachedConverterType, pw, getAdditionalConstructorParameters());
		
		Map<ExecutableElement, List<String>> constructorParameters = toHelper.getConverterParameterNames(cachedConverterType, getAdditionalConstructorParameters());
		
		if (constructorParameters.size() == 0) {
			this.parameterNames = new ArrayList<String>();
		} else {
			this.parameterNames = constructorParameters.values().iterator().next();
		}

		for (MutableVariableElement parameter: getAdditionalConstructorParameters()) {
			pw.println("private " + parameter.asType() + " " + parameter.getSimpleName().toString() + ";");
			pw.println();
		}
		
		super.processElement(element, outputName, roundEnv, pw);
	}
	
	//TODO remove type parameter
	protected void printConverterInstance(PrintWriter pw, NamedType type, TypeElement configurationElement) {
				
		TypeElement converterType = processingEnv.getElementUtils().getTypeElement(type.getCanonicalName());

		List<String> selectedParameterNames = this.parameterNames;

		if (converterType != null) {
		
			TypeElement domainElement = new TransferObjectConfiguration(configurationElement, processingEnv).getDomainType();
			
			if (domainElement != null && toHelper.isDelegateConfiguration(configurationElement)) {
				domainElement = new TransferObjectConfiguration(new TransferObjectConfiguration(configurationElement, processingEnv).getConfiguration(), processingEnv).getDomain();
			}

			NamedType domainConverter = null;
			
			if (domainElement != null) {
				domainConverter = getDomainConverter(domainElement);
			}
			
			if (domainConverter != null && !domainConverter.getQualifiedName().equals(converterType.getQualifiedName().toString())) {
				Map<ExecutableElement, List<String>> constructorParameters = toHelper.getConverterParameterNames(converterType);
	
				if (constructorParameters.size() > 0) {
					selectedParameterNames = constructorParameters.values().iterator().next();
				}
			}
		}

		pw.print("new " + type.getCanonicalName() + "(");
		
		int i = 0;
		
		for (String parameterName: selectedParameterNames) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(parameterName);
			i++;
		}
		
		pw.print(")");
		
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

	//TODO Move this method to toHelper
	private NamedType getDomainConverter(TypeElement domainElement) {
		NamedType converterType = null;
		
		Element configurationElement = toHelper.getConfigurationElement(domainElement, roundEnv);

		if (configurationElement != null) {

			TypeElement delegateConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv).getConfiguration();
			
			if (delegateConfiguration != null) {
				configurationElement = delegateConfiguration;
			}

			converterType = getDtoConverterType((TypeElement)configurationElement);
			
			if (converterType == null) {
				converterType = getTargetClassNames((ImmutableType)getNameTypes().toType(configurationElement))[0];
			}
		}
		
		return converterType;
	}

	private TypeElement getTypeParameter(DeclaredType type) {
		if (type.getTypeArguments() != null && type.getTypeArguments().size() == 1) {
			
			TypeMirror typeParameter = type.getTypeArguments().get(0);
			
			if (typeParameter.getKind().equals(TypeKind.DECLARED)) {
				return (TypeElement)((DeclaredType)typeParameter).asElement();
			}
		}
		
		return null;
	}
	
	private boolean copyDeclared(DeclaredType type, ProcessorContext context, PrintWriter pw, CopyMethodPrinter printer) {
		Element element = type.asElement();

		switch (element.getKind()) {
		case CLASS:
		case INTERFACE:
			TypeElement typeElement = (TypeElement)element;
			
			if (ProcessorUtils.isCollection(type, processingEnv)) {
				TypeElement typeParameter = getTypeParameter(type);
				if (typeParameter == null) {
					processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + type.toString() +
							" should have defined a type parameter", context.getConfigurationElement());
				} else {
					typeElement = typeParameter;
				}
			} else if (ProcessorUtils.isPagedResult(type, processingEnv)) {
				TypeElement typeParameter = getTypeParameter(type);
				if (typeParameter == null) {
					processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + type.toString() +
							" should have defined a type parameter", context.getConfigurationElement());
				} else {
					if (ProcessorUtils.isCollection(typeParameter.asType(), processingEnv)) {
						TypeElement collectionTypeParameter = getTypeParameter((DeclaredType)typeParameter.asType());
						if (collectionTypeParameter == null) {
							processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + typeParameter.asType() +
									" should have defined a type parameter (originally used in the " + type.toString() + ")", 
									context.getConfigurationElement());
						} else {
							typeElement = collectionTypeParameter;
						}
					} else {
						//TODO handle paged result that does not hold a collection of objects
					}
				}
			}

			//reads TransferObjectConfiguration annotation from method in the configuration
			TypeElement converterTypeElement =  new TransferObjectConfiguration(context.getMethod(), processingEnv).getConverter();
			NamedType converterType = null;

			if (converterTypeElement != null) {
				converterType = nameTypesUtils.toType(converterTypeElement);
			}

			if (converterType != null) {
				printer.printCopyMethod(context, pw, converterType);
			} else {
				printer.printCopyMethod(context, pw, getDomainConverter(typeElement));
			}
			//TODO handle maps
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

	/**
	 * This method should be overridden by the specific implementations - like hibernate, appengine, etc.
	 */
	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return false;
	}
}