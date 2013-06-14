package sk.seges.acris.widget.rebind.filterpanel;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import sk.seges.acris.widget.client.Cleaner;
import sk.seges.acris.widget.client.filterpanel.AbstractConjunctiveFilterPanel;
import sk.seges.acris.widget.client.table.BeanTable.DomainObjectProperty;
import sk.seges.acris.widget.client.table.BeanTable.FilterProperty;
import sk.seges.acris.widget.client.table.SpecColumn;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class ConjunctiveFilterPanelSpecCreator {
    private static final String FILTER_PANEL_SUFFIX = "_FilterPanelGen";
    private static final String EXPRESSION_SUFFIX = "Expression";
    private static final String LABEL_SUFFIX = "Label";
    private static final String HANDLERS_SUFFIX = "Handlers";

    private TreeLogger logger;
    private GeneratorContext context;
    private TypeOracle typeOracle;
    private String typeName;

    private JClassType classType;
    
    public ConjunctiveFilterPanelSpecCreator(TreeLogger logger, GeneratorContext context, String typeName) {
        this.logger = logger;
        this.context = context;
        this.typeOracle = context.getTypeOracle();
        this.typeName = typeName;
    }
    
    public String createSpec() {
        try {
            classType = typeOracle.getType(typeName);
        } catch (NotFoundException e) {
            logger.log(TreeLogger.ERROR, "Unable to find type name = " + typeName, e);
            return null;
        }

        SourceWriter source = getSourceWriter(classType);
        if(null != source) {
            JMethod[] methods = classType.getMethods();
            putFieldDefinition(source, methods);
            generateMessagesClass(methods);
            putConstructor(source);
            putInitComponents(source, methods);
            putSetCommonChangeHandler(source, methods);
            putRemoveCommonChangeHandler(source, methods);
            putConstructSearchCriteria(source, methods);
            source.commit(logger);
        }
        return getFullReturnName();
    }

    private void putFieldDefinition(SourceWriter source, JMethod[] methods) {
        source.println("private " + messagesTypeName() + " messages;");
        for(JMethod method : methods) {
            source.println("private Label " + method.getName() + LABEL_SUFFIX + ";");
            SpecColumn specColumn = method.getAnnotation(SpecColumn.class);
            source.println("private " + specColumn.filterWidgetType().getCanonicalName() + " " + method.getName() + ";");
            source.println("private SimpleExpression<Comparable<? extends Serializable>> " + method.getName() + EXPRESSION_SUFFIX + ";");
            source.println("private HandlerRegistration " + method.getName() + HANDLERS_SUFFIX + ";");
        }
        source.println();
    }

    private void putConstructor(SourceWriter source) {
        source.println("public " + classType.getSimpleSourceName() + FILTER_PANEL_SUFFIX + "() {");
        source.indent();
            source.println("super();");
            source.println("initComponents();");
        source.outdent();
        source.println("}");
        source.println();
    }
    
    private void putInitComponents(SourceWriter source, JMethod[] methods) {
        source.println("private void initComponents() {");
        source.indent();
            source.println(messagesTypeName() + " messages = GWT.create(" + messagesTypeName() + ".class);");
            source.println("FlowPanel labelValueWidgetPanel = null;");
            source.println();
            for(JMethod method : methods) {
                putLabelInitialization(source, method);
                putFieldInitialization(source, method);
                putLabelValueWidgetPanelInitialization(source, method);
            }
            source.println("add(new Cleaner());");
            source.println();
            for(JMethod method : methods) {
                putExpressionInitialization(source, method);
            }
        source.outdent();
        source.println("}");
        source.println();
    }

    private void putLabelInitialization(SourceWriter source, JMethod method) {
        String fieldName = method.getName();
        String labelName = fieldName + LABEL_SUFFIX;
        source.println(labelName + " = new Label(messages." + fieldName + "());");
        source.println(labelName + ".addStyleName(\"FilterPanelSpec-label\");");
    }
    
    private void putFieldInitialization(SourceWriter source, JMethod method) {
        SpecColumn specColumn = method.getAnnotation(SpecColumn.class);
        String fieldName = method.getName();
        source.println(fieldName + " = GWT.create(" + specColumn.filterWidgetType().getName() + ".class);");
        source.println(fieldName + ".addStyleName(\"FilterPanelSpec-valueWidget\");");
    }
    
    private void putLabelValueWidgetPanelInitialization(SourceWriter source, JMethod method) {
        String fieldName = method.getName();
        String labelName = fieldName + LABEL_SUFFIX;
        source.println("labelValueWidgetPanel = new FlowPanel();");
        source.println("labelValueWidgetPanel.addStyleName(\"FilterPanelSpec-labelValueWidgetPanel\");");
        source.println("labelValueWidgetPanel.add(" + labelName + ");");
        source.println("labelValueWidgetPanel.add(" + fieldName + ");");
        source.println("labelValueWidgetPanel.add(new Cleaner());");
        source.println("add(labelValueWidgetPanel);");
        source.println();
    }
    
    private void putExpressionInitialization(SourceWriter source, JMethod method) {
        SpecColumn specColumn = method.getAnnotation(SpecColumn.class);
        source.println(method.getName() + EXPRESSION_SUFFIX + " = Filter." + specColumn.filterOperation() + "(\"" + specColumn.field() + "\");");        
    }

    private void putSetCommonChangeHandler(SourceWriter source, JMethod[] methods) {
        source.println("public void setCommonChangeHandler(ChangeHandler handler) {");
        source.indent();
            for(JMethod method : methods) {
                String fieldName = method.getName();
                source.println(fieldName + ".addChangeHandler(handler);");
            }

        source.outdent();
        source.println("}");
        source.println();
    }
    
    private void putRemoveCommonChangeHandler(SourceWriter source, JMethod[] methods) {
        source.println("public void removeCommonChangeHandler() {");
        source.indent();
            for(JMethod method : methods) {
                String fieldName = method.getName();
                source.println(fieldName + HANDLERS_SUFFIX + ".removeHandler();");
            }

        source.outdent();
        source.println("}");
        source.println();
    }
    
    private void putConstructSearchCriteria(SourceWriter source, JMethod[] methods) {
        source.println("public Criterion constructSearchCriteria() {");
        source.indent();
            source.println("Comparable<? extends Serializable> value;");
            source.println("Conjunction conjunction = Filter.conjunction();");
            for(JMethod method : methods) {
                String fieldName = method.getName();
                SpecColumn specColumn = method.getAnnotation(SpecColumn.class);
                Class<? extends Widget> widgetClass = specColumn.filterWidgetType();
                //TODO add other widget types later when needed
                if(TextBox.class.equals(widgetClass)) {
                    source.println("value = " + fieldName + ".getText();");
                    source.println("if(null != value && !\"\".equals(value)) {");
                    source.indent();
                        source.println(fieldName + EXPRESSION_SUFFIX + ".setValue(value);");
                        source.println("conjunction.and(" + fieldName + EXPRESSION_SUFFIX + ");");
                        source.println();
                    source.outdent();
                    source.println("}");
                }/* else if(ListBox.class.equals(widgetClass)) {
                    source.println("value = " + fieldName + ".getValue(" + fieldName + ".getSelectedIndex());");
                    source.println("if(null != value) {");
                    source.indent();
                        source.println(fieldName + EXPRESSION_SUFFIX + ".setValue(value);");
                        source.println("conjunction.and(" + fieldName + EXPRESSION_SUFFIX + ")");
                        source.println();
                    source.outdent();
                    source.println("}");
                } */else {
                    throw new IllegalArgumentException("Unsupported filter value class: " + widgetClass + " ... yet");
                }
            }
            source.println("return conjunction;");
        source.outdent();
        source.println("}");
    }
    
    /**
     * SourceWriter instantiation. Return null if the resource already exist.
     * 
     * @return sourceWriter
     */
    public SourceWriter getSourceWriter(JClassType classType) {
        String packageName = classType.getPackage().getName();
        String simpleName = getSimpleReturnName();
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);

        composer.addImport(Label.class.getCanonicalName());
        composer.addImport(FlowPanel.class.getCanonicalName());
        composer.addImport(SimpleExpression.class.getCanonicalName());
        composer.addImport(List.class.getCanonicalName());
        composer.addImport(DomainObjectProperty.class.getCanonicalName());
        composer.addImport(FilterProperty.class.getCanonicalName());
        composer.addImport(Criterion.class.getCanonicalName());
        composer.addImport(Filter.class.getCanonicalName());
        composer.addImport(Page.class.getCanonicalName());
        composer.addImport(PagedResult.class.getCanonicalName());
        composer.addImport(GWT.class.getCanonicalName());
        composer.addImport(Cleaner.class.getCanonicalName());
        composer.addImport(Conjunction.class.getCanonicalName());
        composer.addImport(Serializable.class.getCanonicalName());
        composer.addImport(ChangeHandler.class.getCanonicalName());
        composer.addImport(HandlerRegistration.class.getCanonicalName());
        composer.addImport(AbstractConjunctiveFilterPanel.class.getCanonicalName());
        
        composer.setSuperclass(AbstractConjunctiveFilterPanel.class.getCanonicalName());

        PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
        if (printWriter == null) {
            return null;
        } else {
            SourceWriter sw = composer.createSourceWriter(context, printWriter);
            return sw;
        }
    }
    
    private void generateMessagesClass(JMethod[] methods) {
        String packageName = classType.getPackage().getName();
        String simpleName = messagesTypeName();
        
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
                packageName, simpleName);
        composer.makeInterface();

        composer.addImplementedInterface(Constants.class.getCanonicalName());

        PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
        if(logger.isLoggable(Type.DEBUG)) {
            logger.log(Type.DEBUG, "Creating messages source writer for " + simpleName + ", packageName = " + packageName + ", simpleName = " + simpleName + ", printWriter = " + printWriter);
        }
        
        if (printWriter == null) {
            return;
        } else {
            SourceWriter sw = composer.createSourceWriter(context, printWriter);
            for(JMethod method : methods) {
                sw.println("String " + method.getName() + "();");   
            }           
            sw.commit(logger);
        }
    }

    private String messagesTypeName() {
        return classType.getSimpleSourceName() + "Messages";
    }

    private String getFullReturnName() {
        return classType.getPackage().getName() + "." + getSimpleReturnName();
    }
    
    private String getSimpleReturnName() {
        return classType.getSimpleSourceName() + FILTER_PANEL_SUFFIX;
    }

}
