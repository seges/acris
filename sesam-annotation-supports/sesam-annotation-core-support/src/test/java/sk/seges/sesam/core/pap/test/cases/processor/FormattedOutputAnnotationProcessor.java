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
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

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
					HierarchyPrintWriter rootPrintWriter = null;
					try {
						JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(annotatedElement.toString() + SUFFIX, annotatedElement);
						OutputStream os = createSourceFile.openOutputStream();
						
						rootPrintWriter = initializePrintWriter(os);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					String packageName = processingEnv.getElementUtils().getPackageOf(annotatedElement).toString();
					
					rootPrintWriter.println("package " + packageName + ";");
					rootPrintWriter.println();

					initializeImportPrinter(rootPrintWriter, packageName);

					FormattedPrintWriter classPrintWriter = rootPrintWriter.initializeNestedPrinter();

					classPrintWriter.println("public class " + annotatedElement.getSimpleName().toString() + SUFFIX + " {");
					
					for (ExecutableElement method: ElementFilter.methodsIn(annotatedElement.getEnclosedElements())) {
						
						classPrintWriter.println();
						classPrintWriter.println("private ", method.getReturnType(), " " + method.getSimpleName().toString() + ";");
						classPrintWriter.println();
						
						classPrintWriter.println("public ", method.getReturnType(), " get" + method.getSimpleName().toString() + "() {");
						classPrintWriter.println("return " + method.getSimpleName().toString() + ";");
						classPrintWriter.println("}");
						classPrintWriter.println();
						
						classPrintWriter.println("public void set" + method.getSimpleName().toString() + "(", method.getReturnType(), " _value) {");
						classPrintWriter.println("this." + method.getSimpleName().toString() + "= _value;");
						classPrintWriter.println("}");
					}
					
					classPrintWriter.println("}");
					rootPrintWriter.flush();

				}
			}
		}
		
		return false;
	}
}