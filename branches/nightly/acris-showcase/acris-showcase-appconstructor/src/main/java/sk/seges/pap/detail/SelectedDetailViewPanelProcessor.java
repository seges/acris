/**
 * 
 */
package sk.seges.pap.detail;

import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.pap.type.DisplayType;
import sk.seges.pap.type.PanelType;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class SelectedDetailViewPanelProcessor extends FluentProcessor {
	public SelectedDetailViewPanelProcessor() {
		reactsOn(SelectedDetail.class);
		setSuperClass(Composite.class);
		addImplementedInterface(new AlwaysRule() {
			@Override
			public List<MutableDeclaredType> getTypes(
					MutableDeclaredType typeElement) {
				return asList((MutableDeclaredType) new DisplayType(typeElement));
			}
		});
	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return new PanelType(inputType);
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
		// fields
		pw.println("private final ", FlowPanel.class, " container;");

		// constructor
		pw.println("public ", context.getOutputType().getSimpleName(), "() {");
		pw.println("container = new ", FlowPanel.class, "();");
		pw.println("initWidget(container);");

		doForAllMembers(context.getTypeElement(), ElementKind.METHOD,
				new FormFieldAction());

		pw.println("}");

		// methods

		pw.println("protected void addRow(", String.class, " labelString, ",
				Widget.class, " editor) {");
		pw.println(FlowPanel.class, " row = new ", FlowPanel.class, "();");
		pw.println(Label.class, " label = new ", Label.class, "(labelString);");
		pw.println("row.add(label);");
		pw.println("row.add(editor);");
		pw.println("container.add(row);");
		pw.println("}");
	}

	public class FormFieldAction extends MethodAction {

		@Override
		protected void doExecute(ExecutableElement element) {
			Name simpleName = element.getSimpleName();
			pw.println("addRow(\"", simpleName, "\", new ", TextBox.class, "());");
		}

	}
}
