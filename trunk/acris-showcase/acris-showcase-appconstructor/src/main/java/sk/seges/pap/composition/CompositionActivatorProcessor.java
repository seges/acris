/**
 * 
 */
package sk.seges.pap.composition;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.pivo.AbstractObjectFactory;
import sk.seges.acris.pivo.ChocolateFactory;
import sk.seges.acris.pivo.IDependency;
import sk.seges.acris.pivo.Reference;
import sk.seges.acris.scaffold.injection.ActivatorPart;
import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.acris.scaffold.mvp.SlotPresenter;
import sk.seges.acris.scaffold.mvp.SlotPresenter.SlotDispay;
import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.type.ActivatorPartType;
import sk.seges.pap.type.PanelType;
import sk.seges.pap.type.PresenterType;
import sk.seges.pap.type.ViewComposerAnnotation;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

/**
 * @author ladislav.gazo
 */
public class CompositionActivatorProcessor extends FluentProcessor {

	public CompositionActivatorProcessor() {
		reactsOn(ViewComposer.class);
		setSuperClass(ActivatorPart.class);
	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return new ActivatorPartType(inputType);
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
		final MutableDeclaredType modelType = (MutableDeclaredType) processingEnv
				.getTypeUtils()
				.toMutableType(context.getTypeElement().asType());
		ActivatorPartType activatorPartType = new ActivatorPartType(modelType);

		// constructor
		pw.println("public ", context.getOutputType().getSimpleName(), "(",
				ChocolateFactory.class, " moduleFactory) {");
		pw.println("super(moduleFactory);");
		pw.println("}");

		pw.println("public static final String ", modelType, " = \"",modelType,"\";");
		
		ViewComposerAnnotation annotation = new ViewComposerAnnotation(context.getTypeElement(), processingEnv);
		if(annotation.isValid()) {
			
		}
		
		pw.println("protected ",Object.class," construct", modelType,"(",Object.class,"... values) {");
		pw.println("return new ",SlotPresenter.class,"((",SlotDispay.class,") values[0],");
		
		doForAllMembers(context.getTypeElement(), ElementKind.INTERFACE, new SequentialElementAction<TypeElement>() {

			@Override
			public void execute(TypeElement element) {
				PanelType panelType = new PanelType((MutableDeclaredType) processingEnv
						.getTypeUtils()
						.toMutableType(element.asType()));
				PresenterType presenterType = new PresenterType((MutableDeclaredType) processingEnv
						.getTypeUtils()
						.toMutableType(element.asType()));
				pw.print("new ",presenterType,"(new ",panelType,"())");
				if(!last) {
					pw.print(",");
				}
				
			}
		});
		
		pw.print(");");
		pw.println("}");
		
		pw.println("public void ", modelType, ScaffoldConstant.PRES_SUFFIX,"() {");
		
		pw.println("moduleFactory.registerChocolate(",modelType,", new ",AbstractObjectFactory.class,"(moduleFactory) {");
		pw.println("@",Override.class);
		pw.println("protected ",Object.class," construct(",Object.class,"... values) {");
		pw.println("return construct", modelType,"(values);");		
		pw.println("}");
		
		pw.println("@",Override.class);			
		pw.println("protected void initDependencies() {");
		//pw.println("dependencies = new ",IDependency.class,"[] {new ",Reference.class,"(CHANNEL_MANAGEMENT_PANEL)};");
		pw.println("dependencies = new ",IDependency.class,"[] {new ",Reference.class,"(\"slotPanel\")};");
		pw.println("}");
		pw.println("});");
		
		
		pw.println("}");
	}

}
