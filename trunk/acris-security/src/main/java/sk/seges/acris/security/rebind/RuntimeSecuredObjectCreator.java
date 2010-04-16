package sk.seges.acris.security.rebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.seges.acris.security.client.mediator.IRuntimePermissionMediator;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

public class RuntimeSecuredObjectCreator extends SecuredObjectCreator {

	private static final String CLASSNAME_POSTFIX = "_RuntimeSecured";

	protected String getClassNamePostFix(){
		return CLASSNAME_POSTFIX;
	}	

	public RuntimeSecuredObjectCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
		super(securedAnnotationProcessor);
	}
	
	protected void generateOnLoadMethod(SourceWriter sourceWriter,
			GeneratorContext context, JClassType classType) {
		sourceWriter.println("public void onLoad() {");
		sourceWriter.indent();
		sourceWriter.println("super.onLoad();");
		sourceWriter.println("user = null;");
		sourceWriter.println(ClientSession.class.getSimpleName() + " clientSession = getClientSession();");
		sourceWriter.println("if (clientSession != null) {");
		sourceWriter.indent();
		sourceWriter.println("user = clientSession.getUser();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println(List.class.getName()+"<String> runtimeUserAuthorities = new "+ArrayList.class.getName() + "<String>();");
		sourceWriter.println(List.class.getName()+"<String> fieldUserAuthorities = new "+ArrayList.class.getName() + "<String>();");
		sourceWriter.println();
		sourceWriter.println("boolean hasViewPermission = false;");
		sourceWriter.println("boolean hasEditPermission = false;");

		List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);
		
		//Add all security permissions from class secured annotation
		if(classAnnots != null && classAnnots.size() > 0){
			for (String string : classAnnots) {
				sourceWriter.println("runtimeUserAuthorities.add(\""+ string+ "\");");
			}
		}

		//Add security permission defined in runtime
		sourceWriter.println("if(getPermissions() != null && getPermissions().length > 0){");
		sourceWriter.indent();
		sourceWriter.println("runtimeUserAuthorities.addAll(" + Arrays.class.getName() + ".asList(getPermissions()));");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();	

		sourceWriter.println("if(runtimeUserAuthorities != null && runtimeUserAuthorities.size() > 0){");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = hasAuthorityForPermission(\"" + PERMISSION_VIEW_NAME + "\", runtimeUserAuthorities);");
		sourceWriter.outdent();
		sourceWriter.println("}else{");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = true;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("if( !hasViewPermission ){");
		sourceWriter.indent();
		sourceWriter.println("this.setVisible(false);");
		sourceWriter.outdent();
		sourceWriter.println("}");
 
		JField[] fields = classType.getFields();

		for (JField field : fields) {
			
			List<String> fieldAnnotationAuthorities = securedAnnotationProcessor.getListAuthoritiesForType(field);

			if (fieldAnnotationAuthorities != null && fieldAnnotationAuthorities.size() > 0) {
				generateFieldSecurityRestrictions(sourceWriter, fieldAnnotationAuthorities, 
						context, classType, field);
			}
		}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateMethods(SourceWriter sourceWriter, GeneratorContext context, JClassType classType) {
		generateHasAuthorityForPermission(sourceWriter);
		generateInterfaceMethods(sourceWriter);
		generateOnLoadMethod(sourceWriter, context, classType);
	}

	protected String[] getInterfaces() {
		return new String[]{
			IRuntimePermissionMediator.class.getName()
		};
	}

	
	protected void generateClassFields(SourceWriter sourceWriter) {
		super.generateClassFields(sourceWriter);
		sourceWriter.println("private " + List.class.getName() + "<String>"
				+ " userAuthorities = new " + ArrayList.class.getName() + "<String>();");
	}

	protected void generateFieldSecurityRestrictions(SourceWriter sourceWriter,
			List<String> fieldAnnotationAuthorities, GeneratorContext context,
			JType classType, JField param) {
		// check of view authority
		sourceWriter.println();
		sourceWriter.println("fieldUserAuthorities = new " + ArrayList.class.getName() + "<String>();");
		if(fieldAnnotationAuthorities != null){ 
			if(fieldAnnotationAuthorities.size() <= 1){
				sourceWriter.println("fieldUserAuthorities.addAll(runtimeUserAuthorities);");
			}
			for (String fieldAnnotationAuthority : fieldAnnotationAuthorities) {
				sourceWriter.println("fieldUserAuthorities.add(\"" + fieldAnnotationAuthority + "\");");
			}
		}
		sourceWriter.println("if (fieldUserAuthorities != null && fieldUserAuthorities.size() > 0) {");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = hasAuthorityForPermission(\"" + PERMISSION_VIEW_NAME + "\", fieldUserAuthorities);");
		sourceWriter.println("if( hasViewPermission ){");
		sourceWriter.indent();

		sourceWriter.println("hasEditPermission = hasAuthorityForPermission(\"" + PERMISSION_EDIT_NAME + "\", fieldUserAuthorities);");
		sourceWriter.println(param.getName() + ".setEnabled( hasEditPermission );");
		sourceWriter.outdent();
		sourceWriter.println("} else if ( " + param.getName() + " != null ) { ");
		sourceWriter.indent();
		sourceWriter.println(param.getName() + ".setVisible(false);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	private void generateHasAuthorityForPermission(SourceWriter sourceWriter){
		sourceWriter.println("private boolean hasAuthorityForPermission(String permission, " + List.class.getName() + "<String> aggregatedUserAuthorities) {");
		sourceWriter.indent();
		sourceWriter.println("for (String userAuthority : aggregatedUserAuthorities) {");
		sourceWriter.indent();
		sourceWriter.println("if (user != null && user.hasAuthority(userAuthority + \"_\" + permission))");
		sourceWriter.indent();
		sourceWriter.println("return true;");
		sourceWriter.outdent();
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("return false;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
	}
	
	private void generateInterfaceMethods(SourceWriter sourceWriter){
		sourceWriter.println("public void setPermission(" + IUserPermission.class.getName() + " userPermission) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.clear();");
		sourceWriter.println("userAuthorities.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public void setPermission(String userPermission) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.clear();");
		sourceWriter.println("userAuthorities.add(userPermission);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
		sourceWriter.println("public void setPermissions(" + IUserPermission.class.getName() + "[] permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.clear();");
		sourceWriter.println("for(" + IUserPermission.class.getName() + " userPermission : permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
		sourceWriter.println("public void setPermissions(String[] permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.clear();");
		sourceWriter.println("for(String userPermission : permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userAuthorities.add(userPermission);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");		
		sourceWriter.println();
		sourceWriter.println("public String[] getPermissions() {");
		sourceWriter.indent();
		sourceWriter.println("return userAuthorities.toArray(new String[0]);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
	}
}