package sk.seges.acris.pap.security;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.acris.pap.security.model.ManageableSecuredType;
import sk.seges.acris.security.shared.util.SecurityUtils;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;

public class ManageableSecuredObjectProcessor extends SecurityProcessor {

	enum WidgetStateHandler {
		VISIBLE {
			@Override
			public String getUtilMethodName() {
				return "handleVisibility";
			}

			@Override
			public Class<?> getWidgetClass() {
				return UIObject.class;
			}
		}, ENABLED {
			@Override
			public String getUtilMethodName() {
				return "handleEnabledState";
			}

			@Override
			public Class<?> getWidgetClass() {
				return HasEnabled.class;
			}
		};
		
		public abstract String getUtilMethodName();
		public abstract Class<?> getWidgetClass();
	}
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new ManageableSecuredType(context.getMutableType(), processingEnv) };
	}

	@Override
    protected void generateMethods(FormattedPrintWriter pw, Element element) {
        generateInitialize(pw);
		super.generateMethods(pw, element);
        generateWidgetMethods(pw, element, WidgetStateHandler.VISIBLE);
        generateWidgetMethods(pw, element, WidgetStateHandler.ENABLED);
    }
    
	private static final String WIDGET_PARAMETER_NAME = "widget";

	private boolean initialized = false;
	
	protected void generateInitialize(FormattedPrintWriter pw) {
		if (initialized) {
	        pw.println("initializeUser();");
		} else {
			initialized = true;
			pw.println("private void initializeUser() {");
			super.generateInitialize(pw);
			pw.println("}");
		}
	}

    private void generateWidgetMethods(FormattedPrintWriter pw, Element element, WidgetStateHandler handler) {
        
    	String type = handler.name().toLowerCase();
        pw.println("@Override");
        pw.println("public void set" + MethodHelper.toMethod(type) + "(", handler.getWidgetClass(), " " + WIDGET_PARAMETER_NAME + ", boolean " + type + ") {");
        pw.println("initializeUser();");
        
        List<String> classAuthorities = ensureAuthoritiesProvider().getListAuthoritiesForType(element);
        if (classAuthorities == null) {
        	classAuthorities = new ArrayList<String>();
        }

        pw.println("if (!" + type + ") {");
        pw.println(SecurityUtils.class, "." + handler.getUtilMethodName() + "(" + WIDGET_PARAMETER_NAME + ", false);");
        pw.println("return;");
        pw.println("}");
        pw.println();

		List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
		
		for (VariableElement field : fields) {
            List<String> fieldAuthorities = ensureAuthoritiesProvider().getListAuthoritiesForType(field);
            
            if (fieldAuthorities != null) {
                if (classAuthorities.size() > 0 && fieldAuthorities.size() <= 1){
                	fieldAuthorities.addAll(classAuthorities);
                }

                pw.println("if (" + field.getSimpleName().toString() + " == " + WIDGET_PARAMETER_NAME + ") {");
                pw.print(SecurityUtils.class, "." + handler.getUtilMethodName() + "(" + USER_FIELD_NAME + ", " + WIDGET_PARAMETER_NAME + ", ");
            	printAuthorities(pw, fieldAuthorities);
                pw.println(");");
                pw.print("} else ");

            }
		}
        pw.println("{");
        pw.println(SecurityUtils.class, "." + handler.getUtilMethodName() + "(" + WIDGET_PARAMETER_NAME + ", true);");
        pw.println("}");
        pw.println("}");
        pw.println();
    }
}