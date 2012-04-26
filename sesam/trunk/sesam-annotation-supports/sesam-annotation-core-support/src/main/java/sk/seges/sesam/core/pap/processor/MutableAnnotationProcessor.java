package sk.seges.sesam.core.pap.processor;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Date;

import javax.annotation.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport.TypePrinterSupport;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.TypePrinter;
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
		MutableDeclaredType mutableType;
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
		
		public MutableDeclaredType getMutableType() {
			return mutableType;
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

	class MutableImportPrinter extends ImportPrinter {

		private String packageName;
		
		MutableImportPrinter(PrintWriter outputPrintWriter, String packageName) {
			super(outputPrintWriter);
			this.packageName = packageName;
		}
		
		protected String getImportPackage(MutableDeclaredType importType) {
			return importType.getCanonicalName().replace("." + importType.getSimpleName(), "");	
		}
		
		protected void printImport(MutableDeclaredType importType) {
			if (packageName == null || !packageName.equals(getImportPackage(importType))) {
				super.printImport(importType);
			}
		}
	}

	@Override
	protected ImportPrinter getImportPrinter(PrintWriter printWriter, String packageName) {
		return new MutableImportPrinter(printWriter, packageName);
	}
	
	@Override
	final protected boolean processElement(MutableDeclaredType el, RoundEnvironment roundEnv) {

		TypeElement typeElement = (TypeElement) el.asElement();

		RoundContext roundContext = new RoundContext();
		roundContext.typeElement = typeElement;
		roundContext.mutableType = el;
		roundContext.processingEnv = processingEnv;
		
		MutableDeclaredType[] outputClasses = getOutputClasses(roundContext);
		
		processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + typeElement.getSimpleName().toString() + " with " + getClass().getSimpleName(), typeElement);
		
		long startTime = new Date().getTime();
		
		for (MutableDeclaredType outputClass: outputClasses) {
			
			FormattedPrintWriter pw = null;
			
			try {
				
				boolean alreadyExists = processingEnv.getElementUtils().getTypeElement(outputClass.getCanonicalName()) != null;

				ProcessorContext context = new ProcessorContext();
				context.typeElement = typeElement;
				context.outputClass = outputClass;
				context.mutableType = el;
				context.processingEnv = processingEnv;

				if (!checkPreconditions(context, alreadyExists)) {
					if (alreadyExists) {
						processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] File " + outputClass.getCanonicalName() + " already exists.", typeElement);
					}
					processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] Skipping file " + outputClass.getCanonicalName() + " processing.", typeElement);
					continue;
				}

				//TODO Use outputClass.getPackageName() + "." + outputClass.getSimpleName() otherwise you'll have problems with nested classes
				JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(outputClass.getPackageName() + "." + outputClass.getSimpleName(), typeElement);
				OutputStream os = createSourceFile.openOutputStream();
				pw = initializePrintWriter(os);

				context.printWriter = pw;

				pw.println("package " + outputClass.getPackageName() + ";");
				pw.println();

				printImports(pw, outputClass.getPackageName());
				printAnnotations(context);
				
				pw.println("@", Generated.class, "(value = \"" + this.getClass().getCanonicalName() + "\")");

				new TypePrinter(processingEnv, pw).printTypeDefinition(outputClass);

				pw.println(" {");
				pw.println();
				processElement(context);
				pw.println("}");
				pw.flush();
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process element " + e.getMessage(), typeElement);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}

		long totalTime = new Date().getTime() - startTime;
		processingEnv.getMessager().printMessage(Kind.NOTE, "Took " + (totalTime / 1000) + "s, " + (totalTime % 1000) + " ms", typeElement);

		return !supportProcessorChain();
	}

	protected abstract MutableDeclaredType[] getOutputClasses(RoundContext context);
}