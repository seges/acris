package sk.seges.acris.recorder.rebind;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.recorder.rpc.bean.IAccessibleBean;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class AccessibleBeanPropertyDescriptorGenerator extends Generator {

	private class Property {
		public String name;
		public String propertyType;
		public JMethod getter;
		public JMethod setter;

		public Property(String name) {
			super();
			this.name = name;
		}
	}

	/**
	 * The {@code TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	public String doGenerate(String typeName, List<JClassType> types)
			throws NotFoundException {
		TypeOracle typeOracle = context.getTypeOracle();
		JClassType type = typeOracle.getType(typeName);
		String packageName = type.getPackage().getName();
		String simpleClassName = type.getSimpleSourceName();
		String className = simpleClassName + "Introspector";
		String qualifiedBeanClassName = packageName + "." + className;
		SourceWriter sourceWriter = getSourceWriter(packageName, className,
				type);
		if (sourceWriter == null) {
			return qualifiedBeanClassName;
		}
		write(sourceWriter, type, types);
		sourceWriter.commit(logger);
		return qualifiedBeanClassName;
	}

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		// Get the subtypes to examine
		JClassType type = null;
		try {
			type = context.getTypeOracle().getType(
					IAccessibleBean.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}
		
		List<JClassType> types = new ArrayList<JClassType>();

		for (JClassType subtype : type.getSubtypes()) {
			types.add(subtype);
		}

		try {
			type = context.getTypeOracle().getType(
					IDomainObject.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}

		for (JClassType subtype : type.getSubtypes()) {
			types.add(subtype);
		}
		
		try {
			logger.log(Type.INFO, "Enable " + typeName + " for introspection");
			return doGenerate(typeName, types);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, null, e);
			throw new UnableToCompleteException();
		}
	}

	protected SourceWriter getSourceWriter(String packageName,
			String beanClassName, JClassType superType) {
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				beanClassName);
		if (printWriter == null) {
			return null;
		}
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				packageName, beanClassName);

		composerFactory.addImport(PropertyDescriptor.class.getName());
		composerFactory.addImport(Method.class.getName());
		// Do not use GwtBeanInfo.class as the BeanInfo interface is NOT fully
		// implemented
		composerFactory
				.addImport("com.googlecode.gwtx.java.introspection.client.GwtBeanInfo");
		composerFactory
				.addImport("com.googlecode.gwtx.java.introspection.client.GwtIntrospector");

		return composerFactory.createSourceWriter(context, printWriter);
	}

	protected Collection<Property> lookupJavaBeanPropertyAccessors(
			JClassType type) {
		Map<String, Property> properties = new HashMap<String, Property>();

		JMethod[] methods = type.getOverridableMethods();
		for (JMethod method : methods) {
			if (!method.isPublic() || method.isStatic()) {
				continue;
			}
			if (method.getName().startsWith("set")
					&& method.getParameters().length == 1) {
				String name = Introspector.decapitalize(method.getName()
						.substring(3));
				String propertyType = null;
				JParameter[] parameters = method.getParameters();
				if (parameters.length == 1) {
					JParameter parameter = parameters[0];
					propertyType = parameter.getType().getErasedType()
							.getQualifiedSourceName();
				} else {
					logger.log(Type.WARN, "Property '" + name + "' has "
							+ parameters.length + " parameters: " + parameters
							+ "!");
					continue;
				}
				Property property = properties.get(name);
				if (property == null) {
					property = new Property(name);
					properties.put(name, property);
				}
				property.setter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					logger.log(Type.WARN, "Property '" + name
							+ "' has an invalid setter: " + propertyType
							+ " was excpected, " + property.propertyType
							+ " found!");
					continue;
				}
			} else if (method.getName().startsWith("get")
					&& method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName()
						.substring(3));
				String propertyType = method.getReturnType().getErasedType()
						.getQualifiedSourceName();
				Property property = properties.get(name);
				if (property == null) {
					property = new Property(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					logger.log(Type.WARN, "Property '" + name
							+ "' has an invalid getter: " + propertyType
							+ " was excpected, " + property.propertyType
							+ " found!");
					continue;
				}
			} else if (method.getName().startsWith("is")
					&& method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName()
						.substring(2));
				String propertyType = method.getReturnType().getErasedType()
						.getQualifiedSourceName();
				Property property = properties.get(name);
				if (property == null) {
					property = new Property(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					logger.log(Type.WARN, "Property '" + name
							+ "' has an invalid (is) getter: " + propertyType
							+ " was excpected, " + property.propertyType
							+ " found!");
					continue;
				}
			}
		}
		return properties.values();
	}

	private void write(SourceWriter w, JClassType _type, List<JClassType> types) {
		w.println("public static void setupBeanInfo() throws "
				+ IntrospectionException.class.getName());
		w.println("{");
		w.indent();
		w.println("GwtBeanInfo beanInfo;");
		for (JClassType type : types) {
			Collection<Property> properties = lookupJavaBeanPropertyAccessors(type);
			w.println("\n// " + type.getName());
			w.println("beanInfo = new GwtBeanInfo();");
			for (Property property : properties) {
				w.print("beanInfo.addPropertyDescriptor( ");
				writePropertyDescriptor(w, type, property.name,
						property.propertyType, property.getter, property.setter);
				w.println(" );");
			}
			w.println("GwtIntrospector.setBeanInfo( "
					+ type.getQualifiedSourceName() + ".class, beanInfo );");
		}
		w.outdent();
		w.println("}");

		// automatically invoke setupBeanInfo()
		w.println("static {");
		w.indent();
		w
				.println("try{setupBeanInfo();}catch(Exception ex){com.google.gwt.user.client.Window.alert(ex.getMessage());}");
		w.outdent();
		w.println("}");
	}

	private void writePropertyDescriptor(SourceWriter sw, JClassType type,
			String propertyName, String propertyType, JMethod getter,
			JMethod setter) {
		sw.print("new PropertyDescriptor( \"" + propertyName + "\", "
				+ propertyType + ".class, ");
		if (getter != null) {
			sw.println("new Method() ");
			sw.println("{");
			sw.indent();
			sw.println("public Object invoke( Object bean, Object... args )");
			sw.println("{");
			sw.indent();
			sw.println("return ( (" + type.getQualifiedSourceName()
					+ ") bean)." + getter.getName() + "();");
			sw.outdent();
			sw.println("}");
			sw.outdent();
			sw.print("}, ");
		} else {
			sw.print("null, ");
		}
		if (setter != null) {
			sw.println("new Method() ");
			sw.println("{");
			sw.indent();
			sw.println("public Object invoke( Object bean, Object... args )");
			sw.println("{");
			sw.indent();
			JType argType = setter.getParameters()[0].getType().getErasedType();
			String argTypeName;
			if (argType.isPrimitive() != null) {
				argTypeName = argType.isPrimitive()
						.getQualifiedBoxedSourceName();
			} else {
				argTypeName = argType.getQualifiedSourceName();
			}
			sw.println("( (" + type.getQualifiedSourceName() + ") bean)."
					+ setter.getName() + "( (" + argTypeName + ") args[0] );");
			sw.println("return null;");
			sw.outdent();
			sw.println("}");
			sw.outdent();
			sw.print("} )");
		} else {
			sw.print("null )");
		}
	}
}
