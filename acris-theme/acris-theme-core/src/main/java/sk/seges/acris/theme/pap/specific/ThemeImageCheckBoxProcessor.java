package sk.seges.acris.theme.pap.specific;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.widget.client.form.ImageCheckBox;

import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.CheckBoxHelper;

public class ThemeImageCheckBoxProcessor extends AbstractComponentSpecificProcessor {

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

			case SUPER_CONSTRUCTOR_ARGS:
				pw.print(", component.resources, \"" + themeSupport.themeName() + "-\"");
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
		return ImageCheckBox.class;
	}
}