package sk.seges.acris.pap.security;

import sk.seges.acris.pap.security.accessor.SecuredAccessor;
import sk.seges.acris.pap.security.configurer.SecurityProcessorConfigurer;
import sk.seges.acris.pap.security.model.SecuredType;
import sk.seges.acris.pap.security.provider.SecuredAuthoritiesProvider;
import sk.seges.acris.pap.security.provider.StringAuthoritiesProvider;
import sk.seges.acris.security.shared.session.ClientSessionDTO;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.acris.security.shared.util.SecurityUtils;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SecurityProcessor extends MutableAnnotationProcessor {

	protected SecuredAuthoritiesProvider securedAuthoritiesProvider;
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new SecuredType(context.getMutableType(), processingEnv) };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SecurityProcessorConfigurer();
	}

	protected SecuredAuthoritiesProvider ensureAuthoritiesProvider() {
		if (this.securedAuthoritiesProvider == null) {
			this.securedAuthoritiesProvider = getAuthoritiesProvider();
		}
		
		return securedAuthoritiesProvider;
	}
	
	protected SecuredAuthoritiesProvider getAuthoritiesProvider() {
		return new StringAuthoritiesProvider();
	}
	
	protected static final String USER_FIELD_NAME = "user";
	
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();

		generateMethods(pw, context.getTypeElement());
	}

	protected void generateClientSessionProviderMethods(FormattedPrintWriter pw, Element element) {
		if (!ProcessorUtils.hasMethod("getClientSession", element, false)) {
			
			MutableTypes typeUtils = processingEnv.getTypeUtils();
			
			MutableDeclaredType clientSessionType = typeUtils.getDeclaredType(typeUtils.toMutableType(ClientSessionDTO.class),
					new MutableDeclaredType[] {/*typeUtils.toMutableType(GenericUserDTO.class)*/});
			
			pw.println("private ", clientSessionType, " clientSession;");
			pw.println();
			pw.println("public ", clientSessionType, " getClientSession() {");
			pw.println("return clientSession;");
			pw.println("}");
			pw.println();
			pw.println("public void setClientSession(", clientSessionType, " clientSession) {");
			pw.println("this.clientSession = clientSession;");
			pw.println("}");
			pw.println();
		}
	}
	
    protected void generateMethods(FormattedPrintWriter pw, Element element) {
		generateClassFields(pw);
		generateInitializeUser(pw);
    	generateClientSessionProviderMethods(pw, element);
		generateOnLoadMethod(pw, element);
		generateSecurityCheck(pw, element);
	}
	
	protected void generateClassFields(FormattedPrintWriter pw) {
		pw.println("private ", GenericUserDTO.class, " " + USER_FIELD_NAME + ";");
		pw.println();
	}

	
	protected void generateOnLoadMethod(FormattedPrintWriter pw, Element element) {
	
		if (!ProcessorUtils.hasMethod("onLoad", element)) {
			//TODO check return type
			return;
		}
		
		pw.println("@Override");
		pw.println("public void onLoad() {");
		pw.println("super.onLoad();");
		pw.println("check();");
		pw.println("}");
		pw.println();
	}

	protected void generateSecurityCheck(FormattedPrintWriter pw, Element mutableSecuredElement) {
		pw.println("@Override");
		pw.println("public void check() {");
		generateSecurityCheckBody(pw, mutableSecuredElement);
		pw.println("}");
		pw.println();
	}

	protected void generateInitializeUser(FormattedPrintWriter pw) {
		pw.println("private void initializeUser() {");
		pw.println(USER_FIELD_NAME + " = null;");
		pw.println();
		pw.println("if (getClientSession() != null) {");
		pw.println(USER_FIELD_NAME + " = (", GenericUserDTO.class, ")getClientSession().getUser();");
		pw.println("}");
		pw.println();
		pw.println("}");
	}

	protected void printAuthorities(FormattedPrintWriter pw, List<String> authorities) {
		int i = 0;
		for (String authority: authorities) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print("\"" + authority + "\"");
			i++;
		}
	}
	
	protected void generateSecurityCheckBody(FormattedPrintWriter pw, Element securedElement) {

        pw.println("initializeUser();");
		processElementFields(pw, securedElement);
	}

	protected void processElementFields(FormattedPrintWriter pw, Element securedElement) {
		// checking visibility of whole panel
		List<String> classAuthorities = ensureAuthoritiesProvider().getListAuthoritiesForType(securedElement);

		generatePermissionCheck(pw, classAuthorities, "this");

		for (VariableElement field : ElementFilter.fieldsIn(securedElement.getEnclosedElements())) {

			List<String> fieldAuthorities = ensureAuthoritiesProvider().getListAuthoritiesForType(field);

			if (fieldAuthorities != null) {

				if (classAuthorities.size() > 0 && fieldAuthorities.size() == 0) {
					fieldAuthorities.addAll(classAuthorities);
				}

				generatePermissionCheck(pw, fieldAuthorities, field.getSimpleName().toString());
			}
		}

		TypeMirror superclass = ((TypeElement) securedElement).getSuperclass();

		if (superclass.getKind().equals(TypeKind.DECLARED) && getSecuredAccessor(((DeclaredType)superclass).asElement()).isValid()) {
			processElementFields(pw, ((DeclaredType)superclass).asElement());
		}
	}

	protected SecuredAccessor getSecuredAccessor(Element element) {
		return new SecuredAccessor(element, processingEnv);
	}

	protected void generatePermissionCheck(FormattedPrintWriter pw, List<String> fieldAuthorities, String name) {
		if (fieldAuthorities != null && fieldAuthorities.size() > 0 ) {
			pw.print(SecurityUtils.class, ".handlePermission(" + USER_FIELD_NAME + ", " + name + ", ");
			printAuthorities(pw, fieldAuthorities);
			pw.println(");");
		}
	}
}