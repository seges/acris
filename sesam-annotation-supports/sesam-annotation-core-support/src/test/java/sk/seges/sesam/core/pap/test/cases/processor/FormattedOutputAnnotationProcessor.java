package sk.seges.sesam.core.pap.test.cases.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport.TypePrinterSupport;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.processor.PlugableAnnotationProcessor;
import sk.seges.sesam.core.pap.test.cases.annotation.BasicTestAnnotation;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

@PrintSupport(printer = @TypePrinterSupport(printSerializer = ClassSerializer.SIMPLE))
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FormattedOutputAnnotationProcessor extends PlugableAnnotationProcessor {

	public static final String SUFFIX = "Formatted";
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> result = new HashSet<String>();
		result.add(BasicTestAnnotation.class.getCanonicalName());
		return result;
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		if (!roundEnv.processingOver()) {

			for (TypeElement annotation: annotations) {
				Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
				
				for (Element annotatedElement: elementsAnnotatedWith) {
					FormattedPrintWriter pw = null;
					try {
						JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(annotatedElement.toString() + SUFFIX, annotatedElement);
						OutputStream os = createSourceFile.openOutputStream();
						
						pw = initializePrintWriter(os);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					pw.println("package " + processingEnv.getElementUtils().getPackageOf(annotatedElement).toString() + ";");
					
					printImports(pw);
					
					pw.println();
					pw.println("public class " + annotatedElement.toString() + SUFFIX + " {");
					
					for (ExecutableElement method: ElementFilter.methodsIn(annotatedElement.getEnclosedElements())) {
						
						pw.println();
						pw.println("private ", String.class, " " + method.getSimpleName().toString() + ";");
						pw.println("private ", method.getReturnType(), " " + method.getSimpleName().toString() + ";");
						pw.println();
						
						pw.println("public ", method.getReturnType(), " get" + method.getSimpleName().toString() + "() {");
						pw.println("return " + method.getSimpleName().toString() + ";");
						pw.println("}");
						pw.println();
						
						pw.println("public void set" + method.getSimpleName().toString() + "(", method.getReturnType(), " _value) {");
						pw.println("this." + method.getSimpleName().toString() + "= _value;");
						pw.println("}");
					}
					
					pw.println("}");
					pw.flush();

				}
			}
		}
		
		return false;
	}
}