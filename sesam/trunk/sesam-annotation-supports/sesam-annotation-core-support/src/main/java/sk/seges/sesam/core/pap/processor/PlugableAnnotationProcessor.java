package sk.seges.sesam.core.pap.processor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter.FlushListener;

public abstract class PlugableAnnotationProcessor extends AbstractProcessor {

	Map<FormattedPrintWriter, PrintWriter> printersMap = new HashMap<FormattedPrintWriter, PrintWriter>();
	private Map<FormattedPrintWriter, ByteArrayOutputStream> buffersMap = new HashMap<FormattedPrintWriter, ByteArrayOutputStream>();

	protected final String lineSeparator;

	protected PlugableAnnotationProcessor() {
		lineSeparator = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
	}
	
	class ImportPrinter implements FlushListener {

		protected final PrintWriter outputPrintWriter;
		
		ImportPrinter(PrintWriter outputPrintWriter) {
			this.outputPrintWriter = outputPrintWriter;
		}
		
		private String getVeryTopPackage(MutableDeclaredType importType) {
			String importPackage = importType.getPackageName();
			int index = importPackage.indexOf('.');
			if (index != -1) {
				importPackage = importPackage.substring(0, index);
			}
			return importPackage;
		}
		
		private List<MutableDeclaredType> removeNoPackageImports(List<MutableDeclaredType> imports) {
			List<MutableDeclaredType> result = new ArrayList<MutableDeclaredType>();
			for (MutableDeclaredType importType: imports) {
				if (importType.getPackageName() != null && importType.getPackageName().length() > 0)  {
					result.add(importType);
				}
			}
			
			return result;
		}
		
		private void sortByPackage(List<MutableDeclaredType> imports) {
			Collections.sort(imports, new Comparator<MutableDeclaredType>() {

				@Override
				public int compare(MutableDeclaredType o1, MutableDeclaredType o2) {
					return o1.getCanonicalName().compareTo(o2.getCanonicalName());
				}
				
			});
		}

		private void addImport(List<? extends Type> imports, MutableDeclaredType mutableType) {
			if (mutableType.getPackageName() != null && !mutableType.getPackageName().equals(Void.class.getPackage().getName())) {
				ListUtils.addUnique(imports, mutableType);
			}
		}

		@Override
		public void beforeFlush(FormattedPrintWriter pw) {}

		@Override
		public void afterFlush(FormattedPrintWriter pw) {
			
			List<MutableDeclaredType> mergedImports = new ArrayList<MutableDeclaredType>();
			ListUtils.addUnique(mergedImports, pw.getUsedTypes());
			
			mergedImports = removeNoPackageImports(mergedImports);

			List<MutableDeclaredType> collectedImports = new ArrayList<MutableDeclaredType>();
			
			for (MutableDeclaredType importName: mergedImports) {
				addImport(collectedImports, importName);
			}
			
			sortByPackage(collectedImports);
			
			String previousPackage = null;

			for (MutableDeclaredType importType : collectedImports) {
				if (previousPackage != null && !getVeryTopPackage(importType).equals(previousPackage)) {
					outputPrintWriter.println();
				}
				//TODO do no print types that are nested in the output class
				printImport(importType);
				previousPackage = getVeryTopPackage(importType);
			}

			if (collectedImports.size() > 0) {
				outputPrintWriter.println();
			}

			ByteArrayOutputStream byteArrayOutputStream = buffersMap.get(pw);		

			String afterImports = PlugableAnnotationProcessor.this.toString(byteArrayOutputStream);
			outputPrintWriter.print(afterImports);

			outputPrintWriter.flush();
		}
		
		protected void printImport(MutableDeclaredType importType) {
			outputPrintWriter.println("import " + importType.toString(ClassSerializer.CANONICAL, false) + ";");
		}
	}
	
	protected TypeParametersSupport typeParametersSupport;
	protected MutableProcessingEnvironment processingEnv;
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		this.processingEnv = new MutableProcessingEnvironment(pe);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
	}

	private PrintSupport getPrintSupport(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		
		PrintSupport printerSupport = clazz.getAnnotation(PrintSupport.class);
		if (printerSupport != null) {
			return printerSupport;
		}

		if (clazz.getSuperclass() != null) {
			return getPrintSupport(clazz.getSuperclass());
		}
		
		return null;
	}
	
	protected FormattedPrintWriter initializePrintWriter(OutputStream os) {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final PrintWriter outputPrintWriter = new PrintWriter(os);

		FormattedPrintWriter pw = new FormattedPrintWriter(byteArrayOutputStream, processingEnv) {
			@Override
			public void close() {
				super.close();
				outputPrintWriter.close();
			}
		};
				
		printersMap.put(pw, outputPrintWriter);
		buffersMap.put(pw, byteArrayOutputStream);
		
		PrintSupport printerSupport = getPrintSupport(this.getClass());
		
		if (printerSupport != null) {
			pw.setAutoIndent(printerSupport.autoIdent());
			pw.setSerializer(printerSupport.printer().printSerializer());
			pw.serializeTypeParameters(printerSupport.printer().printTypeParameters());
		} else {
			pw.setAutoIndent(false);
			pw.setSerializer(ClassSerializer.CANONICAL);
			pw.serializeTypeParameters(true);
		}
				
		return pw;
	}

	protected ImportPrinter getImportPrinter(PrintWriter printWriter, String packageName) {
		return new ImportPrinter(printWriter);
	}

	protected void printImports(FormattedPrintWriter pw, String packageName) {
		PrintWriter printWriter = printersMap.get(pw);
		if (printWriter == null) {
			throw new RuntimeException("Unknown print writer. Use initializePrintWriter in order to intialize print wirter correctly.");
		}
		pw.flush();
		pw.addFlushListener(getImportPrinter(printWriter, packageName));

		ByteArrayOutputStream byteArrayOutputStream = buffersMap.get(pw);		

		String beforeImports = toString(byteArrayOutputStream);
		printWriter.println(beforeImports);

		byteArrayOutputStream.reset();
	}
	
	private String toString(ByteArrayOutputStream outputStream) {
		String s = outputStream.toString();
		if (s.endsWith(lineSeparator)) {
			s = s.substring(0, s.length() - lineSeparator.length());
		}
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}
}
