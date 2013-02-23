package sk.seges.sesam.core.pap.processor;

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
import sk.seges.sesam.core.pap.printer.ImportPrinter;
import sk.seges.sesam.core.pap.printer.TypePrinter;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

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
		HierarchyPrintWriter currentPrintWriter;
		HierarchyPrintWriter rootPrintWriter;
		
		MutableDeclaredType mutableType;
		MutableProcessingEnvironment processingEnv;
		
		public MutableDeclaredType getOutputType() {
			return outputClass;
		}
		
		public HierarchyPrintWriter getPrintWriter() {
			return currentPrintWriter;
		}		
		
		public TypeElement getTypeElement() {
			return typeElement;
		}
		
		public HierarchyPrintWriter getRootPrintWriter() {
			return rootPrintWriter;
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
		
		MutableImportPrinter(MutableProcessingEnvironment processingEnv, String packageName) {
			super(processingEnv);
			this.packageName = packageName;
		}
		
		protected String getImportPackage(MutableDeclaredType importType) {
			return importType.getCanonicalName().replace("." + importType.getSimpleName(), "");	
		}
		
		@Override
		protected boolean isImportValid(MutableDeclaredType importType) {
			return packageName == null || !packageName.equals(getImportPackage(importType));
		}
	}

	@Override
	protected ImportPrinter initializeImportPrinter(String packageName) {
		return new MutableImportPrinter(processingEnv, packageName);
	}
	
	@Override
	final protected int processElement(MutableDeclaredType el, RoundEnvironment roundEnv) {

		TypeElement typeElement = (TypeElement) el.asElement();

		RoundContext roundContext = new RoundContext();
		roundContext.typeElement = typeElement;
		roundContext.mutableType = el;
		roundContext.processingEnv = processingEnv;

		processingEnv.getUsedTypes().clear();
		
		MutableDeclaredType[] outputClasses = getOutputClasses(roundContext);
		
		processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + typeElement.getSimpleName().toString() + " with " + getClass().getSimpleName(), typeElement);
		
		long startTime = new Date().getTime();

		int processedCount = 0;
		
		for (MutableDeclaredType outputClass: outputClasses) {
			
			HierarchyPrintWriter rootPrintWriter = null;
			
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
				rootPrintWriter = initializePrintWriter(createSourceFile.openOutputStream());
				context.rootPrintWriter = rootPrintWriter;

				processedCount++;
				
				rootPrintWriter.println("package " + outputClass.getPackageName() + ";");
				rootPrintWriter.println();

				rootPrintWriter.addNestedPrinter(initializeImportPrinter(outputClass.getPackageName()));
				
				context.currentPrintWriter = rootPrintWriter;

				printAnnotations(context);
				
				rootPrintWriter.println("@", Generated.class, "(value = \"" + this.getClass().getCanonicalName() + "\")");
				
				new TypePrinter(rootPrintWriter, processingEnv).print(outputClass);

				context.currentPrintWriter = outputClass.getPrintWriter();

				processElement(context);
				rootPrintWriter.flush();
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process element " + e.getMessage(), typeElement);
			} finally {
				if (rootPrintWriter != null) {
					rootPrintWriter.close();
				}
			}
		}

		long totalTime = new Date().getTime() - startTime;
		processingEnv.getMessager().printMessage(Kind.NOTE, "Took " + (totalTime / 1000) + "s, " + (totalTime % 1000) + " ms", typeElement);

		return processedCount;
	}

	protected abstract MutableDeclaredType[] getOutputClasses(RoundContext context);
}