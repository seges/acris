package sk.seges.corpis.appscaffold.datainterface.pap;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.datainterface.pap.configurer.DataInterfaceProcessorConfigurer;
import sk.seges.corpis.appscaffold.datainterface.pap.model.DataInterfaceType;
import sk.seges.corpis.appscaffold.shared.annotation.domain.Exclude;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

/**
 * @author ladislav.gazo
 */
public class DataInterfaceProcessor extends FluentProcessor {

	public static final String DATA_SUFFIX = "Data";
	public static final String MODEL_SUFFIX = "Model";

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new DataInterfaceType(context.getMutableType(), processingEnv)
		};
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DataInterfaceProcessorConfigurer();
	}

	@Override
	protected void processElement(final ProcessorContext context) {

		MethodAction action = new MethodAction() {
			@Override
			public void doExecute(ExecutableElement fieldDef) {
				Exclude exclude = fieldDef.getAnnotation(Exclude.class);
				if (exclude != null) {
					return;
				}

				TypeMirror returnType = fieldDef.getReturnType();
				MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType((DeclaredType)returnType);
				mutableType = mutableType.replaceClassSuffix(MODEL_SUFFIX, DATA_SUFFIX);
				
				context.getPrintWriter().println(getGetterSignature(mutableType, fieldDef) + ";");
				context.getPrintWriter().println(getSetterSignature(mutableType, fieldDef) + ";");
			}
		};

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());
		for (ExecutableElement fieldDef : methodsIn) {
			action.execute(fieldDef);
		}	
	}
}