package sk.seges.acris.binding.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public abstract class AbstractCreator {

	protected TreeLogger logger;
	protected GeneratorContext context;
	protected TypeOracle typeOracle;

	protected String typeName;
	protected String packageName;
	protected JClassType classType;

	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

		this.typeOracle = context.getTypeOracle();
		assert typeOracle != null;
		
		this.logger = logger;
		assert logger != null;
	
		this.context = context;
		assert context != null;

		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to find classtype for " + typeName);
			throw new UnableToCompleteException();
		}

		this.typeName = typeName;

		packageName = classType.getPackage().getName();

		if (!initialize()) {
			return typeName;
		}
		
		SourceWriter sourceWriter = getSourceWriter(getOutputPackage(), getOutputSimpleName());
		
		if (sourceWriter == null) {
			return getOutputPackage() + "." + getOutputSimpleName();
		}

		doGenerate(sourceWriter);
		
		sourceWriter.commit(logger);

		return getOutputPackage() + "." + getOutputSimpleName();
	}

	protected boolean initialize() throws UnableToCompleteException {
		return true;
	}
	
	protected abstract void doGenerate(SourceWriter sourceWriter) throws UnableToCompleteException;
	
	protected String getOutputPackage() {
		return packageName;
	}
	
	protected abstract String getOutputSimpleName();
	
	protected final SourceWriter getSourceWriter(String packageName, String outputTypeName) throws UnableToCompleteException {
		
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, outputTypeName);

		String superclass = getSuperclassName();
		
		if (superclass != null) {
			composer.setSuperclass(superclass);
		}
		
		String[] interfaces = getImplementedInterfaces();

		for (String interfaceName : interfaces) {
			composer.addImplementedInterface(interfaceName);
		}

		String[] imports = getImports();

		for (String importName : imports) {
			composer.addImport(importName);
		}

		PrintWriter printWriter = context.tryCreate(logger, packageName, outputTypeName);

		if (printWriter == null) {
			return null;
		}

		return composer.createSourceWriter(context, printWriter);
	}

	protected abstract String[] getImports() throws UnableToCompleteException;

	protected abstract String[] getImplementedInterfaces();

	protected abstract String getSuperclassName();

	protected String[] addUniqueToArray(String[] elements, String element) {
		List<String> elementsList = new ArrayList<String>();

		for (String el : elements) {
			if (el.equals(element)) {
				return elements;
			}
			elementsList.add(el);
		}
		elementsList.add(element);

		return elementsList.toArray(new String[] {});
	}
}