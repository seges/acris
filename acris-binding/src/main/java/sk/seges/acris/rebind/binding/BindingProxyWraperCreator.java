package sk.seges.acris.rebind.binding;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.util.List;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import sk.seges.acris.bind.BeanProxyWrapper;
import sk.seges.acris.bind.BeanWrapper;
import sk.seges.acris.rebind.bean.PropertyResolver;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BindingProxyWraperCreator {

	public static final String WRAPPER_SUFFIX = "BeanWrapperProxy"; 
	
	private GeneratorContext context;
	private TreeLogger logger;
	private String defaultValue;
	
	public BindingProxyWraperCreator(GeneratorContext context, TreeLogger logger, String defaultValueConverted) {
		this.context = context;
		this.logger = logger;
	}
	
	public String generate(String packageName,
			JClassType parentBeanClassType, PropertyResolver propertyResolver) throws UnableToCompleteException {
	
		String proxyTupleName = propertyResolver.getPropertyTuple(parentBeanClassType);
		
		JClassType listBeanClassType;
		try {
			listBeanClassType = propertyResolver.resolveBeanPropertyClassType(parentBeanClassType);
		} catch (IntrospectionException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}
		
		SourceWriter sourceWriter = getSourceWriter(packageName, proxyTupleName, parentBeanClassType, listBeanClassType);
		
		if (sourceWriter == null) {
			//already generated
			return proxyTupleName + WRAPPER_SUFFIX;
		}

		generateBeanWrapperMethods(sourceWriter, listBeanClassType, propertyResolver.getBeanProperty(), propertyResolver.getBeanPropertyReference());
		generateProxyBeanMethods(sourceWriter, parentBeanClassType, listBeanClassType, propertyResolver.getBeanPropertyReference());
		generateListBeanValuesMethods(sourceWriter, listBeanClassType);
		
		sourceWriter.commit(logger);
		
		return proxyTupleName + WRAPPER_SUFFIX;
	}
	
	private String getFirstUpperCase(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
	
	private boolean generateListBeanValuesMethods(SourceWriter sourceWriter, JClassType listBeanClassType) {
		sourceWriter.println("private List<" + listBeanClassType.getSimpleSourceName() + "> proxyValues;");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public void setProxyValues(List<" + listBeanClassType.getSimpleSourceName() + "> proxyValues) {");
		sourceWriter.indent();
		sourceWriter.println("this.proxyValues = proxyValues;");
		sourceWriter.outdent();
		sourceWriter.println("}");

		return true;
	}
	
	private boolean generateProxyBeanMethods(SourceWriter sourceWriter, JClassType parentBeanClassType, JClassType listBeanClassType, String listBeanProperty) {
		sourceWriter.println("private " + BeanWrapper.class.getSimpleName() + "<" + parentBeanClassType.getSimpleSourceName() + "> _targetBean;");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public void setTargetWrapper(" + BeanWrapper.class.getSimpleName() + "<" + parentBeanClassType.getSimpleSourceName() + "> targetBeanWrapper) {");
		sourceWriter.indent();
		sourceWriter.println("_targetBean = targetBeanWrapper;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public void set" + getFirstUpperCase(listBeanProperty) + "(String " + listBeanProperty + "_property) {");
		sourceWriter.indent();
		sourceWriter.println("for (" + listBeanClassType.getSimpleSourceName() + " " + listBeanClassType.getSimpleSourceName().toLowerCase() + " : proxyValues) {");
		
		sourceWriter.indent();
		sourceWriter.println("if (" + listBeanClassType.getSimpleSourceName().toLowerCase() + ".get" + getFirstUpperCase(listBeanProperty) + "() == null) continue;");
		
		
		sourceWriter.println("if (" + listBeanClassType.getSimpleSourceName().toLowerCase() + ".get" + getFirstUpperCase(listBeanProperty) + "().equals(" + listBeanProperty + "_property)) {");
		sourceWriter.indent();
		sourceWriter.println("_targetBean.setAttribute(\"" + listBeanClassType.getSimpleSourceName().toLowerCase() + "\", " + listBeanClassType.getSimpleSourceName().toLowerCase() + ");");
		sourceWriter.println("break;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");

		return true;
	}
	
	private boolean generateBeanWrapperMethods(SourceWriter sourceWriter, JClassType listBeanClassType, String bindingProperty, String listBeanProperty) {
		sourceWriter.println("private " + listBeanClassType.getSimpleSourceName() + " contenu;");
		sourceWriter.println("private final " + PropertyChangeSupport.class.getSimpleName() + " pcs = new " + PropertyChangeSupport.class.getSimpleName() + "(this);");
		sourceWriter.println("");
		sourceWriter.println("public String get" + getFirstUpperCase(listBeanProperty) + "() {");
		
		sourceWriter.indent();
//		sourceWriter.println("return contenu.get" + getFirstUpperCase(listBeanProperty) + "();");
		sourceWriter.println(listBeanClassType.getSimpleSourceName() + " " + bindingProperty + "_ref = (" + listBeanClassType.getSimpleSourceName() + ")_targetBean.getAttribute(\""+ bindingProperty +"\");"); 
		sourceWriter.println("if (" + bindingProperty + "_ref == null) {");
		sourceWriter.indent();
		sourceWriter.println("return " + (defaultValue == null ? "null" : defaultValue) + ";");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("return " + bindingProperty + "_ref.get" + getFirstUpperCase(listBeanProperty) + "();" );
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public " + listBeanClassType.getSimpleSourceName() + " getContent() {");
		sourceWriter.indent();
		sourceWriter.println("return contenu;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public Object getAttribute(String attr) {");
		sourceWriter.indent();
		sourceWriter.println("if (attr.equals(\"" + listBeanProperty + "\")) {");
		sourceWriter.indent();
		sourceWriter.println("return this.get" + getFirstUpperCase(listBeanProperty) + "();"); 
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("return null;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public void setContent(" + listBeanClassType.getSimpleSourceName() + " content) {");
		sourceWriter.indent();
		sourceWriter.println("this.contenu = content;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public void addPropertyChangeListener(" + PropertyChangeListener.class.getSimpleName() + " listener) {");
		sourceWriter.indent();
		sourceWriter.println("pcs.addPropertyChangeListener(listener);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public void removePropertyChangeListener(" + PropertyChangeListener.class.getSimpleName() + " listener) {");
		sourceWriter.indent();
		sourceWriter.println("pcs.removePropertyChangeListener(listener);");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public void setAttribute(String attr, Object value) {");
		sourceWriter.indent();
		sourceWriter.println("if (attr.equals(\"" + listBeanProperty + "\")) {"); 
		sourceWriter.indent();
		sourceWriter.println("set" + getFirstUpperCase(listBeanProperty) + "(value.toString());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");

		return true;
	}
	
	public SourceWriter getSourceWriter(String packageName, String proxyTupleName, JClassType parentBeanClassType, JClassType listBean) {
		String simpleName = proxyTupleName + WRAPPER_SUFFIX;
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);

		composer.addImport(PropertyChangeListener.class.getCanonicalName());
		composer.addImport(PropertyChangeSupport.class.getCanonicalName());
		composer.addImport(List.class.getCanonicalName());
		composer.addImport(HasPropertyChangeSupport.class.getCanonicalName());
		composer.addImport(BeanWrapper.class.getCanonicalName());
		
		composer.addImport(parentBeanClassType.getQualifiedSourceName());
		composer.addImport(listBean.getQualifiedSourceName());
	
		composer.addImplementedInterface(HasPropertyChangeSupport.class.getCanonicalName());
		composer.addImplementedInterface(BeanProxyWrapper.class.getCanonicalName() + "<" + parentBeanClassType.getSimpleSourceName() + "," + listBean.getSimpleSourceName() + ">");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);

		if (printWriter == null) {
			return null;
		}
		
		SourceWriter sw = composer.createSourceWriter(context, printWriter);
		return sw;
	}
}