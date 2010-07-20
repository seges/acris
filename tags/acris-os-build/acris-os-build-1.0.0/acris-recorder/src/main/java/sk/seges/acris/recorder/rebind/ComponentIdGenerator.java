package sk.seges.acris.recorder.rebind;

import java.io.PrintWriter;

import sk.seges.acris.recorder.rpc.event.IRecordableEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ComponentIdGenerator extends Generator {

	private String packageName = null;

	private String className = null;

	private JClassType classType = null;
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		TypeOracle typeOracle = context.getTypeOracle();

		try {
			classType = typeOracle.getType(typeName);
			packageName = classType.getPackage().getName();
			className = classType.getSimpleSourceName() + "WithElementId";

			generateClass(logger, context);

		} catch (Exception e) {
			logger.log(TreeLogger.ERROR, "Component generator failure!!!", e);
		}

		return packageName + "." + className;
	}

	private void generateClass(TreeLogger logger, GeneratorContext context) {

		PrintWriter printWriter = null;
		printWriter = context.tryCreate(logger, packageName, className);

		if (printWriter == null)
			return;

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
		composer.setSuperclass(packageName + "." + classType.getSimpleSourceName());
		composer.addImport(DOM.class.getName());
		composer.addImport(GWT.class.getName());
		composer.addImport(IRecordableEvent.class.getName());
		composer.addImport(packageName + "." + classType.getSimpleSourceName());

		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);

		generateStaticSequence(sourceWriter);
		generateConstructor(sourceWriter);
		
		sourceWriter.outdent();
		sourceWriter.println("}");

		context.commit(logger, printWriter);
	}

	private int generateHash(String text) {
	    int hash = 0;
	 
	    for (int i = 0; i < text.length(); i++) {
	        hash += text.charAt(i);
	        hash += (hash << 10);
	        hash ^= (hash >> 6);
	    }

	    hash += (hash << 3);
	    hash ^= (hash >> 11);
	    hash += (hash << 15);
	    
	    return hash;
	} 
	
	private void generateStaticSequence(SourceWriter sourceWriter) {
		sourceWriter.println("private static int componentCounter = 0;");
	}
	
	private void generateConstructor(SourceWriter sourceWriter) {

		sourceWriter.println("public " + className + "() { ");
		sourceWriter.indent();
		sourceWriter.println("super();");

		//TODO, do the elementID hashing
		sourceWriter.println("DOM.setElementProperty(getElement(), IRecordableEvent.ELEMENT_ID_NAME, " + packageName.length() + " + \"_" + classType.getSimpleSourceName() + "_\" + componentCounter);");
//		sourceWriter.println("GWT.log(IRecordableEvent.ELEMENT_ID_NAME + \": \" + " + packageName.length() + " + \"_" + classType.getSimpleSourceName() + "_\" + componentCounter, null);");
		sourceWriter.println("componentCounter++;");

		sourceWriter.outdent();
		sourceWriter.println("}");

	}
}