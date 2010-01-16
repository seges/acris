package sk.seges.acris.binding.rebind.binding;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;

import sk.seges.acris.binding.client.providers.BindingProxyWrapperBaseAdapterProvider;
import sk.seges.acris.binding.client.providers.support.generic.AbstractBindingBeanAdapterProvider;
import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.rebind.bean.PropertyResolver;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generator for {@link BeanProxyWrapper} class.
 * @author fat
 */
public class BindingProxyWraperCreator {

	public static final String WRAPPER_SUFFIX = AbstractBindingBeanAdapterProvider.WRAPPER_SUFFIX; 
	
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

		sourceWriter.println("static {");
		sourceWriter.indent();
		sourceWriter.println(BeanAdapterFactory.class.getSimpleName() + ".addProvider(new " + BindingProxyWrapperBaseAdapterProvider.class.getCanonicalName() + "());");
		sourceWriter.outdent();
		sourceWriter.println("}");

		generateProxyBeanMethods(sourceWriter, parentBeanClassType, listBeanClassType, propertyResolver.getBeanPropertyReference(), propertyResolver.getBeanProperty());
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
	
	private boolean generateProxyBeanMethods(SourceWriter sourceWriter, JClassType parentBeanClassType, JClassType listBeanClassType, String listBeanProperty, String bindingProperty) {
		sourceWriter.println("private " + BeanWrapper.class.getSimpleName() + "<" + parentBeanClassType.getSimpleSourceName() + "> _targetBean;");
		sourceWriter.println("");
		sourceWriter.println("@Override");
		sourceWriter.println("public void setTargetWrapper(" + BeanWrapper.class.getSimpleName() + "<" + parentBeanClassType.getSimpleSourceName() + "> targetBeanWrapper) {");
		sourceWriter.indent();
		sourceWriter.println("_targetBean = targetBeanWrapper;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");
		sourceWriter.println("public void setBoundPropertyValue(String " + listBeanProperty + "_property) {");
		sourceWriter.indent();
		sourceWriter.println("for (" + listBeanClassType.getSimpleSourceName() + " " + listBeanClassType.getSimpleSourceName().toLowerCase() + " : proxyValues) {");
		
		sourceWriter.indent();
		sourceWriter.println("if (" + listBeanClassType.getSimpleSourceName().toLowerCase() + ".get" + getFirstUpperCase(listBeanProperty) + "() == null) continue;");
		
		sourceWriter.println("if (" + listBeanClassType.getSimpleSourceName().toLowerCase() + ".get" + getFirstUpperCase(listBeanProperty) + "().equals(" + listBeanProperty + "_property)) {");
		sourceWriter.indent();
		sourceWriter.println("_targetBean.setAttribute(\"" + bindingProperty + "\", " + listBeanClassType.getSimpleSourceName().toLowerCase() + ");");
		sourceWriter.println("break;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("private " + listBeanClassType.getSimpleSourceName() + " contenu;");
		sourceWriter.println("");
		sourceWriter.println("public String getBoundPropertyValue() {");
		sourceWriter.indent();
		sourceWriter.println(listBeanClassType.getSimpleSourceName() + " " + bindingProperty + "_ref = (" + listBeanClassType.getSimpleSourceName() + ")_targetBean.getAttribute(\""+ bindingProperty +"\");"); 
		sourceWriter.println("if (" + bindingProperty + "_ref == null) {");
		sourceWriter.indent();
		sourceWriter.println("return " + (defaultValue == null ? "null" : defaultValue) + ";");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("return " + bindingProperty + "_ref.get" + getFirstUpperCase(listBeanProperty) + "();" );
		sourceWriter.outdent();
		sourceWriter.println("}");

		return true;
	}
	
	public SourceWriter getSourceWriter(String packageName, String proxyTupleName, JClassType parentBeanClassType, JClassType listBean) {
		String simpleName = proxyTupleName + WRAPPER_SUFFIX;
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);

		composer.addImport(PropertyChangeListener.class.getCanonicalName());
		composer.addImport(List.class.getCanonicalName());
		composer.addImport(BeanWrapper.class.getCanonicalName());
		composer.addImport(BeanAdapterFactory.class.getCanonicalName());
		composer.addImport(BindingProxyWrapperBaseAdapterProvider.class.getCanonicalName());

		composer.addImport(parentBeanClassType.getQualifiedSourceName());
		composer.addImport(listBean.getQualifiedSourceName());
	
		composer.addImplementedInterface(BeanProxyWrapper.class.getCanonicalName() + "<" + parentBeanClassType.getSimpleSourceName() + "," + listBean.getSimpleSourceName() + ">");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);

		if (printWriter == null) {
			return null;
		}
		
		return composer.createSourceWriter(context, printWriter);
	}
}