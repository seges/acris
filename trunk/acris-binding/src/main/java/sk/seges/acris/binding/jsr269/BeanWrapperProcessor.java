/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.acris.binding.client.metadata.PropertyMetaDescriptor;
import sk.seges.acris.binding.client.metadata.SimpleClassMetaDescriptor;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.jsr269.AbstractConfigurableProcessor;

/**
 * Generates bean wrapper interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link sk.seges.acris.binding.client.annotations.BeanWrapper} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/bean-wrapper.properties file</li>
 * </ul>
 * Bean processor also generates constants for accessing each attribute in the bean in to order to write type-safe code.
 * Let's imagine that you have a bean User POJO with the following fields:
 * <ul>
 * <li>First name - represented by the property firstName</li>
 * <li>Last name - represented by the property lastName</li>
 * <li>Login - represented by the property login</li>
 * </ul>
 * and the BeanWrapper will contains following fields:
 * <ul>
 * <li>FIRST_NAME targeting "firstName" property</li>
 * <li>LAST_NAME targeting "lastName" property</li>
 * <li>LOGIN targeting "login" property</li>
 * </ul>
 * From now you can use constants from bean wrapper in order to reach type safe coding, e.g. UserBeanWrapper.FIRST_NAME
 * will reference User.getFirstName() method and when the property firstName will be renamed then also UserBeanWrapper
 * will be regenerated and you will get a compile error (when using the strings, determining the references is much more
 * harder)
 * 
 * @author eldzi
 * @author fat
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({BeanWrapperProcessor.CONFIG_FILE_LOCATION})
public class BeanWrapperProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";

	public static final String BEAN_CLASS_NAME = "class_";

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		annotations.add(sk.seges.acris.binding.client.annotations.BeanWrapper.class.getCanonicalName());
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	protected String getBeanWrapperName(String beanName) {
		return beanName + BEAN_WRAPPER_SUFFIX;
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		try {
			TypeElement typeElement = (TypeElement) element;

			Name beanElementName = ((TypeElement) element).getQualifiedName();
			
			String beanPackageName = beanElementName.toString().substring(0, beanElementName.toString().lastIndexOf("."));

			Element enclosingElement = element.getEnclosingElement();
			while (enclosingElement != null) {
				if (!enclosingElement.getKind().equals(ElementKind.CLASS) && !enclosingElement.getKind().equals(ElementKind.INTERFACE)) {
					enclosingElement = null;
				} else {
					String enclosingName = ((TypeElement) enclosingElement).getQualifiedName().toString();
					beanPackageName = enclosingName.substring(0, enclosingName.lastIndexOf("."));
					enclosingElement = enclosingElement.getEnclosingElement();
				}
			}

			String simpleName = getBeanWrapperName(beanElementName.toString().replace(beanPackageName, "").replace(".", ""));

			JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(beanPackageName + "." + simpleName, element);
			OutputStream os = createSourceFile.openOutputStream();
			PrintWriter pw = new PrintWriter(os);

			pw.println("package " + beanPackageName + ";");
			pw.println();

			for (String importName : getImports()) {
				pw.println("import " + importName + ";");
			}

			pw.println("@" + Generated.class.getCanonicalName() + "(\"" + BeanWrapperProcessor.class.getCanonicalName() + "\")");
			pw.println("public interface " + simpleName + " extends " + BeanWrapper.class.getCanonicalName() + "<" + beanElementName.toString() + "> {");
			pw.println();

			TypeElement el = typeElement;

			HashSet<String> types = new HashSet<String>();
			
			writeFieldsFromClass(types, pw, el);

			while (el.getSuperclass() != null) {
				if (el.getSuperclass() instanceof DeclaredType) {
					el = (TypeElement) ((DeclaredType) el.getSuperclass()).asElement();

					writeFieldsFromClass(types, pw, el);
				} else {
					break;
				}
			}

			pw.println("}");
			pw.flush();
			pw.close();

		} catch (IOException e) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process element = ", element);
		}

		return true;
	}

	protected String[] appendImport(String[] imports, String importName) {
		List<String> result = new ArrayList<String>();

		for (String inputImport : imports) {
			if (inputImport.equals(importName)) {
				return imports;
			}
			result.add(inputImport);
		}

		result.add(importName);

		return result.toArray(new String[] {});
	}

	protected String[] getImports() {
		return new String[] {BeanWrapper.class.getName(), SimpleClassMetaDescriptor.class.getName(), PropertyMetaDescriptor.class.getName()};
	}

	private TypeMirror getMethodReturnType(String name, Element classElement) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(classElement.getEnclosedElements());

		for (ExecutableElement method : methods) {
			if (method.toString().equals(name)) {
				return method.getReturnType();
			}
		}

		return null;
	}

	private String createConstant(String name) {

		String result = "";

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				result += "_";
			}
			result += c;
		}

		return result.toUpperCase();
	}

	private void writeFieldsFromClass(Set<String> types, PrintWriter pw, Element element) {
		writeFieldsFromClass(types, pw, element, "", 1);
	}
	
	@SuppressWarnings("unchecked")
	private void writeFieldsFromClass(Set<String> types, PrintWriter pw, Element element, String prefix, int level) {
		List<? extends Element> fieldsOfClass = ElementFilter.fieldsIn(element.getEnclosedElements());

		for (Element fieldElement : fieldsOfClass) {
			if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
				continue;
			}

			TypeMirror typeMirror = fieldElement.asType();
			String fieldName = fieldElement.getSimpleName().toString();
			if (fieldName.length() > 0) {
				String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "()";
				TypeMirror returnType = getMethodReturnType(getter, element);
				if (returnType != null) {
					typeMirror = returnType;
				}
			}

			String fieldSimpleName = fieldElement.getSimpleName().toString();

			boolean interfaceGenerated = false;
			
			if (!types.contains(fieldSimpleName)) {
				//cycle not detected
				if (!typeMirror.getKind().isPrimitive()) {
					if ( typeMirror.getKind() == TypeKind.DECLARED ) {
						final Element classElement = ((DeclaredType)typeMirror).asElement();
						TypeElement classTypeElement = (TypeElement) classElement;
						for (String annotationName : annotations) {
							try {
								Annotation annotation = classTypeElement.getAnnotation((Class<Annotation>)Class.forName(annotationName));
								if (annotation != null) {
									//this is supported bean wrapper class
										types.add(fieldSimpleName);
										pw.println(indent("public static interface " + createConstant(fieldSimpleName) + " {", level));
										pw.println();
										pw.println(indent("public static " + String.class.getSimpleName() + " THIS = \"" + prefix + fieldElement.getSimpleName() + "\";", level + 1));
										pw.println();
										writeFieldsFromClass(types, pw, classTypeElement, prefix + fieldElement.getSimpleName() + ".", level + 1);
										pw.println(indent("}", level));
										pw.println();
										interfaceGenerated = true;
										break;
								}
							} catch (ClassNotFoundException e) {
								processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find annotation " + annotationName + " on the class path", element);
							}
						}
					}
				}
			}

			if (!interfaceGenerated) {
				pw.println(indent("public static " + String.class.getSimpleName() + " " + createConstant(fieldSimpleName) + " = \""
						+ prefix + fieldElement.getSimpleName() + "\";", level));
				pw.println();
			}
		}
	}

	private String indent(String text, int level) {
		String result = "";
		
		for (int i = 0; i < level; i++) {
			result += "	";
		}
		return result + text;
	}
}