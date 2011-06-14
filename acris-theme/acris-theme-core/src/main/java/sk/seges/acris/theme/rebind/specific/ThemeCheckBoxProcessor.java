package sk.seges.acris.theme.rebind.specific;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import sk.seges.acris.theme.client.annotation.ThemeSupport;

import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.CheckBoxHelper;

public class ThemeCheckBoxProcessor extends AbstractComponentSpecificProcessor {

	@Override
	public Type[] getImports() {
		return new Type[] {
				LabelElement.class,
				CheckBoxHelper.class
		};
	}
	
	@Override
	public void process(Statement statement, ThemeSupport themeSupport, PrintWriter pw) {

		switch (statement) {
			case CONSTRUCTOR:
				pw.println("component.parentElement.appendChild(component." + themeSupport.elementName() + ");");
				pw.println("component.getElement().getChild(0).appendChild(getLabelElement());");
				break;

			case CLASS:
				pw.println("protected " + LabelElement.class.getSimpleName() + " getLabelElement() {");
				pw.println("return " + CheckBoxHelper.class.getSimpleName() + ".getLabelElement(this);");
				pw.println("}");
				pw.println("");
				break;

			default:
				break;
		}
	}

	@Override
	protected Class<?> getComponentClass() {
		return CheckBox.class;
	}
}