package sk.seges.acris.core.jsr269;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

public abstract class AbstractConfigurableProcessor extends AbstractProcessor {

	public static final String CONFIG_FILE_LOCATION = "configLocation";
	protected static final String PATH_SEPARATOR = "/";

	protected Set<String> annotations = null;
	protected Set<String> interfaces = null;
	protected Set<Element> elements = new HashSet<Element>();

	protected abstract String getConfigurationFileLocation();

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);

		String location = processingEnv.getOptions().get(CONFIG_FILE_LOCATION);
		if (location == null) {
			location = getConfigurationFileLocation();
		}
		Properties prop = new Properties();
		try {
			prop.load(getInputStreamForResource(location));
		} catch (IOException e) {
			throw new RuntimeException("Unable to initialize.", e);
		}

		annotations = new HashSet<String>();
		
		String annotationsString = (String) prop.get("annotations");
		if (annotationsString != null) {
			annotations.addAll(parse(annotationsString));
		}
		
		String interfacesString = (String) prop.get("interfaces");
		
		if (interfacesString != null) {
			interfaces = parse(interfacesString);
		}
	}

	protected abstract boolean processElement(Element element, RoundEnvironment roundEnv);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
	 * javax.annotation.processing.RoundEnvironment)
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			// finally process gathered elements and create java files
			for (Element element : elements) {
				processElement(element, roundEnv);
			}
		} else {
			for (String annotation : this.annotations) {
				try {
					Set<? extends Element> els = roundEnv.getElementsAnnotatedWith((Class<Annotation>)Class.forName(annotation));
					for (Element element : els) {
						if (!element.getKind().equals(ElementKind.CLASS) && !element.getKind().equals(ElementKind.INTERFACE)) {
							continue;
						}
						elements.add(element);
					}
				} catch (ClassNotFoundException e) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process find annotation " + annotation);
				}
			}
			for (Element element : roundEnv.getRootElements()) {
				if (!element.getKind().equals(ElementKind.CLASS) && !element.getKind().equals(ElementKind.INTERFACE)) {
					continue;
				}
				TypeElement typeElement = (TypeElement) element;
				if (isAssignable(typeElement)) {
					elements.add(element);
				}
			}
		}

		return true;
	}

	private boolean hasAnnotation(TypeElement typeElement) {
		if (annotations == null) {
			return false;
		}

		List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
		for (AnnotationMirror mirror : annotationMirrors) {
			String mirrorFqn = ((TypeElement) mirror.getAnnotationType().asElement()).getQualifiedName().toString();
			if (annotations.contains(mirrorFqn)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAssignable(TypeElement typeElement) {
		if (interfaces == null) {
			return false;
		}

		for (String iface : interfaces) {
			if (isAssignableFrom(typeElement, iface)) {
				return true;
			}
		}

		return false;
	}

	protected void log(String msg) {
		processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, msg);
	}

	private boolean isAssignableFrom(TypeElement typeElement, String type) {
		boolean result;

		result = isOfType(typeElement, type);
		if (result == true) {
			return true;
		}

		List<? extends TypeMirror> interfaces2 = typeElement.getInterfaces();
		TypeMirror superclass = typeElement.getSuperclass();

		result = isAssignable(superclass, type);
		if (result == true) {
			return true;
		}

		for (TypeMirror mirror : interfaces2) {
			if (isAssignable(mirror, type)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAssignable(TypeMirror mirror, String type) {
		if (mirror instanceof DeclaredType) {
			DeclaredType dt = (DeclaredType) mirror;
			TypeElement dte = (TypeElement) dt.asElement();

			if (isOfType(dte, type)) {
				return true;
			} else {
				return isAssignableFrom(dte, type);
			}
		}
		return false;
	}

	private boolean isOfType(TypeElement te, String type) {
		return te.getQualifiedName().equals(type);
	}

	private InputStream getInputStreamForResource(String resource) {
		String pkg = getPackage(resource);
		String name = getRelativeName(resource);
		InputStream ormStream;
		try {
			FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, pkg, name);
			ormStream = fileObject.openInputStream();
		} catch (IOException e1) {
			// TODO - METAGEN-12
			// unfortunately, the Filer.getResource API seems not to be able to
			// load from /META-INF. One gets a
			// FilerException with the message with "Illegal name /META-INF".
			// This means that we have to revert to
			// using the classpath. This might mean that we find a
			// persistence.xml which is 'part of another jar.
			// Not sure what else we can do here
			ormStream = this.getClass().getResourceAsStream(resource);
		}
		return ormStream;
	}

	private String getPackage(String resourceName) {
		if (!resourceName.contains(PATH_SEPARATOR)) {
			return "";
		} else {
			return resourceName.substring(0, resourceName.lastIndexOf(PATH_SEPARATOR));
		}
	}

	private String getRelativeName(String resourceName) {
		if (!resourceName.contains(PATH_SEPARATOR)) {
			return resourceName;
		} else {
			return resourceName.substring(resourceName.lastIndexOf(PATH_SEPARATOR) + 1);
		}
	}

	private Set<String> parse(String line) {
		if (line == null || line.length() == 0) {
			return null;
		}
		Set<String> result = new HashSet<String>();

		StringTokenizer tokenizer = new StringTokenizer(line.trim(), ",");
		String name;
		while (tokenizer.hasMoreElements()) {
			name = tokenizer.nextToken().trim();
			result.add(name);
		}
		return result;
	}
}
