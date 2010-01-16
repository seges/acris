package sk.seges.acris.security.rebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.seges.acris.security.client.IRuntimeRolesProvider;
import sk.seges.acris.security.rpc.domain.IUserPermission;
import sk.seges.acris.security.rpc.to.ClientContext;
import sk.seges.acris.security.rpc.to.ClientContextHolder;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * generates secured panel according to annotations Secured in original panel, which is
 * replaced by secured panel
 * 
 * @author MPsenkova
 * 
 */
public class RuntimeSecuredPanelCreator extends SecuredPanelCreator {

	private static final String CLASSNAME_POSTFIX = "RuntimeSecurityWrapper";

	protected String getClassNamePostFix(){
		return CLASSNAME_POSTFIX;
	}	

	public RuntimeSecuredPanelCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
		super(securedAnnotationProcessor);
	}
	
	/**
	 * generate onLoad method, calls super onLoad at beginning
	 * 
	 * @param sourceWriter
	 * @param context
	 * @param classType
	 */
	protected void generateOnLoadMethod(SourceWriter sourceWriter,
			GeneratorContext context, JClassType classType) {
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
		sourceWriter.println("if (user == null) {");
		sourceWriter.indent();
		sourceWriter.println("return;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println(List.class.getName()+"<String> myRoles = new "+ArrayList.class.getName() + "<String>();");
		sourceWriter.println(List.class.getName()+"<String> fieldRoles = new "+ArrayList.class.getName() + "<String>();");		
		sourceWriter.println("boolean isView = false;");
		sourceWriter.println("boolean isEdit = false;");

		// checking visibility of whole panel
		List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);
		
		if(classAnnots != null && classAnnots.size() > 0){
			for (String string : classAnnots) {
				sourceWriter.println("myRoles.add(\""+ string+ "\");");
			}
		}

		sourceWriter.println("if(getRoles() != null && getRoles().length > 0){");
		sourceWriter.indent();
		sourceWriter.println("myRoles.addAll("+Arrays.class.getName()+".asList(getRoles()));");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		sourceWriter.println();	

		sourceWriter.println("if(myRoles != null && myRoles.size() > 0){");
		sourceWriter.indent();
		sourceWriter.println("isView = hasRoleFromMyRoles(\""+VIEW +"\", myRoles);");
		sourceWriter.outdent();
		sourceWriter.println("}else{");
		sourceWriter.indent();
		sourceWriter.println("isView = true;");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		sourceWriter.println();

		sourceWriter.println("if( !isView ){");
		sourceWriter.indent();
		sourceWriter.println("this.setVisible(false);");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
 
		JField[] globalVars = classType.getFields();

		// looping over every panel field
		for (JField param : globalVars) {
			
			List<String> paramAnnots = securedAnnotationProcessor.getListAuthoritiesForType(param);

			if (paramAnnots != null && paramAnnots.size() > 0) {
				generateSourceForField(sourceWriter, paramAnnots, 
						context, classType, param);
			}
		}
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}

	protected void generateMethods(SourceWriter sourceWriter, GeneratorContext context, JClassType classType) {
		generateHasRoleFromMyRoles(sourceWriter);
		sourceWriter.println();
		generateSetPermission(sourceWriter);
		sourceWriter.println();
		generateSetPermissions(sourceWriter);
		sourceWriter.println();
		generateGetRoles(sourceWriter);
		sourceWriter.println();
		generateOnLoadMethod(sourceWriter, context, classType);
	}

	protected String[] getInterfaces() {
		return new String[]{
			IRuntimeRolesProvider.class.getName()
		};
	}

	
	protected void generateGlobalVariables(SourceWriter sourceWriter) {
		super.generateGlobalVariables(sourceWriter);
		sourceWriter.println("private " + List.class.getName() + "<String>"
				+ " roles = new " + ArrayList.class.getName() + "<String>();");
	}

	/**
	 * checks authorities according annotation of param and writes source
	 * 
	 * @param sourceWriter
	 * @param annotation
	 * @param context
	 * @param classType
	 * @param param
	 */
	protected void generateSourceForField(SourceWriter sourceWriter,
			List<String> fieldAnnots, GeneratorContext context,
			JType classType, JField param) {
		// check of view authority
		sourceWriter.println();
		sourceWriter.println("fieldRoles = new "+ArrayList.class.getName() + "<String>();");
		if(fieldAnnots != null){ 
			if(fieldAnnots.size() <= 1){
				sourceWriter.println("fieldRoles.addAll(myRoles);");
			}
			for (String string : fieldAnnots) {
				sourceWriter.println("fieldRoles.add(\""+ string +"\");");
			}
		}
		sourceWriter.println("if (fieldRoles != null && fieldRoles.size() > 0) {");
		sourceWriter.indent();
		sourceWriter.println("isView = hasRoleFromMyRoles(\""+VIEW +"\", fieldRoles);");
		sourceWriter.println("if( isView ){");
		sourceWriter.indent();

		sourceWriter.println("isEdit = hasRoleFromMyRoles(\""+EDIT +"\", fieldRoles);");
		sourceWriter.println(param.getName() + ".setEnabled( isEdit );");
		sourceWriter.outdent();
		sourceWriter.println("} else if ( " + param.getName() + " != null ) { ");
		sourceWriter.indent();
		sourceWriter.println(param.getName() + ".setVisible(false);");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}
	
	/**
	 * generates private function for checking if user has required role with permission
	 * @param sourceWriter
	 */
	private void generateHasRoleFromMyRoles(SourceWriter sourceWriter){
		sourceWriter.println("private boolean hasRoleFromMyRoles(String perm, "+ List.class.getName()+"<String> roles) {");
		sourceWriter.indent();
		sourceWriter.println("boolean myVisibility = false;");
		sourceWriter.println("for (String role : roles) {");
		sourceWriter.indent();
		sourceWriter.println("if(user.hasAuthority(role+\"_\"+perm))");
		sourceWriter.indent();
		sourceWriter.println("myVisibility = true;");
		sourceWriter.outdent();
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		sourceWriter.println("return myVisibility;");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}
	
	private void generateSetPermission(SourceWriter sourceWriter){
		sourceWriter.println("public void setPermission(" + IUserPermission.class.getName() + " userPermission) {");
		sourceWriter.indent();
		sourceWriter.println("roles.clear();");
		sourceWriter.println("roles.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}
	
	private void generateSetPermissions(SourceWriter sourceWriter){
		sourceWriter.println("public void setPermissions(" + IUserPermission.class.getName() + "[] permissions) {");
		sourceWriter.indent();
		sourceWriter.println("roles.clear();");
		sourceWriter.println("for(" + IUserPermission.class.getName() + " userPermission : permissions) {");
		sourceWriter.indent();
		sourceWriter.println("roles.add(userPermission.name());");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);		
	}
		
	private void generateGetRoles(SourceWriter sourceWriter){
		sourceWriter.println("public String[] getRoles() {");
		sourceWriter.indent();
		sourceWriter.println("return roles.toArray(new String[0]);");
		sourceWriter.outdent();
		sourceWriter.println(RIGHT_BRACKET_FINISH);
	}
}