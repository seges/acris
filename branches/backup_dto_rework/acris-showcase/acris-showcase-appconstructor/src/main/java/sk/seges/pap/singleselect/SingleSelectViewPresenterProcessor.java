/**
 * 
 */
package sk.seges.pap.singleselect;

import java.util.List;

import sk.seges.acris.mvp.AbstractDisplayAwareActivity;
import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.acris.scaffold.mvp.DefaultViewConfiguration;
import sk.seges.pap.type.DisplayType;
import sk.seges.pap.type.PresenterType;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author ladislav.gazo
 */
public class SingleSelectViewPresenterProcessor extends FluentProcessor {
	private final DefaultViewConfiguration defaultConfiguration = new DefaultViewConfiguration();

	public SingleSelectViewPresenterProcessor() {
		reactsOn(Singleselect.class);
		setSuperClass(new AlwaysRule() {
			@Override
			public List<MutableDeclaredType> getTypes(
					MutableDeclaredType typeElement) {
				return asList(toParametrizedMutableDeclaredType(
						AbstractDisplayAwareActivity.class, new DisplayType(
								typeElement)));
			}
		});

	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return new PresenterType(inputType);
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
		MutableDeclaredType modelType = (MutableDeclaredType) processingEnv
				.getTypeUtils()
				.toMutableType(context.getTypeElement().asType());
		DisplayType displayType = new DisplayType(modelType);

		// constructor
		pw.println("public ", context.getOutputType().getSimpleName(), "(",
				displayType, " display) {");
		pw.println("super(display);");
		pw.println("}");

		// methods
		pw.println("@", Override.class);
		pw.println("public void start(", AcceptsOneWidget.class, " panel, ",
				EventBus.class, " eventBus) {");
		pw.println("refresh();");
		pw.println("panel.setWidget(display);");
		pw.println("}");

		pw.println("public void refresh() {");
		pw.println("}");

		System.out.println("");
	}
}
