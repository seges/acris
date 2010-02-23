/**
 * 
 */
package sk.seges.acris.binding.rebind.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.binding.rebind.RebindUtils;
import sk.seges.sesam.domain.IObservableObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author eldzi
 */
public class BeanWrapperCreator {
	private static final String WRAPPER_XXX = "Wrapper___xxx";
	private static final String CONTENU_FIELD = "contenu";
	private TreeLogger logger;
	private GeneratorContext context;
	private TypeOracle typeOracle;
	private String typeName;
	private String superclassName;

	public BeanWrapperCreator(TreeLogger logger, GeneratorContext context,
			String typeName, String superclassName) {
		this.superclassName = superclassName;
		this.logger = logger;
		this.context = context;
		this.typeOracle = context.getTypeOracle();
		this.typeName = typeName;
	}
	
	public String createWrapper() {
		JClassType classType, originalType, objectType;
		try {
			classType = originalType = typeOracle.getType(typeName);
			objectType = typeOracle.getType(Object.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Unable to find type name = " + typeName, e);
			return null;
		}
		
		SourceWriter source = getSourceWriter(originalType);

		List<JMethod> allMethods = new ArrayList<JMethod>();
		
		while(classType != null && !classType.equals(objectType)) {
//			logger.log(TreeLogger.WARN, "Creating bean wrapper " + classType.getParameterizedQualifiedSourceName());

			JMethod[] methods = classType.getMethods();
			boolean found = false;
			for(JMethod method : methods) {
				if(!(isGetter(method) || isSetter(method))) {
					continue;
				}
				if(method.isFinal()) {
					continue;
				}
				if(method.isAbstract()) {
					// TODO: handle abstract classes - when there is a wrapper
					// over abstract class, do implement a method that will
					// delegate to it but without the abstract modifier...
					continue;
				}

				if(method.isPrivate() || method.isProtected()) {
					continue;
				}
				
				for(JMethod allMethod : allMethods) {
					if(allMethod.getReadableDeclaration().compareTo(method.getReadableDeclaration()) == 0) {
						found = true;
					}
				}
				if(!found) {
					allMethods.add(method);
				} else {
					found = false;
				}
			}
			
			classType = classType.getSuperclass();
//			logger.log(TreeLogger.WARN, "Super is bean wrapper " + classType);
		}
		
		String simpleName = originalType.getSimpleSourceName();
		JMethod[] methods = allMethods.toArray(new JMethod[0]); 
		
		try {
			createWrapper(methods, simpleName, source, originalType);
		} catch(NotFoundException e) {
			throw new RuntimeException(e);
		}
		
		return originalType.getParameterizedQualifiedSourceName() + "Wrapper";
	}
	
	
	public void createWrapper(JMethod[] methods, String simpleName, SourceWriter source, JClassType classType) throws NotFoundException {
		if (source != null) {
			source.indent();
			if(superclassName == null) {
				source.println("private " + simpleName + " " + CONTENU_FIELD + ";");
			} else {
				// initialize new instance by default
				// usually this happen when there is Gilead proxied domain object
				source.println("private " + simpleName + " " + CONTENU_FIELD + " = new " + superclassName + "();");
			}
			source.println();
			source.println("private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);");
			source.println();
			source
					.println("public void setContent(Object " + CONTENU_FIELD + ") {");
			source.indent();
			source.println("this." + CONTENU_FIELD + " = (" + simpleName + ") " + CONTENU_FIELD + ";");
			source.outdent();
			source.println("}");
			source.println();
			source.println("public Object getContent() {");
			source.indent();
			source.println("return this." + CONTENU_FIELD + ";");
			source.outdent();
			source.println("}");
			
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
						
			// create links with content object getters and setters
			for (int i = 0; i < methods.length; i++) {
				if(isExcludedMethod(methods[i])) {
					// there was a method but was excluded
					continue;
				}

				JMethod methode = methods[i];
				String fieldName = RebindUtils.toFieldName(methode.getName());
				if (methode.getName().startsWith("set")) { // setter
					JParameter parameter = methode.getParameters()[0];
					JMethod getter = RebindUtils.getGetter(classType, fieldName);
					if(getter == null) {
						// this is not regular setter with getter counterpart.
						// We don't generate wrapper methods for that.
						methods[i] = null;
						continue;
					}
					if(isNotBean(parameter.getType()) || classType.findField(fieldName) == null) {
						generateSetterForPrimitive(source, methode, parameter, getter);
					} else {
						generateSetterForBean(source, methode, parameter, getter);
					}
				} else { // getter
					if(isNotBean(methode.getReturnType()) || classType.findField(fieldName) == null) {
						generateGetterForPrimitive(source, methode);
					} else {
						generateGetterForBean(source, methode);
					}
				}
				source.outdent();
				source.println("}");
				source.println();
			}
			
			// create the getAttribute method
			source.println("public Object getAttribute(String attr) {");
			source.indent();

			for (int i = 0; i < methods.length; i++) {
				if(isExcludedMethod(methods[i])) {
					// there was a method but was excluded
					continue;
				}
				String methodName = methods[i].getName();
				JType returnType = methods[i].getReturnType();
				if (isGetter(methods[i])) {
					source.println("if (attr.equals(\""
							+ RebindUtils.toFieldName(methodName)
							+ "\")) {");
					source.indent();
					source.println("return "
							+ castToString(returnType, "this." + methodName
									+ "()") + ";");
					source.outdent();
					source.print("} else ");
				}
			}
			source.println("{");
			source.indent();
			source.println("return null;");
			source.outdent();
			source.println("}");
			source.outdent();
			source.println("}");
			source.println();
			// create the set attribute method
			source
					.println("public void setAttribute(String attr, Object value) {");
			source.indent();
			for (int i = 0; i < methods.length; i++) {
				if(isExcludedMethod(methods[i])) {
					// there was a method but was excluded
					continue;
				}

				JMethod methode = methods[i];
				if (isSetter(methode)) {
					JType paramType = methode.getParameters()[0].getType();
					source.println("if (attr.equals(\""
							+ RebindUtils.toFieldName(methode.getName())
							+ "\")) { ");
					source.indent();
					source.println("this." + methode.getName() + "("
							+ castFromString(paramType, "value") + ");");
					source.outdent();
					source.print("} else ");
				}
			}
			source.println("{");
			source.println("}");
			source.outdent();
			source.println("}");
			
			source.println("@Override");
			source.println("public int hashCode() {");
			source.indent();
			source.println("if(" + CONTENU_FIELD + " != null) {");
			source.indent();
			source.println("return " + CONTENU_FIELD + ".hashCode();");
			source.outdent();
			source.println("} else {");
			source.indent();
			source.println("return super.hashCode();");
			source.outdent();
			source.println("}");
			source.outdent();
			source.println("}");
			
			source.println("@Override");
			source.println("public boolean equals(Object obj) {");
			source.indent();
			source.println("if(obj instanceof BeanWrapper) {");
			source.indent();
			source.println("return equals(((BeanWrapper)obj).getContent());");
			source.outdent();
			source.println("}");  
			source.println("if(" + CONTENU_FIELD + " != null) {");
			source.indent();
			source.println("return " + CONTENU_FIELD + ".equals(obj);");
			source.outdent();
			source.println("} else {");
			source.indent();
			source.println("return super.equals(obj);");
			source.outdent();
			source.println("}");
			source.outdent();
			source.println("}");
			
			source.commit(logger);
		}
	}

	private void generateGetterForPrimitive(SourceWriter source, JMethod methode) {
		source.println(methode.getReadableDeclaration() + " {");
		source.indent();

		source.println("return " + CONTENU_FIELD + "." + methode.getName()
				+ "();");
	}

	private String getBeanWrapperType(JType returnType) {
		return BeanWrapper.class.getName() + "<" + returnType.getQualifiedSourceName() + ">";
	}
	
	private void generateGetterForBean(SourceWriter source, JMethod methode) {
		String field = RebindUtils.toFieldName(methode.getName());
		JType returnType = methode.getReturnType();
		
		field = field + WRAPPER_XXX;
		String beanWrapperType = getBeanWrapperType(returnType);
		source.println("private " + beanWrapperType + " " + field + ";");
		
		source.println(methode.getReadableDeclaration() + " {");
		source.indent();
		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + beanWrapperType + ") GWT.create(" + returnType.getQualifiedSourceName() + ".class);");
		source.println("this." + field + ".setContent(" + CONTENU_FIELD + "." + methode.getName() + "());");
		source.outdent();
		source.println("}");
		source.println("return (" + returnType.getQualifiedSourceName() + ")this." + field + ";");		
	}

	private void generateSetterForPrimitive(SourceWriter source, JMethod methode, JParameter parameter, JMethod getter) {
		source.println(methode.getReadableDeclaration() + " {");
		source.indent();

		source.println(parameter.getType().getQualifiedSourceName() + " oldValue = " + getter.getName() + "();");
		source.println(CONTENU_FIELD + "." + methode.getName() + "("
				+ parameter.getName() + ");");
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
	}
	
	private void generateSetterForBean(SourceWriter source, JMethod methode, JParameter parameter, JMethod getter) {
		String field = RebindUtils.toFieldName(methode.getName()) + WRAPPER_XXX;
		JType returnType = parameter.getType();
		
		String beanWrapperType = getBeanWrapperType(returnType);
		source.println(methode.getReadableDeclaration() + " {");
		source.indent();

		source.println(parameter.getType().getQualifiedSourceName() + " oldValue = " + CONTENU_FIELD + "." + getter.getName() + "();");
		source.println(CONTENU_FIELD + "." + methode.getName() + "("
				+ parameter.getName() + ");");
		
		source.println("if(this." + field + " == null) {");
		source.indent();
		source.println("this." + field + " = (" + beanWrapperType + ") GWT.create(" + returnType.getQualifiedSourceName() + ".class);");
		source.outdent();
		source.println("}");
		source.println("this." + field + ".setContent(" + parameter.getName() + ");");		
		source.println("pcs.firePropertyChange(\"" + parameter.getName() + "\", oldValue, " + parameter.getName() + ");");
	}

	private boolean isNotBean(JType type) {
		JClassType collection = typeOracle.findType(Collection.class.getCanonicalName());
		JClassType map = typeOracle.findType(Map.class.getCanonicalName());
		
		boolean isCollection = false, isMap = false;
		if(type instanceof JClassType) {
			isCollection = ((JClassType) type).isAssignableTo(collection);
			isMap = ((JClassType) type).isAssignableTo(map);
		}
		return type.isPrimitive() != null || type.isEnum() != null || type.isArray() != null || isCollection
				|| isMap || type.getSimpleSourceName().equals("String")
				|| type.getSimpleSourceName().equals("Byte") || type.getSimpleSourceName().equals("Short")
				|| type.getSimpleSourceName().equals("Integer") || type.getSimpleSourceName().equals("Long")
				|| type.getSimpleSourceName().equals("Boolean") || type.getSimpleSourceName().equals("Float")
				|| type.getSimpleSourceName().equals("Double") || type.getSimpleSourceName().equals("Number")
				|| type.getSimpleSourceName().equals("Character")
				|| type.getSimpleSourceName().equals("Date")
				|| type.getSimpleSourceName().equals("Timestamp");
	}
	
	private boolean isExcludedMethod(JMethod method) {
		return method == null;
	}

	private boolean isGetter(JMethod methode) {
		return (methode.getName().startsWith("get") || methode.getName()
				.startsWith("is"))
				&& methode.getParameters().length == 0;
	}
	
	private boolean isSetter(JMethod methode) {
		return methode.getName().startsWith("set") && methode.getParameters().length == 1;
	}
	
	/**
	 * SourceWriter instantiation. Return null if the resource already exist.
	 * 
	 * @return sourceWriter
	 */
	public SourceWriter getSourceWriter(JClassType classType) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Wrapper";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.setSuperclass(superclassName);
		composer
				.addImplementedInterface(BeanWrapper.class.getName());
		composer
		.addImplementedInterface(HasPropertyChangeSupport.class.getCanonicalName());
		composer.addImport(BeanWrapper.class.getCanonicalName());
		composer.addImport(IObservableObject.class.getCanonicalName());
		composer.addImport(PropertyChangeSupport.class.getCanonicalName());
		composer.addImport(PropertyChangeListener.class.getCanonicalName());
		composer.addImport(GWT.class.getCanonicalName());
		
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

	public String castToString(JType type, String value) {
		return value;
		/*if (type.getSimpleSourceName().equals("String")) {
			return value;
		} else if (type.getSimpleSourceName().equals("int")) {
			return "Integer.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("byte")) {
			return "Byte.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("short")) {
			return "Short.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("long")) {
			return "Long.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("float")) {
			return "Float.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("double")) {
			return "Double.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("boolean")) {
			return "Boolean.toString(" + value + ")";
		} else if (type.getSimpleSourceName().equals("char")) {
			return "Character.toString(" + value + ")";
		} else {
			return "type not considered for the moment";
		}*/
	}

	public String castFromString(JType type, String value) {
		if (type.isPrimitive() != null) {
			JPrimitiveType primitiveType = type.isPrimitive();
			return "("+primitiveType.getQualifiedBoxedSourceName()+")"+value;
		}
		
		
		return "("+type.getQualifiedSourceName()+")"+value;

	}
}
