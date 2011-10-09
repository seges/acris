package sk.seges.acris.security.rebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.acris.security.client.mediator.IRuntimeAuthorityMediator;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.SourceWriter;

public class RuntimeSecuredObjectCreator extends SecuredObjectCreator {

	private static final String CLASSNAME_POSTFIX = "_RuntimeSecured";

	protected String getClassNamePostFix(){
		return CLASSNAME_POSTFIX;
	}	

	public RuntimeSecuredObjectCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
		super(securedAnnotationProcessor);
	}
	
	@Override
	protected void generateSecurityCheckBody(SourceWriter sourceWriter, GeneratorContext context,
			JClassType classType) throws NotFoundException {
		sourceWriter.println("user = null;");
		sourceWriter.println(ClientSession.class.getSimpleName() + " clientSession = getClientSession();");
		sourceWriter.println("if (clientSession != null) {");
		sourceWriter.indent();
		sourceWriter.println("user = clientSession.getUser();");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println(List.class.getName()+"<String> runtimeUserGrants = new "+ArrayList.class.getName() + "<String>();");
		sourceWriter.println();
		sourceWriter.println("boolean hasViewPermission = false;");
		sourceWriter.println("boolean hasEditPermission = false;");

		List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);
		
		//Add all security permissions from class secured annotation
		if(classAnnots != null && classAnnots.size() > 0){
			for (String string : classAnnots) {
				sourceWriter.println("runtimeUserGrants.add(\""+ string+ "\");");
			}
		}

		//Add security permission defined in runtime
		sourceWriter.println("if(getGrants() != null && getGrants().length > 0){");
		sourceWriter.indent();
		sourceWriter.println("runtimeUserGrants.addAll(" + Arrays.class.getName() + ".asList(getGrants()));");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();	

		sourceWriter.println("if(runtimeUserGrants != null && runtimeUserGrants.size() > 0){");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = hasAuthorityForPermission(\"" + PERMISSION_VIEW_NAME + "\", runtimeUserGrants);");
		sourceWriter.outdent();
		sourceWriter.println("}else{");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = true;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("if(this.isVisible() != hasViewPermission){");
		sourceWriter.indent();
		sourceWriter.println("this.setVisible(hasViewPermission);");
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
	}
	
	protected void generateMethods(SourceWriter sourceWriter, GeneratorContext context, JClassType classType) throws NotFoundException {
		generateHasAuthorityForPermission(sourceWriter);
		generateInterfaceMethods(sourceWriter);
		generateOnLoadMethod(sourceWriter, context, classType);
		generateSecurityCheck(sourceWriter, context, classType);
	}

	protected String[] getInterfaces() {
		return new String[]{
			IRuntimeAuthorityMediator.class.getName(), CheckableSecuredObject.class.getCanonicalName()
		};
	}

	
	protected void generateClassFields(SourceWriter sourceWriter) {
		super.generateClassFields(sourceWriter);
		sourceWriter.println("private " + List.class.getName() + "<String>"
				+ " userGrants = new " + ArrayList.class.getName() + "<String>();");
	}

	protected void generateFieldSecurityRestrictions(SourceWriter sourceWriter,
			List<String> fieldAnnotationAuthorities, GeneratorContext context,
			JType classType, JField param) {
		// check of view authority
		sourceWriter.println();
		sourceWriter.println("fieldUserGrants = new " + ArrayList.class.getName() + "<String>();");
		if(fieldAnnotationAuthorities != null){ 
			if(fieldAnnotationAuthorities.size() <= 1){
				sourceWriter.println("fieldUserGrants.addAll(runtimeUserGrants);");
			}
			for (String fieldAnnotationAuthority : fieldAnnotationAuthorities) {
				sourceWriter.println("fieldUserGrants.add(\"" + fieldAnnotationAuthority + "\");");
			}
		}
		sourceWriter.println("if (fieldUserGrants != null && fieldUserGrants.size() > 0) {");
		sourceWriter.indent();
		sourceWriter.println("hasViewPermission = hasAuthorityForPermission(\"" + PERMISSION_VIEW_NAME + "\", fieldUserGrants);");
		
		sourceWriter.println("if ( " + param.getName() + " != null ) { ");
		sourceWriter.indent();
		
		checkFieldVisibility(sourceWriter, param);
		
		sourceWriter.println("if( hasViewPermission ){");
		sourceWriter.indent();
		sourceWriter.println("hasEditPermission = hasAuthorityForPermission(\"" + PERMISSION_EDIT_NAME + "\", fieldUserGrants);");
		sourceWriter.println(param.getName() + ".setEnabled( hasEditPermission );");
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	private void generateHasAuthorityForPermission(SourceWriter sourceWriter){
		sourceWriter.println("private boolean hasAuthorityForPermission(String permission, " + List.class.getName() + "<String> aggregatedUserGrants) {");
		sourceWriter.indent();
		sourceWriter.println("for (String userGrant : aggregatedUserGrants) {");
		sourceWriter.indent();
		sourceWriter.println("if (user != null && user.hasAuthority(userGrant + \"_\" + permission))");
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
		sourceWriter.println("@Override");
		sourceWriter.println("public void setPermission(" + UserPermission.class.getName() + " userPermission) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.clear();");
		sourceWriter.println("userGrants.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		
		sourceWriter.println("@Override");
		sourceWriter.println("public void setGrant(String grant) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.clear();");
		sourceWriter.println("userGrants.add(grant);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
		
		sourceWriter.println("@Override");
		sourceWriter.println("public void setPermissions(" + UserPermission.class.getName() + "[] permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.clear();");
		sourceWriter.println("for(" + UserPermission.class.getName() + " userPermission : permissions) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
		
		sourceWriter.println("@Override");
		sourceWriter.println("public void setGrants(String[] grants) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.clear();");
		sourceWriter.println("for(String grant : grants) {");
		sourceWriter.indent();
		sourceWriter.println("userGrants.add(grant);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");		
		sourceWriter.println();
		
		sourceWriter.println("@Override");
		sourceWriter.println("public String[] getGrants() {");
		sourceWriter.indent();
		sourceWriter.println("return userGrants.toArray(new String[0]);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();
	}
}