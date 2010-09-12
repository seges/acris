/**
 * 
 */
package sk.seges.acris.binding.rebind.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.jsr269.BeanWrapperProcessor;
import sk.seges.acris.binding.rebind.AbstractCreator;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;
import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.sesam.domain.IObservableObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JMethodHelper;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author eldzi
 * @author fat
 */
public class BeanWrapperCreator extends AbstractCreator {

	protected static final String NESTED_BEAN_WRAPPER = "__nested";
	protected static final String BEAN_WRAPPER_CONTENT = "beanWrapperContent";

	protected String superclassName;
	private List<String> wrappedFields = new ArrayList<String>();

	private BindingNamingStrategy typeStrategy;

	protected JClassType beanType;
	private String outputSimpleName;
	
	public BeanWrapperCreator(BindingNamingStrategy typeStrategy) {
		this.typeStrategy = typeStrategy;
	}

	/**
	 * @param typeName
	 * @return
	 */
	protected String suggestSuperclass(String typeName) {
//		return beanType.getQualifiedSourceName();
		return null;
	}

	private String ensureOutputSimpleName() {
		if (outputSimpleName == null) {
			outputSimpleName = typeStrategy.getBeanWrapperImplementationName(classType.getSimpleSourceName());
		}
		return outputSimpleName;
	}
	
	protected String getOutputSimpleName() {
		return ensureOutputSimpleName();
	}

	@Override
	protected boolean initialize() throws UnableToCompleteException {
		typeStrategy.setGeneratorContext(context);

		String beanTypeName = typeStrategy.getBeanTypeName(typeName);

		try {
			beanType = typeOracle.getType(beanTypeName);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Undefined bean type (" + beanTypeName + " cannot be found on the classpath)", e);
			throw new UnableToCompleteException();
		}

		this.superclassName = suggestSuperclass(typeName);
		return true;
	}

	protected void doGenerate(SourceWriter sourceWriter) throws UnableToCompleteException {

		wrappedFields.clear();

		String beanTypeName = typeStrategy.getBeanTypeName(typeName);

		if (beanTypeName == null) {
			logger.log(TreeLogger.ERROR, "Unable to extract bean from type " + typeName);
			throw new UnableToCompleteException();
		}

		JClassType processingType = beanType;

		JClassType objectType;

		try {
			objectType = typeOracle.getType(Object.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Undefined bean type (" + beanTypeName + " cannot be found on the classpath)", e);
			throw new UnableToCompleteException();
		}

//		new IntrospectionDelegateCreator().generateBeanInfoConnection(sourceWriter, getOutputSimpleName(), beanTypeName);
		
		List<JMethod> allMethods = new ArrayList<JMethod>();

		while (processingType != null && !processingType.equals(objectType)) {

			JMethod[] methods = processingType.getMethods();
			boolean found = false;
			for (JMethod method : methods) {
				if (!(isGetter(method) || isSetter(method))) {
					continue;
				}
				if (method.isFinal()) {
					continue;
				}
//				if (method.isAbstract()) {
//					// TODO: handle abstract classes - when there is a wrapper
//					// over abstract class, do implement a method that will
//					// delegate to it but without the abstract modifier...
//					continue;
//				}

				if (method.isPrivate() || method.isProtected()) {
					continue;
				}

				for (JMethod allMethod : allMethods) {
					if (RebindUtils.getComparableMethodDeclaration(allMethod).compareTo(RebindUtils.getComparableMethodDeclaration(method)) == 0) {
						found = true;
					}
				}
				if (!found) {
					allMethods.add(method);
				} else {
					found = false;
				}
			}

			processingType = processingType.getSuperclass();
		}

		JMethod[] methods = allMethods.toArray(new JMethod[0]);

		try {
			createWrapper(methods, sourceWriter, beanType);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to create wrapper for bean " + beanTypeName, e);
			throw new UnableToCompleteException();
		}
	}

	protected void createWrapper(JMethod[] methods, SourceWriter source, JClassType classType) throws NotFoundException {
		source.indent();
		
		String simpleName = classType.getSimpleSourceName();
		
		if (superclassName == null || classType.isInterface() != null || classType.isAbstract()) {
			source.println("private " + simpleName + " " + BEAN_WRAPPER_CONTENT + ";");
		} else {
			// initialize new instance by default
			// usually this happen when there is Gilead proxied domain
			// object
			source.println("private " + simpleName + " " + BEAN_WRAPPER_CONTENT + " = GWT.create(" + simpleName + ".class);");
		}
		source.println();

		generatePropertyChangeSupportMethods(source, simpleName);

		// create links with content object getters and setters
		for (int i = 0; i < methods.length; i++) {
			if (isExcludedMethod(methods[i])) {
				// there was a method but was excluded
				continue;
			}

			JMethod beanMethod = methods[i];

			String fieldName = RebindUtils.toFieldName(beanMethod.getName());

			if (beanMethod.getName().startsWith("set")) { // setter
				JParameter parameter = beanMethod.getParameters()[0];
				JMethod getter = RebindUtils.getGetter(classType, fieldName);
				if (getter == null) {
					// this is not regular setter with getter counterpart.
					// We don't generate wrapper methods for that.
					methods[i] = null;
					continue;
				}
				//JField declaredField = findDeclaredField(classType, fieldName);
				if (isNotBean(parameter.getType()) /* && declaredField != null */) {
					generateSetterForPrimitive(source, beanMethod, parameter, getter);
				} else /* if (declaredField != null) */{
					generateSetterForBean(source, beanMethod, parameter, getter);
				}
			} else { // getter
				//JField declaredField = findDeclaredField(classType, fieldName);
				if (isNotBean(beanMethod.getReturnType()) /* && declaredField != null */) {
					generateGetterForPrimitive(source, beanMethod);
				} else /* if (declaredField != null) */{
					generateGetterForBean(source, beanMethod);
				}
			}
		}

		generateWrapperMethods(source, simpleName, methods);

		generateGetBeanAttributes(source, methods);
		generateSetBeanAttributes(source, methods, simpleName);

		generateClearWrappersMethod(source);

		source.commit(logger);
	}

	protected void generateSetBeanAttributes(SourceWriter source, JMethod[] methods, String simpleName) {
		// create the set attribute method
		source.println("public void setBeanAttribute(String attr, Object value) {");
		source.indent();
		for (int i = 0; i < methods.length; i++) {
			if (isExcludedMethod(methods[i])) {
				// there was a method but was excluded
				continue;
			}

			JMethod methode = methods[i];
			if (isSetter(methode)) {
				JType paramType = methode.getParameters()[0].getType();
				source.println("if (attr.equals(\"" + RebindUtils.toFieldName(methode.getName()) + "\")) { ");
				source.indent();
				source.println("this." + methode.getName() + "(" + castFromString(paramType, "value") + ");");
				source.outdent();
				source.print("} else ");
			}
		}
		source.println("if (attr.equals(\"" + BEAN_WRAPPER_CONTENT + "\")) {");
		source.indent();
		source.println("this." + BEAN_WRAPPER_CONTENT + " = (" + simpleName + ") value;");
		source.outdent();
		source.println("} else ");
		source.print("{");
		source.println("}");
		source.outdent();
		source.println("}");

	}

	protected void generateGetBeanAttributes(SourceWriter source, JMethod[] methods) {
		// create the getAttribute method
		source.println("public Object getBeanAttribute(String attr) {");
		source.indent();

		for (int i = 0; i < methods.length; i++) {
			if (isExcludedMethod(methods[i])) {
				// there was a method but was excluded
				continue;
			}
			String methodName = methods[i].getName();
			JType returnType = methods[i].getReturnType();
			if (isGetter(methods[i])) {
				source.println("if (attr.equals(\"" + RebindUtils.toFieldName(methodName) + "\")) {");
				source.indent();
				source.println("return " + castToString(returnType, "this." + methodName + "()") + ";");
				source.outdent();
				source.print("} else ");
			}
		}
		
		source.println("if (attr.equals(\"" + BEAN_WRAPPER_CONTENT + "\")) {");
		source.indent();
		source.println("return " + BEAN_WRAPPER_CONTENT + ";");
		source.outdent();
		source.println("} else ");
		source.print("{");
		source.indent();
		source.println("return null;");
		source.outdent();
		source.println("}");
		source.outdent();
		source.println("}");
		source.println();

	}

	protected String getRawWrapperType() {
		return BeanWrapper.class.getName();
	}

	protected void generateWrapperMethods(SourceWriter source, String simpleName, JMethod[] methods) throws NotFoundException {
		source.println("public void setBeanWrapperContent(" + simpleName + " " + BEAN_WRAPPER_CONTENT + ") {");
		source.indent();
		source.println(simpleName + " oldValue = this." + BEAN_WRAPPER_CONTENT + ";");
		source.println("this." + BEAN_WRAPPER_CONTENT + " = (" + simpleName + ") " + BEAN_WRAPPER_CONTENT + ";");
		source.println("clearWrappers();");
		source.println("pcs.firePropertyChange(BeanWrapper.CONTENT, oldValue, "+ BEAN_WRAPPER_CONTENT + ");");
		source.outdent();
		source.println("}");
		source.println();
		source.println("public " + simpleName + " getBeanWrapperContent() {");
		source.indent();
		source.println("return this." + BEAN_WRAPPER_CONTENT + ";");
		source.outdent();
		source.println("}");
	}

	private void generatePropertyChangeSupportMethods(SourceWriter source, String simpleName) {
		source.println("private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);");
		source.println();

		source.println("public void addPropertyChangeListener(PropertyChangeListener listener) {");
		source.indent();
		source.println("pcs.addPropertyChangeListener(listener);");
		source.outdent();
		source.println("}");

		source.println("public void removePropertyChangeListener(PropertyChangeListener listener) {");
		source.indent();
		source.println("pcs.removePropertyChangeListener(listener);");
		source.outdent();
		source.println("}");
		source.println();
	}

	private void generateClearWrappersMethod(SourceWriter source) {
		source.println("private void clearWrappers() {");
		source.indent();
		for (String field : wrappedFields) {
			source.println(field + " = null;");
		}
		source.outdent();
		source.println("}");
	}

	protected void generateGetterForPrimitive(SourceWriter source, JMethod methode) {
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();
		source.println("if(" + BEAN_WRAPPER_CONTENT + " != null) {");
		source.indent();
		source.println("return " + BEAN_WRAPPER_CONTENT + "." + methode.getName() + "();");
		source.outdent();
		source.println("}");
		String field = defaultValue(methode.getReturnType());
		source.println("return " + field + ";");
		source.outdent();
		source.println("}");
		source.println();
	}

	protected String getWrapperType(JType returnType) {
		return getRawWrapperType() + "<" + returnType.getQualifiedSourceName() + ">";
	}

	protected String getWrapperClassName(String beanClassName) {
		return beanClassName + BeanWrapperProcessor.BEAN_WRAPPER_SUFFIX;
	}

	protected void generateGetterForSimpleBean(SourceWriter source, JMethod methode) {
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();
		source.println("return " + BEAN_WRAPPER_CONTENT + "." + methode.getName() + "();");
		source.outdent();
		source.println("}");
		source.println();
	}

	protected void generateGetterForBean(SourceWriter source, JMethod methode) {
		String field = RebindUtils.toFieldName(methode.getName());
		JType returnType = methode.getReturnType();

		String resultName = getWrapperClassName(returnType.getQualifiedSourceName());

		try {
			typeOracle.getType(resultName);
		} catch (NotFoundException e) {
			generateGetterForPrimitive(source, methode);
			return;
		}
		
		field = field + NESTED_BEAN_WRAPPER;
		String wrapperType = getWrapperType(returnType);
		source.println("private " + wrapperType + " " + field + ";");

		source.println(new JMethodHelper(methode).getReadableDeclaration(wrapperType, false, false, false, false, true) + " {");
//		source.println(methode.getReadableDeclaration() + " {");
		source.indent();
		
		source.println("if (" + BEAN_WRAPPER_CONTENT + "." + methode.getName() + "() == null) {");
		source.indent();
		source.println("return null;");
		source.outdent();
		source.println("}");
		
		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + wrapperType + ") GWT.create(" + getWrapperClassName(returnType.getQualifiedSourceName()) + ".class);");
		source.println("if(" + BEAN_WRAPPER_CONTENT + " != null) {");
		source.indent();
		source.println("this." + field + ".setBeanWrapperContent(" + BEAN_WRAPPER_CONTENT + "." + methode.getName() + "());");
		source.outdent();
		source.println("}");
		source.outdent();
		source.println("}");
		source.println("return this." + field + ";");
		source.outdent();
		source.println("}");
		source.println();

		addWrappedField(field);
	}

	protected void addWrappedField(String field) {
		wrappedFields.add(field);
	}
	
	protected void generateSetterForPrimitive(SourceWriter source, JMethod methode, JParameter parameter, JMethod getter) {
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();

		source.println(parameter.getType().getQualifiedSourceName() + " oldValue = " + getter.getName() + "();");
		source.println(BEAN_WRAPPER_CONTENT + "." + methode.getName() + "(" + parameter.getName() + ");");
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
		source.outdent();
		source.println("}");
		source.println();
	}

	protected void generateSetterForBean(SourceWriter source, JMethod methode, JParameter parameter, JMethod getter) {
		String field = RebindUtils.toFieldName(methode.getName()) + NESTED_BEAN_WRAPPER;
		JType returnType = parameter.getType();

		String resultName = getWrapperClassName(returnType.getQualifiedSourceName());

		try {
			typeOracle.getType(resultName);
		} catch (NotFoundException e) {
			generateSetterForPrimitive(source, methode, parameter, getter);
			return;
		}

		String wrapperType = getWrapperType(returnType);

//		source.println(new JMethodHelper(methode).getReadableDeclaration(wrapperType, 0) + " {");
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();

		source.println("if (" + parameter.getName() + " == null) {");
		source.indent();
		source.println("return;");
		source.outdent();
		source.println("}");

		source.println(parameter.getType().getQualifiedSourceName() + " oldValue = " + BEAN_WRAPPER_CONTENT + "." + getter.getName() + "();");
		source.println(BEAN_WRAPPER_CONTENT + "." + methode.getName() + "(" + parameter.getName() + ");");

		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + wrapperType + ") GWT.create(" + getWrapperClassName(returnType.getQualifiedSourceName()) + ".class);");
		source.outdent();
		source.println("}");
		source.println("this." + field + ".setBeanWrapperContent(" + parameter.getName() + ");");
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
		source.outdent();
		source.println("}");
		source.println();
	}

	private boolean isNotBean(JType type) {
		JClassType collection = typeOracle.findType(Collection.class.getCanonicalName());
		JClassType map = typeOracle.findType(Map.class.getCanonicalName());
		JClassType number = typeOracle.findType(Number.class.getCanonicalName());

		boolean isCollection = false, isMap = false, isNumber = false;
		if (type instanceof JClassType) {
			isCollection = ((JClassType) type).isAssignableTo(collection);
			isMap = ((JClassType) type).isAssignableTo(map);
			isNumber = ((JClassType) type).isAssignableTo(number);
		}
		return type.isPrimitive() != null || type.isEnum() != null || type.isArray() != null || isCollection || isNumber || isMap
				|| type.getSimpleSourceName().equals("String") || type.getSimpleSourceName().equals("Byte") || type.getSimpleSourceName().equals("Short")
				|| type.getSimpleSourceName().equals("Integer") || type.getSimpleSourceName().equals("Long") || type.getSimpleSourceName().equals("Boolean")
				|| type.getSimpleSourceName().equals("Float") || type.getSimpleSourceName().equals("Double") || type.getSimpleSourceName().equals("Number")
				|| type.getSimpleSourceName().equals("Character") || type.getSimpleSourceName().equals("Date")
				|| type.getSimpleSourceName().equals("Timestamp");
	}

	protected boolean isExcludedMethod(JMethod method) {
		return method == null;
	}

	private boolean isGetter(JMethod methode) {
		return (methode.getName().startsWith("get") || methode.getName().startsWith("is")) && methode.getParameters().length == 0;
	}

	private boolean isSetter(JMethod methode) {
		return methode.getName().startsWith("set") && methode.getParameters().length == 1;
	}

	protected String[] getImports() {
		return new String[] {BeanWrapper.class.getCanonicalName(), IObservableObject.class.getCanonicalName(), PropertyChangeSupport.class.getCanonicalName(),
				PropertyChangeListener.class.getCanonicalName(), GWT.class.getCanonicalName(), classType.getQualifiedSourceName()};
	}

	protected String[] getImplementedInterfaces() {
		return new String[] {HasPropertyChangeSupport.class.getCanonicalName(), classType.getSimpleSourceName()};
	}

	protected String getSuperclassName() {
		return superclassName;
	}

	protected String castToString(JType type, String value) {
		return value;
	}

	protected String defaultValue(JType type) {
		if (type.getSimpleSourceName().equals("String")) {
			return null;
		} else if (type.getSimpleSourceName().equals("int")) {
			return "0";
		} else if (type.getSimpleSourceName().equals("byte")) {
			return "Byte.valueOf(\"0\").byteValue()";
		} else if (type.getSimpleSourceName().equals("short")) {
			return "Short.valueOf(\"0\").shortValue()";
		} else if (type.getSimpleSourceName().equals("long")) {
			return "0L";
		} else if (type.getSimpleSourceName().equals("float")) {
			return "0f";
		} else if (type.getSimpleSourceName().equals("double")) {
			return "0d";
		} else if (type.getSimpleSourceName().equals("boolean")) {
			return "false";
		} else if (type.getSimpleSourceName().equals("char")) {
			return "''";
		} else {
			return "null";
		}
	}

	protected String castFromString(JType type, String value) {
		if (type.isPrimitive() != null) {
			JPrimitiveType primitiveType = type.isPrimitive();
			return "(" + primitiveType.getQualifiedBoxedSourceName() + ")" + value;
		}

		return "(" + type.getQualifiedSourceName() + ")" + value;

	}
}