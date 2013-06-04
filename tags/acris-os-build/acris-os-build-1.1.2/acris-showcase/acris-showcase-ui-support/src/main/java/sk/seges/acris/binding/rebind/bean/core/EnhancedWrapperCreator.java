package sk.seges.acris.binding.rebind.bean.core;

import sk.seges.acris.binding.client.model.BeanWrapperManager;
import sk.seges.acris.binding.rebind.bean.BeanWrapperCreator;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;
import sk.seges.acris.core.rebind.RebindUtils;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.dev.javac.typemodel.JMethodHelper;
import com.google.gwt.user.rebind.SourceWriter;

public abstract class EnhancedWrapperCreator extends BeanWrapperCreator {

	public EnhancedWrapperCreator(BindingNamingStrategy nameStrategy) {
		super(nameStrategy);
	}

	@Override
	protected void generateWrapperMethods(SourceWriter source, String simpleName, JMethod[] methods) throws NotFoundException {
		source.println("public void setBeanWrapperContent(" + simpleName + " " + BEAN_WRAPPER_CONTENT + ") {");
		source.indent();
		source.println("this." + BEAN_WRAPPER_CONTENT + " = (" + simpleName + ") " + BEAN_WRAPPER_CONTENT + ";");
		source.println("clearWrappers();");

		for (JMethod method : methods) {
			if (method.getName().startsWith("set")) {

				String fieldName = RebindUtils.toFieldName(method.getName());

				JMethod getter = RebindUtils.getGetter(beanType, fieldName);

				if (getter != null) {
					source.println(method.getName() + "(" + BEAN_WRAPPER_CONTENT + "." + getter.getName() + "());");
				}
			}
		}

		source.outdent();
		source.println("}");
		source.println();
		source.println("public " + simpleName + " getBeanWrapperContent() {");
		source.indent();

		source.println("if (this." + BEAN_WRAPPER_CONTENT + " == null) {");
		source.indent();
		source.println("return null;");
		source.outdent();
		source.println("}");
		source.println("");

		for (JMethod method : methods) {
			if (method.getName().startsWith("get")) {

				String fieldName = RebindUtils.toFieldName(method.getName());

				JMethod setter = RebindUtils.getSetter(beanType, fieldName, method.getReturnType());

				if (setter != null) {
					if (method.getReturnType().isClassOrInterface() != null) {
						JClassType classType = method.getReturnType().isClassOrInterface();

						if (hasBeanWrapper(classType)) {
							source.println("if (" + method.getName() + "() != null && " + BEAN_WRAPPER_CONTENT + "." + method.getName() + "() == null) {");
							source.indent();
							source.println(BEAN_WRAPPER_CONTENT + "." + setter.getName() + "(" + method.getName() + "().getBeanWrapperContent());");
							source.outdent();
							source.println("}");
						} else {
							source.println(BEAN_WRAPPER_CONTENT + "." + setter.getName() + "(" + method.getName() + "());");
						}
					} else {
						source.println(BEAN_WRAPPER_CONTENT + "." + setter.getName() + "(" + method.getName() + "());");
					}
				}
			}
		}
		source.println("return this." + BEAN_WRAPPER_CONTENT + ";");
		source.outdent();
		source.println("}");
	}

	protected boolean hasBeanWrapper(JClassType classType) {

		String resultName = getWrapperClassName(classType.getQualifiedSourceName());
		try {
			typeOracle.getType(resultName);
			return true;
		} catch (NotFoundException e) {}

		return false;
	}

	protected abstract void generateAttributeSetter(SourceWriter source, String name, String value);
	
	@Override
	protected void generateSetterForPrimitive(SourceWriter source, JMethod methode, JParameter parameter, JMethod getter) {
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();

		source.println(parameter.getType().getQualifiedSourceName() + " oldValue = " + getter.getName() + "();");
		generateAttributeSetter(source, "\"" + RebindUtils.toFieldName(methode.getName()) + "\"", parameter.getName());
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
		source.outdent();
		source.println("}");
		source.println();
	}

	protected abstract void getAttributeGetter(SourceWriter source, String param, String typeSimpleName, String typeQualifiedName);

	@Override
	protected void generateGetterForPrimitive(SourceWriter source, JMethod methode) {
		source.println(methode.getReadableDeclaration(false, false, false, false, true) + " {");
		source.indent();
		getAttributeGetter(source, RebindUtils.toFieldName(methode.getName()), 
				methode.getReturnType().getSimpleSourceName(), methode.getReturnType().getQualifiedSourceName());
		source.outdent();
		source.println("}");
		source.println();
	}

	@Override
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

		source.println(wrapperType + " wrapper = " + BeanWrapperManager.class.getCanonicalName() + ".<" + parameter.getType().getQualifiedSourceName()
				+ ">getWrapper(" + parameter.getName() + ");");
		source.println("if (wrapper != null) {");
		source.indent();
		source.println("this." + field + " = wrapper;");
		source.outdent();
		source.println("} else {");
		source.indent();
		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + wrapperType + ") GWT.create(" + getWrapperClassName(returnType.getQualifiedSourceName()) + ".class);");
		source.println(BeanWrapperManager.class.getCanonicalName() + ".putWrapper(" + parameter.getName() + ", this." + field + ");");
		source.outdent();
		source.println("}");
		source.println("this." + field + ".setBeanWrapperContent(" + parameter.getName() + ");");
		source.outdent();
		source.println("}");
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
		source.outdent();
		source.println("}");
		source.println();
	}

	@Override
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

		source.println(new JMethodHelper((com.google.gwt.dev.javac.typemodel.JMethod) methode).getReadableDeclaration(wrapperType, false, false, false, false, true) + " {");
		//		source.println(methode.getReadableDeclaration() + " {");
		source.indent();

		String methodName = BEAN_WRAPPER_CONTENT + "." + methode.getName() + "()";

		source.println("if (" + methodName + " == null) {");
		source.indent();
		source.println("return null;");
		source.outdent();
		source.println("}");

		source.println(wrapperType + " wrapper = " + BeanWrapperManager.class.getCanonicalName() + ".<" + returnType.getQualifiedSourceName() + ">getWrapper("
				+ methodName + ");");
		source.println("if (wrapper != null) {");
		source.indent();
		source.println("this." + field + " = wrapper;");
		source.outdent();
		source.println("} else {");
		source.indent();
		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + wrapperType + ") GWT.create(" + getWrapperClassName(returnType.getQualifiedSourceName()) + ".class);");
		source.println(BeanWrapperManager.class.getCanonicalName() + ".putWrapper(" + methodName + ", this." + field + ");");
		source.outdent();
		source.println("}");
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

}