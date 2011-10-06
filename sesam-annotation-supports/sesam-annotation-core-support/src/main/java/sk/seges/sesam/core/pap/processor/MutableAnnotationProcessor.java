package sk.seges.sesam.core.pap.processor;

import java.io.OutputStream;
import java.lang.reflect.Type;

import javax.annotation.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport.TypePrinterSupport;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

@PrintSupport(autoIdent = true, printer = @TypePrinterSupport(printSerializer = ClassSerializer.SIMPLE))
public abstract class MutableAnnotationProcessor extends ConfigurableAnnotationProcessor {

	public class RoundContext {

		TypeElement typeElement;
		MutableDeclaredType mutableType;
		MutableProcessingEnvironment processingEnv;
		
		public TypeElement getTypeElement() {
			return typeElement;
		}

		public MutableDeclaredType getMutableType() {
			return mutableType;
		}
		
		public MutableProcessingEnvironment getProcessingEnv() {
			return processingEnv;
		}
	}
	
	public class ProcessorContext {
		
		TypeElement typeElement;
		MutableDeclaredType outputClass;
		FormattedPrintWriter printWriter;
		MutableProcessingEnvironment processingEnv;
		
		public MutableDeclaredType getOutputType() {
			return outputClass;
		}
		
		public FormattedPrintWriter getPrintWriter() {
			return printWriter;
		}		
		
		public TypeElement getTypeElement() {
			return typeElement;
		}
		
		public MutableProcessingEnvironment getProcessingEnv() {
			return processingEnv;
		}
	}
	
	protected Type[] getImports(ProcessorContext context) {
		return new Type[] { };
	}

	protected void printAnnotations(ProcessorContext context) {
	}

	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		return true;
	}

	protected abstract void processElement(ProcessorContext context);

	final protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		TypeElement typeElement = (TypeElement) element;

		RoundContext roundContext = new RoundContext();
		roundContext.typeElement = typeElement;
		roundContext.mutableType = (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(typeElement.asType());
		roundContext.processingEnv = processingEnv;
		
		MutableDeclaredType[] outputClasses = getOutputClasses(roundContext);
		
		processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + element.getSimpleName().toString() + " with " + getClass().getSimpleName(), element);

		for (MutableDeclaredType outputClass: outputClasses) {
			
			FormattedPrintWriter pw = null;
			
			try {
				
				boolean alreadyExists = processingEnv.getElementUtils().getTypeElement(outputClass.getCanonicalName()) != null;

				ProcessorContext context = new ProcessorContext();
				context.typeElement = typeElement;
				context.outputClass = outputClass;
				context.processingEnv = processingEnv;

				if (!checkPreconditions(context, alreadyExists)) {
					if (alreadyExists) {
						processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] File " + outputClass.getCanonicalName() + " already exists.", element);
					}
					processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] Skipping file " + outputClass.getCanonicalName() + " processing.", element);
					continue;
				}

				//TODO Use outputClass.getPackageName() + "." + outputClass.getSimpleName() otherwise you'll have problems with nested classes
				JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(outputClass.getPackageName() + "." + outputClass.getSimpleName(), element);
				OutputStream os = createSourceFile.openOutputStream();
				pw = initializePrintWriter(os);

				context.printWriter = pw;

				pw.println("package " + outputClass.getPackageName() + ";");
				pw.println();

				printImports(pw);
				printAnnotations(context);
				
				pw.println("@", Generated.class, "(value = \"" + this.getClass().getCanonicalName() + "\")");

				MutableTypeMirror superClassType = outputClass.getSuperClass();

				pw.print("public " + outputClass.getKind().toString() + " " + outputClass.toString(ClassSerializer.SIMPLE, true));
				
				if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && !outputClass.getKind().equals(MutableTypeKind.INTERFACE)) {
					pw.print(" extends ", superClassType);
				}
	
				if (outputClass.getInterfaces() != null && outputClass.getInterfaces().size() > 0) {

					boolean supportedType = false;
					
					if (outputClass.getKind().equals(MutableTypeKind.CLASS)) {
						pw.print(" implements ");
						supportedType = true;
					} else 	if (outputClass.getKind().equals(MutableTypeKind.INTERFACE)) {
						pw.print(" extends ");
						supportedType = true;
					}
	
					if (supportedType) {
						int i = 0;

						if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && outputClass.getKind().equals(MutableTypeKind.INTERFACE)) {
							pw.print(superClassType);
							i++;
						}
						
						for (MutableTypeMirror type : outputClass.getInterfaces()) {
							if (i > 0) {
								pw.print(", ");
							}
							pw.print(type);
							i++;
						}
					}
				}
				pw.println(" {");
				pw.println();
				processElement(context);
				pw.println("}");
				pw.flush();
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process element " + e.getMessage(), element);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}

		return supportProcessorChain();
	}

	protected abstract MutableDeclaredType[] getOutputClasses(RoundContext context);
}