package sk.seges.acris.theme.pap.specific;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ThemeDefaultProcessor extends AbstractComponentSpecificProcessor {

	@Override
	protected ExecutableMethodDefinition[] getOuterMethodDefinitions() {
		return new ExecutableMethodDefinition[] {
				new ExecutableMethodDefinition("isAttached"),
				new ExecutableMethodDefinition("setVisible").param(TypeKind.BOOLEAN),
				new ExecutableMethodDefinition("getElement"),
				new ExecutableMethodDefinition("removeFromParent")
		};
	}

	@Override
	protected ExecutableMethodDefinition[] getIgnoredMethodDefinitions() {
		return new ExecutableMethodDefinition[] {
				new ExecutableMethodDefinition("getElement")
		};
	}
	
	@Override
	public void process(Statement statement, ThemeContext themeContext, FormattedPrintWriter pw) {

		switch (statement) {
			case CONSTRUCTOR:
				pw.println("this.component = component;");
				break;

			case SUPER_CONSTRUCTOR_ARGS:
				pw.print("(", Element.class, ")component." + themeContext.getThemeSupport().elementName());
				break;
				
			case CLASS:
				pw.println("protected ", com.google.gwt.user.client.Element.class, " getElement(String name) {");
				pw.println("if (component != null) {");
				pw.println("return component.getElement(name);");
				pw.println("}");
				pw.println("return null;");
				pw.println("}");
				pw.println();
				pw.println("@Override");
				pw.println("public ", com.google.gwt.user.client.Element.class, " getElement() {");
				pw.println("if (component == null) {");
				pw.println("return super.getElement();");
				pw.println("}");
				pw.println("if (componentOperation) {");
				pw.println("return (", Element.class, ")component." + themeContext.getThemeSupport().elementName() + ";");
				pw.println("}");
				pw.println("return component.getElement();");
				pw.println("}");
				break;

			default:
				break;
		}
	}

	public boolean supports(TypeElement typeElement) {
		return true;
	}
	
	@Override
	protected Class<?>[] getComponentClasses() {
		return new Class<?>[] {Widget.class};
	}	
}