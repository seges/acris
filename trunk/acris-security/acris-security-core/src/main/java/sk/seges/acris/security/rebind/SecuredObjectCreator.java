package sk.seges.acris.security.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class SecuredObjectCreator {

	private static final String SECURED_SOURCE_FILE_SUFIX = "SecurityWrapper";

	protected static final String PERMISSION_VIEW_NAME = Permission.VIEW.name();

	protected static final String PERMISSION_EDIT_NAME = Permission.EDIT.name();

	private String superclassName;

	protected String className = null;

	protected String packageName = null;

	protected ISecuredAnnotationProcessor securedAnnotationProcessor;

	public SecuredObjectCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
		this.securedAnnotationProcessor = securedAnnotationProcessor;
	}

	public String doGenerate(TreeLogger logger, GeneratorContext context, String typeName, String superclassName)
			throws UnableToCompleteException {
		this.superclassName = superclassName;

		final TypeOracle typeOracle = context.getTypeOracle();
		assert typeOracle != null;

		try {
			JClassType classType = typeOracle.getType(typeName);
			packageName = classType.getPackage().getName();
			className = classType.getSimpleSourceName() + getClassNamePostFix();

			// Generate secured class for specified classType
			generateClass(logger, context, classType);

			return packageName + "." + className;
		} catch (Exception e) {
			logger.log(TreeLogger.ERROR, "Unable to generate security wrapper over the " + typeName, e);
			throw new UnableToCompleteException();
		}
	}

	protected void generateClass(TreeLogger logger, GeneratorContext context, JClassType classType)
			throws NotFoundException {
		PrintWriter printWriter = null;
		printWriter = context.tryCreate(logger, packageName, className);

		if (printWriter == null) {
			return;
		}

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);

		String[] imports = getImports();
		if (imports != null) {
			for (String imp : imports) {
				composer.addImport(imp);
			}
		}

		String[] interfaces = getInterfaces();
		if (interfaces != null) {
			for (String intf : interfaces) {
				composer.addImplementedInterface(intf);
			}
		}

		composer.setSuperclass(this.superclassName);

		SourceWriter sourceWriter = null;
		sourceWriter = composer.createSourceWriter(context, printWriter);

		generateClassFields(sourceWriter);
		generateMethods(sourceWriter, context, classType);

		sourceWriter.outdent();
		sourceWriter.println("}");

		context.commit(logger, printWriter);
	}

	protected void generateClassFields(SourceWriter sourceWriter) {
		sourceWriter.println("private " + UserData.class.getSimpleName() + " user;");
	}

	/**
	 * 
	 * @return array of names of classes, which need to be imported returns null
	 *         if no classes
	 */
	protected String[] getImports() {
		return new String[] { GWT.class.getCanonicalName(), ClientSession.class.getCanonicalName(),
				UserData.class.getCanonicalName() };
	}

	protected String[] getInterfaces() {
		return new String[] { CheckableSecuredObject.class.getCanonicalName() };
	}

	protected void generateMethods(SourceWriter sourceWriter, GeneratorContext context, JClassType classType)
			throws NotFoundException {
		generateOnLoadMethod(sourceWriter, context, classType);
		generateSecurityCheck(sourceWriter, context, classType);
	}

	protected void generateOnLoadMethod(SourceWriter sourceWriter, GeneratorContext context, JClassType classType)
			throws NotFoundException {
		sourceWriter.println();
		sourceWriter.println("@Override");
		sourceWriter.println("public void onLoad() {");
		sourceWriter.indent();
		sourceWriter.println("super.onLoad();");
		sourceWriter.println("check();");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateSecurityCheck(SourceWriter sourceWriter, GeneratorContext context, JClassType classType)
			throws NotFoundException {
		sourceWriter.println("@Override");
		sourceWriter.println("public void check() {");
		sourceWriter.indent();
		generateSecurityCheckBody(sourceWriter, context, classType);
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateSecurityCheckBody(SourceWriter sourceWriter, GeneratorContext context, JClassType classType)
			throws NotFoundException {
		sourceWriter.println("user = null;");
		sourceWriter.println(ClientSession.class.getSimpleName() + " clientSession = getClientSession();");
		sourceWriter.println("if (clientSession != null) {");
		sourceWriter.indent();
		sourceWriter.println("user = clientSession.getUser();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("boolean hasViewPermission = false;");
		sourceWriter.println("boolean hasEditPermission = false;");

		List<String> classRoles = new ArrayList<String>();

		// checking visibility of whole panel
		List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);

		if (classAnnots != null && classAnnots.size() > 0) {
			boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(classType);
			sourceWriter.println("if (user != null) {");
			sourceWriter.indent();
			sourceWriter.println("hasViewPermission = "
					+ generateCheckUserAuthority(Permission.VIEW_SUFFIX, classAnnots, useModifier) + ";");
			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.println("if( !hasViewPermission ){");
			sourceWriter.indent();
			sourceWriter.println("super.setVisible(false);");
			sourceWriter.outdent();
			sourceWriter.println("} else {");
			sourceWriter.indent();
			sourceWriter.println("super.setVisible(true);");
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

			if (paramAnnots != null/* && paramAnnots.size() > 0 */) {
				boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(param);
				List<String> allAnnotations = new ArrayList<String>();
				allAnnotations.addAll(paramAnnots);
				if ((classRoles.size() > 0) && (paramAnnots.size() < 1)) {
					allAnnotations.addAll(classRoles);
				}

				generateFieldSecurityRestrictions(sourceWriter, allAnnotations, context, param, useModifier);
			}
		}
	}

	protected void generateFieldSecurityRestrictions(SourceWriter sourceWriter, List<String> fieldAnnots,
			GeneratorContext context, JField param, boolean useModifiers) throws NotFoundException {
		sourceWriter.println("// securing field " + param.getName());

		sourceWriter.println("if (user != null) {");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = "
				+ generateCheckUserAuthority(Permission.VIEW_SUFFIX, fieldAnnots, useModifiers) + ";");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("if ( " + param.getName() + " != null ) { ");
		sourceWriter.indent();

		checkFieldVisibility(sourceWriter, param);

		sourceWriter.println("if( hasViewPermission ){");
		sourceWriter.indent();
		if (param.getType().isClassOrInterface() != null
				&& (((JClassType) param.getType()).isAssignableTo(context.getTypeOracle().getType(
						Focusable.class.getName())) || ((JClassType) param.getType()).isAssignableTo(context
						.getTypeOracle().getType(DateBox.class.getName())))) {
			sourceWriter.println("hasEditPermission = "
					+ generateCheckUserAuthority(Permission.EDIT_SUFFIX, fieldAnnots, useModifiers) + ";");
			sourceWriter.println(param.getName() + ".setEnabled(hasEditPermission);");
		}
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void checkFieldVisibility(SourceWriter sourceWriter, JField param) {
		sourceWriter.println("if ( " + param.getName() + ".isVisible() != hasViewPermission ) { ");
		sourceWriter.indent();
		sourceWriter.println(param.getName() + ".setVisible(hasViewPermission);");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected String generateCheckUserAuthority(String modifier, List<String> fieldUserAuthorities, boolean useModifier) {

		StringBuffer sb = new StringBuffer();

		int size = fieldUserAuthorities.size();

		for (String fieldUserAuthority : fieldUserAuthorities) {
			size--;
			if (useModifier) {
				sb.append("(user.hasAuthority(\"" + fieldUserAuthority + "\"))");
			} else {
				sb.append("(user.hasAuthority(\"" + fieldUserAuthority + "\"))");
			}
			if (size > 0) {
				sb.append(" || \n");
			}
		}
		return sb.toString();
	}

	protected String getClassNamePostFix() {
		return SECURED_SOURCE_FILE_SUFIX;
	}
}