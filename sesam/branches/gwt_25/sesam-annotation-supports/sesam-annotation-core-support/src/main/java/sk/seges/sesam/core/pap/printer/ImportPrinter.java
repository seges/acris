package sk.seges.sesam.core.pap.printer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

public class ImportPrinter extends FormattedPrintWriter {

	private final FormattedPrintWriter outputPrintWriter;
	
	public ImportPrinter(HierarchyPrintWriter hierarchyPrintWriter, MutableProcessingEnvironment processingEnv) {
		super(null, processingEnv, hierarchyPrintWriter.getUsedTypes());
		this.outputPrintWriter = hierarchyPrintWriter.addNestedPrinter(this);
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
	public void flush() {
		printImports();
		super.flush();
	}
	
	private void printImports() {
		
		List<MutableDeclaredType> mergedImports = new ArrayList<MutableDeclaredType>();
		ListUtils.addUnique(mergedImports, getUsedTypes());
		
		mergedImports = removeNoPackageImports(mergedImports);

		List<MutableDeclaredType> collectedImports = new ArrayList<MutableDeclaredType>();
		
		for (MutableDeclaredType importName: mergedImports) {
			addImport(collectedImports, importName);
		}
		
		sortByPackage(collectedImports);
		
		String previousPackage = null;

		for (MutableDeclaredType importType : collectedImports) {
			if (!isImportValid(importType)) {
				continue;
			}
			
			if (previousPackage != null && !getVeryTopPackage(importType).equals(previousPackage)) {
				outputPrintWriter.println();
			}
			//TODO do not print types that are nested in the output class
			
			printImport(importType);
			previousPackage = getVeryTopPackage(importType);
		}

		if (collectedImports.size() > 0) {
			outputPrintWriter.println();
		}
	}

	protected boolean isImportValid(MutableDeclaredType importType) {
		return true;
	}
	
	protected void printImport(MutableDeclaredType importType) {
		outputPrintWriter.println("import " + importType.toString(ClassSerializer.CANONICAL, false) + ";");
	}
}