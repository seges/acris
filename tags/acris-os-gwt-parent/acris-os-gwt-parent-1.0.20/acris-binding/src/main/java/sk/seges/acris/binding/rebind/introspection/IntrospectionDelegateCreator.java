package sk.seges.acris.binding.rebind.introspection;

import java.beans.Introspector;

import sk.seges.acris.binding.client.init.BeanWrapperIntrospector;
import sk.seges.acris.binding.rebind.bean.BeanWrapperCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.rebind.TypeStrategy;

/**
 * @author ladislav.gazo
 * @author Peter Simun
 */
public class IntrospectionDelegateCreator {

	public void generateBeanInfoConnection(SourceWriter source, String wrapperType, String beanType) {
		final String gwtBeanInfo = "com.googlecode.gwtx.java.introspection.client.GwtBeanInfo";

		source.println("static {");
		source.indent();
		source.println(BeanWrapperIntrospector.class.getCanonicalName() + ".init();");
		source.println("try {");
		source.indent();

		final String introsp = Introspector.class.getCanonicalName();
		// in devel mode there is GenericBeanInfo instead of GwtBeanInfo and the following code cannot be executed - GwtIntrospector won't accept it
		source.println("if (" + GWT.class.getCanonicalName() + ".isScript()) {");
		source.indent();
		// connecting it with wrapper is probably useless and should be done in wrapper generator but to be sure it is also here....
		source.println(introsp + ".setBeanInfo( " + wrapperType + ".class, (" + gwtBeanInfo + ")" + introsp + ".getBeanInfo(" + beanType + ".class) );");
		source.outdent();
		source.println("}");

		source.outdent();
		source.println("} catch(Exception e) {");
		source.indent();
		source.println("throw new RuntimeException(\"Unable to initialize bean wrapper introspection.\", e);");
		source.outdent();
		source.println("}");
		source.outdent();
		source.println("}");
	}
}
