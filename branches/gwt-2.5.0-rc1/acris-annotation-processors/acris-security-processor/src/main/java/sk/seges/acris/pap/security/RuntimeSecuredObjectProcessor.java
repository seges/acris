package sk.seges.acris.pap.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

import sk.seges.acris.pap.security.model.RuntimeSecuredType;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RuntimeSecuredObjectProcessor extends SecurityProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new RuntimeSecuredType(context.getMutableType(), processingEnv) };
	}

	@Override
	protected void generateMethods(FormattedPrintWriter pw, Element element) {
		super.generateMethods(pw, element);
		generateGrantsMethods(pw);
	}

	protected void printAuthorities(FormattedPrintWriter pw, List<String> authorities) {
		pw.print("getGrants(), ");
		super.printAuthorities(pw, authorities);
	}
	
	protected MutableDeclaredType toMutableType(Class<?> clazz, Class<?> typeVariable) {
		return processingEnv.getTypeUtils().getDeclaredType(
				processingEnv.getTypeUtils().toMutableType(clazz),
				new MutableDeclaredType[] {processingEnv.getTypeUtils().toMutableType(typeVariable)});
	}
	
	protected void generateClassFields(FormattedPrintWriter pw) {
		super.generateClassFields(pw);
		pw.println("private ", toMutableType(List.class, String.class), " userGrants = new ", toMutableType(ArrayList.class, String.class), "();");
	}

	private void generateGrantsMethods(FormattedPrintWriter pw){
		pw.println("@Override");
		pw.println("public void setPermission(", UserPermission.class, " userPermission) {");
		pw.println("userGrants.clear();");
		pw.println("userGrants.add(userPermission.name());");
		pw.println("}");
		pw.println("");
		pw.println("@Override");
		pw.println("public void setGrant(", String.class, " grant) {");
		pw.println("userGrants.clear();");
		pw.println("userGrants.add(grant);");
		pw.println("}");
		pw.println();
		pw.println("@Override");
		pw.println("public void setPermissions(", UserPermission.class, "[] permissions) {");
		pw.println("userGrants.clear();");
		pw.println("for(", UserPermission.class, " userPermission : permissions) {");
		pw.println("userGrants.add(userPermission.name());");
		pw.println("}");
		pw.println("}");
		pw.println("@Override");
		pw.println("public void setGrants(", String.class, "[] grants) {");
		pw.println("userGrants.clear();");
		pw.println("for(String grant : grants) {");
		pw.println("userGrants.add(grant);");
		pw.println("}");
		pw.println("}");		
		pw.println();
		pw.println("@Override");
		pw.println("public ", String.class, "[] getGrants() {");
		pw.println("return userGrants.toArray(new ", String.class, "[0]);");
		pw.println("}");
		pw.println();
	}
}