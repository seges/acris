/**
 * 
 */
package sk.seges.acris.binding.rebind.binding;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generates GWT bean info bodies for defined types. Overriding of
 * java.lang.reflect.Method is done here but is allowed only in production mode
 * where the Method is emulated and is not final.
 * 
 * @author eldzi
 */
public class BeanPropertyDescriptorCreator {
	private TreeLogger logger = null;

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

	protected Collection<Property> lookupJavaBeanPropertyAccessors(JClassType type) {
		Map<String, Property> properties = new HashMap<String, Property>();

		JMethod[] methods = type.getOverridableMethods();
		for (JMethod method : methods) {
			if (!method.isPublic() || method.isStatic()) {
				continue;
			}
			if (method.getName().startsWith("set") && method.getParameters().length == 1) {
				String name = Introspector.decapitalize(method.getName().substring(3));
				String propertyType = null;
				JParameter[] parameters = method.getParameters();
				if (parameters.length == 1) {
					JParameter parameter = parameters[0];
					propertyType = parameter.getType().getErasedType().getQualifiedSourceName();
				} else {
					logger.log(Type.WARN, "Property '" + name + "' has " + parameters.length
							+ " parameters: " + parameters + "!");
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
					logger.log(Type.WARN, "Property '" + name + "' has an invalid setter: " + propertyType
							+ " was excpected, " + property.propertyType + " found!");
					continue;
				}
			} else if (method.getName().startsWith("get") && method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName().substring(3));
				String propertyType = method.getReturnType().getErasedType().getQualifiedSourceName();
				Property property = properties.get(name);
				if (property == null) {
					property = new Property(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					logger.log(Type.WARN, "Property '" + name + "' has an invalid getter: " + propertyType
							+ " was excpected, " + property.propertyType + " found!");
					continue;
				}
			} else if (method.getName().startsWith("is") && method.getParameters().length == 0) {
				String name = Introspector.decapitalize(method.getName().substring(2));
				String propertyType = method.getReturnType().getErasedType().getQualifiedSourceName();
				Property property = properties.get(name);
				if (property == null) {
					property = new Property(name);
					properties.put(name, property);
				}
				property.getter = method;
				if (property.propertyType == null) {
					property.propertyType = propertyType;
				} else if (!property.propertyType.equals(propertyType)) {
					logger.log(Type.WARN, "Property '" + name + "' has an invalid (is) getter: "
							+ propertyType + " was excpected, " + property.propertyType + " found!");
					continue;
				}
			}
		}
		return properties.values();
	}

	public void write(TreeLogger logger, SourceWriter w, List<JClassType> beanList) {
		this.logger = logger;
		w.println("public static void setupBeanInfo() throws " + IntrospectionException.class.getName());
		w.println("{");
		w.indent();
		w.println("GwtBeanInfo beanInfo;");
		for (JClassType type : beanList) {
			Collection<Property> properties = lookupJavaBeanPropertyAccessors(type);
			// if(logger.isLoggable(Type.DEBUG)) {
			// logger.log(Type.WARN, "Found # properties = " + properties.size()
			// + " for type = " + type.getParameterizedQualifiedSourceName());
			// }
			w.println("\n// " + type.getName());
			w.println("beanInfo = new GwtBeanInfo();");
			for (Property property : properties) {
				w.print("beanInfo.addPropertyDescriptor( ");
				writePropertyDescriptor(w, type, property.name, property.propertyType, property.getter,
						property.setter);
				w.println(" );");
			}
			w
					.println("GwtIntrospector.setBeanInfo( " + type.getQualifiedSourceName()
							+ ".class, beanInfo );");
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

	private void writePropertyDescriptor(SourceWriter sw, JClassType type, String propertyName,
			String propertyType, JMethod getter, JMethod setter) {
		sw.print("new PropertyDescriptor( \"" + propertyName + "\", " + propertyType + ".class, ");
		if (getter != null) {
			sw.println("new Method() ");
			sw.println("{");
			sw.indent();
			sw.println("public Object invoke( Object bean, Object... args )");
			sw.println("{");
			sw.indent();
			sw.println("return ( (" + type.getQualifiedSourceName() + ") bean)." + getter.getName() + "();");
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
				argTypeName = argType.isPrimitive().getQualifiedBoxedSourceName();
			} else {
				argTypeName = argType.getQualifiedSourceName();
			}
			sw.println("( (" + type.getQualifiedSourceName() + ") bean)." + setter.getName() + "( ("
					+ argTypeName + ") args[0] );");
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
