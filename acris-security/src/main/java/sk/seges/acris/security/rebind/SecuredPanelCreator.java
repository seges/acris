package sk.seges.acris.security.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.domain.Permission;
import sk.seges.acris.security.rpc.to.ClientContext;
import sk.seges.acris.security.rpc.to.ClientContextHolder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * generates secured panel according to annotations in original panel, which is
 * replaced by secured panel
 */
public class SecuredPanelCreator {

	private static final String CLASSNAME_POSTFIX = "SecurityWrapper";

	protected static final String RIGHT_BRACKET_FINISH = "}";

	protected static final String VIEW = Permission.VIEW.name();

	protected static final String EDIT = Permission.EDIT.name();

	/** Name of the superclass */
	private String superclassName;
	
	/** Simple name of class to be generated */
	protected String className = null;

	/** Package name of class to be generated */
	protected String packageName = null;

	protected ISecuredAnnotationProcessor securedAnnotationProcessor;
	
	public SecuredPanelCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
		this.securedAnnotationProcessor = securedAnnotationProcessor;
	}
	
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName, String superclassName) throws UnableToCompleteException {
		this.superclassName = superclassName;
		final TypeOracle typeOracle = context.getTypeOracle();
		assert typeOracle != null;
		
		try {
			// get classType and save instance variables
			JClassType classType = typeOracle.getType(typeName);
			packageName = classType.getPackage().getName();
			className = classType.getSimpleSourceName() + getClassNamePostFix();

			// Generate class source code
			generateClass(logger, context, classType);
		} catch (Exception e) {
			// record to logger that Map generation threw an exception
			logger.log(TreeLogger.ERROR, "PropertyMap ERROR!!!", e);
		}

		// return the fully qualifed name of the class generated
		return packageName + "." + className;
	}

	/**
	 * Generate source code for new class. Class extends <code>HashMap</code>.
	 * 
	 * @param logger
	 *            Logger object
	 * @param context
	 *            Generator context
	 * @throws NotFoundException 
	 */
	protected void generateClass(TreeLogger logger, GeneratorContext context,
			JClassType classType) throws NotFoundException {
		// get print writer that receives the source code
		PrintWriter printWriter = null;
		printWriter = context.tryCreate(logger, packageName, className);
		// print writer if null, source code has ALREADY been generated, return
		if (printWriter == null)
			return;
		// init composer, set class properties, create source writer
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, className);

		// add imports
		String[] imports = getImports();
		if(imports != null){
			for (String imp : imports) {
				composer.addImport(imp);
			}
		}
		
		//add implemented interfaces
		String[] interfaces = getInterfaces();
		if(interfaces != null){
			for (String intf : interfaces) {
				composer.addImplementedInterface(intf);				
			}
		}

		// add super class
		composer.setSuperclass(this.superclassName);

		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);
		// generate global variables
		sourceWriter.println();
		generateGlobalVariables(sourceWriter);
		sourceWriter.println();
		// generator constructor source code
		generateConstructor(sourceWriter);
		sourceWriter.println();
		generateMethods(sourceWriter, context, classType);
		// close generated class
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		// commit generated class
		context.commit(logger, printWriter);
	}
	
	protected void generateMethods(SourceWriter sourceWriter,
			GeneratorContext context, JClassType classType) throws NotFoundException{
		generateOnLoadMethod(sourceWriter, context, classType);
	}
	
	/**
	 * 
	 * @return array of names of classes, which need to be imported
	 * returns null if no classes
	 */
	protected String[] getImports(){
		return new String[] { GWT.class.getCanonicalName(),
				ClientContext.class.getCanonicalName(),
				GenericUser.class.getCanonicalName(),
				ClientContextHolder.class.getCanonicalName()};
	}

	
	/**
	 * 
	 * @return array of names of interfaces, which need to be implemented
	 * returns null if no interfaces
	 */
	protected String[] getInterfaces(){
		return null;
	}
	
	
	/**
	 * generate onLoad method, calls super onLoad at beginning
	 * 
	 * @param sourceWriter
	 * @param context
	 * @param classType
	 * @throws NotFoundException 
	 */
	protected void generateOnLoadMethod(SourceWriter sourceWriter,
			GeneratorContext context, JClassType classType) throws NotFoundException {
		sourceWriter.println("@Override");
		sourceWriter.println("public void onLoad() {");
		sourceWriter.indent();
		sourceWriter.println("super.onLoad();");
		sourceWriter.println("user = null;");
		sourceWriter.println(ClientContextHolder.class.getSimpleName()
				+ " cch = GWT.create(" + ClientContextHolder.class.getSimpleName()
				+ ".class);");
		sourceWriter.println(ClientContext.class.getSimpleName()
				+ " ctxt = cch.getClientContext();");
		sourceWriter.println("if (ctxt != null) {");
		sourceWriter.indent();
		sourceWriter.println("user = ctxt.getUser();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("boolean isView = false;");
		sourceWriter.println("boolean isEdit = false;");

		List<String> classRoles = new ArrayList<String>();

		// checking visibility of whole panel
		List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);

		if(classAnnots != null && classAnnots.size() > 0){
			boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(classType);
			sourceWriter.println("if (user != null) {");
			sourceWriter.indent();
			sourceWriter.println("isView = " + generateAuthConds(VIEW, classAnnots, useModifier) + ";");
			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.println("if( !isView ){");
			sourceWriter.indent();
			sourceWriter.println("this.setVisible(false);");
			sourceWriter.outdent();
			sourceWriter.println("}");

			for (String string : classAnnots) {
				classRoles.add(string);
			}
		}

		JField[] globalVars = classType.getFields();

		// looping over every panel field
		for (JField param : globalVars) {

			List<String> paramAnnots = securedAnnotationProcessor.getListAuthoritiesForType(param);
			
			if (paramAnnots != null && paramAnnots.size() > 0) {
				boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(param);
				List<String> allAnnotations = new ArrayList<String>();
				allAnnotations.addAll(paramAnnots);
				if((classRoles.size() > 0) && (paramAnnots.size() <= 1)){
					allAnnotations.addAll(classRoles);
				}
				
				generateSourceForField(sourceWriter, allAnnotations, 
						context, classType, param, useModifier);
			}
		}
		
		
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}


	/**
	 * checks authorities according annotation of param and writes source
	 * 
	 * @param sourceWriter
	 * @param annotation
	 * @param context
	 * @param classType
	 * @param param
	 * @throws NotFoundException 
	 */
	protected void generateSourceForField(SourceWriter sourceWriter,
			List<String> fieldAnnots, GeneratorContext context,
			JType type, JField param, boolean useModifiers) throws NotFoundException {
		// check of view authority
		sourceWriter.println("if (user != null) {");
		sourceWriter.indent();
		sourceWriter.println("isView = " + generateAuthConds(VIEW, fieldAnnots, useModifiers) + ";");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("if( isView ){");
		sourceWriter.indent();
		// focusable specific
//		try {

		if (type.isClassOrInterface() != null && ((JClassType)type).isAssignableTo(context.getTypeOracle().getType(
				Focusable.class.getName()))) {
			sourceWriter.println("isEdit = " + generateAuthConds(EDIT, fieldAnnots, useModifiers)+";");
			sourceWriter.println(param.getName() + ".setEnabled( isEdit );");
		}
		sourceWriter.outdent();
		sourceWriter.println("} else if ( " + param.getName() + " != null ) { ");
		sourceWriter.indent();
		sourceWriter.println(param.getName() + ".setVisible(false);");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}

	/**
	 * Generate source code for the default constructor. Create default
	 * constructor, call super(), and insert all key/value pairs from the
	 * resoruce bundle.
	 * 
	 * @param sourceWriter
	 *            Source writer to output source code
	 */
	protected void generateConstructor(SourceWriter sourceWriter) {
		sourceWriter.println("public " + className + "() { ");
		sourceWriter.indent();
		sourceWriter.println("super();");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateGlobalVariables(SourceWriter sourceWriter) {
		sourceWriter.println("private " + GenericUser.class.getSimpleName()
				+ " user;");
	}
	
	/**
	 * 
	 * @param sourceWriter
	 * @param modifier
	 * @param fieldAnnots
	 * @return
	 */
	private String generateAuthConds(String modifier, List<String> fieldAnnots, boolean useModifier) {
		Iterator<String> it = fieldAnnots.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			String string = it.next();
			if (useModifier) {
				sb.append("(user.hasAuthority(\"" + string + "_" + modifier + "\"))");
			} else {
				sb.append("(user.hasAuthority(\"" + string + "\"))");
			}
			if (it.hasNext())
				sb.append(" || \n");
		}
		return sb.toString();
	}

	protected String getClassNamePostFix(){
		return CLASSNAME_POSTFIX;
	}	
}