package sk.seges.sesam.core.pap.test;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.pap.test.annotation.TestAnnotation;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TestAnnotationProcessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> result = new HashSet<String>();
		result.add(TestAnnotation.class.getCanonicalName());
		return result;
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		if (!roundEnv.processingOver()) {

			for (TypeElement annotation: annotations) {
				Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
				
				for (Element annotatedElement: elementsAnnotatedWith) {
					PrintWriter pw = null;
					try {
						JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(annotatedElement.toString() + "Test", annotatedElement);
						OutputStream os = createSourceFile.openOutputStream();
						
						pw = new PrintWriter(os);
					} catch (Exception e) {
						return false;
					}
					
					pw.println("package " + processingEnv.getElementUtils().getPackageOf(annotatedElement).toString() + ";");
					pw.println();
					pw.println("public class " + annotatedElement.toString() + "Test" + " {");
					
					for (ExecutableElement method: ElementFilter.methodsIn(annotatedElement.getEnclosedElements())) {
						
						pw.println();
						pw.println("	private " + method.getReturnType().toString() + " " + method.getSimpleName().toString() + ";");
						pw.println();
						pw.println("	public " + method.getReturnType().toString() + " get" + method.getSimpleName().toString() + "() {");
						pw.println("		return " + method.getSimpleName().toString() + ";");
						pw.println("	}");
						pw.println();
						pw.println("	public void set" + method.getSimpleName().toString() + "(" + method.getReturnType().toString() + " _value) {");
						pw.println("		this." + method.getSimpleName().toString() + "= _value;");
						pw.println("	}");
					}
					
					pw.println("}");
					pw.flush();
				}
			}
		}
		
		return false;
	}

}
