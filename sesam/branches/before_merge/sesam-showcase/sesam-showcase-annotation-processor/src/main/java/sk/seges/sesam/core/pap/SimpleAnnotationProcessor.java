package sk.seges.sesam.core.pap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.annotation.Hierarchy;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SimpleAnnotationProcessor extends AbstractProcessor {

	private static final String DEFAULT_SUFFIX  = "Generated";
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotationTypes = new HashSet<String>();
		annotationTypes.add(Hierarchy.class.getName());
		return annotationTypes;
	}
	
	private String getGeneratedFileSuffix() {
		return DEFAULT_SUFFIX;
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
			for (String annotationType: getSupportedAnnotationTypes()) {
				Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(processingEnv.getElementUtils().getTypeElement(annotationType));
				for (Element annotatedElement: annotatedElements) { 
					OutputStream fileStream = null;
					
					String elementPackage = processingEnv.getElementUtils().getPackageOf(annotatedElement).getQualifiedName().toString();
					
					try {
						JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(elementPackage + "." +
								annotatedElement.getSimpleName().toString() + getGeneratedFileSuffix(), annotatedElement);
						fileStream = sourceFile.openOutputStream();
					} catch (IOException e) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to generate target class for elmenet [reason: " + e.toString() + "]",
								annotatedElement);
					}
					PrintWriter pw = new PrintWriter(fileStream);
					pw.println("package " + elementPackage + ";");
					pw.println();
					pw.println("public class " + annotatedElement.getSimpleName().toString() + getGeneratedFileSuffix() + "{");
					pw.println("}");
					pw.flush();
					pw.close();
				}
			}
		} else {
			processingEnv.getMessager().printMessage(Kind.NOTE, "Processing finished");
		}
		return false;
	}
}