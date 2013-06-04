package sk.seges.acris.security.rebind;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.acris.security.client.IManageableSecuredObject;
import sk.seges.acris.security.rpc.session.ClientSession;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.SourceWriter;

public class ManageableSecuredObjectCreator extends SecuredObjectCreator {

    private static final String CLASSNAME_POSTFIX = "_ManageableSecured";

    protected String getClassNamePostFix(){
        return CLASSNAME_POSTFIX;
    }   

    public ManageableSecuredObjectCreator(ISecuredAnnotationProcessor securedAnnotationProcessor) {
        super(securedAnnotationProcessor);
    }

    protected String[] getInterfaces() {
        return new String[] {
            IManageableSecuredObject.class.getName(), CheckableSecuredObject.class.getCanonicalName()
        };
    }
    
    @Override
    protected String[] getImports() {
        // TODO Auto-generated method stub
        return super.getImports();
    }

    protected void generateMethods(SourceWriter sourceWriter, GeneratorContext context, JClassType classType) throws NotFoundException {
        generateOnLoadMethod(sourceWriter, context, classType);
        generateSecurityCheck(sourceWriter, context, classType);
        generateSetVisibleMethod(sourceWriter, context, classType);
        generateSetEnabledMethod(sourceWriter, context, classType);
    }
    
    private void generateSetVisibleMethod(SourceWriter sourceWriter,
            GeneratorContext context, JClassType classType) throws NotFoundException {
        
        sourceWriter.println("@Override");
        sourceWriter.println("public void setVisible(" + Widget.class.getCanonicalName() + " widget, boolean visible) {");
        sourceWriter.indent();
        sourceWriter.println("user = null;");
        sourceWriter.println(ClientSession.class.getSimpleName() + " clientSession = getClientSession();");
        sourceWriter.println("if (clientSession != null) {");
        sourceWriter.indent();
        sourceWriter.println("user = clientSession.getUser();");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println("boolean hasViewPermission = false;");
        
        List<String> classRoles = new ArrayList<String>();
        List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);
        if(classAnnots != null && classAnnots.size() > 0){
            for (String string : classAnnots) {
                classRoles.add(string);
            }
        }
        JField[] globalVars = classType.getFields();
        
        sourceWriter.println("if (visible) {");
        sourceWriter.indent();
        // looping over every panel field
        for (JField param : globalVars) {

            List<String> paramAnnots = securedAnnotationProcessor.getListAuthoritiesForType(param);
            
            if (paramAnnots != null/* && paramAnnots.size() > 0*/) {
                boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(param);
                List<String> allAnnotations = new ArrayList<String>();
                allAnnotations.addAll(paramAnnots);
                if((classRoles.size() > 0) && (paramAnnots.size() <= 1)){
                    allAnnotations.addAll(classRoles);
                }
                
                generateFieldVisibilityRestriction(sourceWriter, allAnnotations, 
                        context, param, useModifier);
            }
        }
        sourceWriter.indent();
        sourceWriter.println("widget.setVisible(true);");
        sourceWriter.outdent();
        sourceWriter.outdent();
        sourceWriter.println("} else {");
        sourceWriter.indent();
        sourceWriter.println("widget.setVisible(false);");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.outdent();
        sourceWriter.println("}");
    }

    protected void generateFieldVisibilityRestriction(SourceWriter sourceWriter,
            List<String> fieldAnnots, GeneratorContext context,
            JField param, boolean useModifiers) throws NotFoundException {
        // check of view authority
        sourceWriter.println("if (" + param.getName() + "==widget && user != null) {");
        sourceWriter.indent();
        sourceWriter.println("hasViewPermission = " + generateCheckUserAuthority(PERMISSION_VIEW_NAME, fieldAnnots, useModifiers) + ";");
        sourceWriter.println(param.getName() + ".setVisible(hasViewPermission);");
        sourceWriter.outdent();
        sourceWriter.println("} else ");
    }

    private void generateSetEnabledMethod(SourceWriter sourceWriter,
            GeneratorContext context, JClassType classType) throws NotFoundException {
        
        sourceWriter.println("@Override");
        sourceWriter.println("public void setEnabled(" + FocusWidget.class.getCanonicalName() + " widget, boolean enabled) {");
        sourceWriter.indent();
        sourceWriter.println("user = null;");
        sourceWriter.println(ClientSession.class.getSimpleName() + " clientSession = getClientSession();");
        sourceWriter.println("if (clientSession != null) {");
        sourceWriter.indent();
        sourceWriter.println("user = clientSession.getUser();");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println("boolean hasEditPermission = false;");
        
        List<String> classRoles = new ArrayList<String>();
        List<String> classAnnots = securedAnnotationProcessor.getListAuthoritiesForType(classType);
        if(classAnnots != null && classAnnots.size() > 0){
            for (String string : classAnnots) {
                classRoles.add(string);
            }
        }
        JField[] globalVars = classType.getFields();
        
        sourceWriter.println("if(enabled) {");
        sourceWriter.indent();
        // looping over every panel field
        for (JField param : globalVars) {
            if (param.getType().isClassOrInterface() != null && ((JClassType)param.getType()).isAssignableTo(context.getTypeOracle().getType(
                    FocusWidget.class.getName()))) {
                
                List<String> paramAnnots = securedAnnotationProcessor.getListAuthoritiesForType(param);
            
                if (paramAnnots != null/* && paramAnnots.size() > 0*/) {

                    boolean useModifier = !securedAnnotationProcessor.isAuthorityPermission(param);
                    List<String> allAnnotations = new ArrayList<String>();
                    allAnnotations.addAll(paramAnnots);
                    if((classRoles.size() > 0) && (paramAnnots.size() < 1)){
                        allAnnotations.addAll(classRoles);
                    }
                    
                    generateFieldEnabledFlagRestriction(sourceWriter, allAnnotations, 
                            context, param, useModifier);
                }
            }
        }
        sourceWriter.indent();
        sourceWriter.println("widget.setEnabled(true);");
        sourceWriter.outdent();
        sourceWriter.outdent();
        sourceWriter.println("} else {");
        sourceWriter.indent();
        sourceWriter.println("widget.setEnabled(false);");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.outdent();
        sourceWriter.println("}");
    }

    protected void generateFieldEnabledFlagRestriction(SourceWriter sourceWriter,
            List<String> fieldAnnots, GeneratorContext context,
            JField param, boolean useModifiers) throws NotFoundException {
        // check of view authority
        sourceWriter.println("if (user != null && " + param.getName() + "==widget) {");
        sourceWriter.indent();
        sourceWriter.println("hasEditPermission = " + generateCheckUserAuthority(PERMISSION_EDIT_NAME, fieldAnnots, useModifiers)+";");
        sourceWriter.println(param.getName() + ".setEnabled(hasEditPermission);");
        sourceWriter.outdent();
        sourceWriter.println("} else ");
    }

}
