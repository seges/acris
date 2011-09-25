package sk.seges.corpis.appscaffold.datainterface.pap;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;
import sk.seges.corpis.appscaffold.shared.annotation.domain.Exclude;
import sk.seges.corpis.appscaffold.shared.annotation.domain.FieldStrategyDefinition;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
public class DataInterfaceProcessor extends FluentProcessor {

	private static final String DATA_SUFFIX = "Data";
	private static final String MODEL_SUFFIX = "Model";

	public DataInterfaceProcessor() {
		addImplementedInterface(TypedClassBuilder.get(IMutableDomainObject.class, Object.class));
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		ImmutableType type = mutableType;
		if (mutableType.getSimpleName().endsWith(MODEL_SUFFIX)) {
			type = mutableType.setName(mutableType.getSimpleName().substring(0,
					mutableType.getSimpleName().length() - MODEL_SUFFIX.length()) + DATA_SUFFIX);
		}
		return new NamedType[] { type };
	}

	@Override
	protected ElementKind getElementKind() {
		  return ElementKind.INTERFACE;
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DataInterfaceProcessorConfigurer();
	}

	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv,
			final FormattedPrintWriter pw) {

		MethodAction action = new MethodAction() {
			@Override
			public void doExecute(ExecutableElement fieldDef) {
				List<? extends AnnotationMirror> annotationMirrors = fieldDef.getAnnotationMirrors();

				Exclude exclude = fieldDef.getAnnotation(Exclude.class);
				if (exclude != null) {
					return;
				}

				pw.println(getGetterSignature(fieldDef.getReturnType(), fieldDef) + ";");
				pw.println(getSetterSignature(fieldDef.getReturnType(), fieldDef) + ";");
			}
		};

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		for (ExecutableElement fieldDef : methodsIn) {
			action.execute(fieldDef);
		}
		
	}

}
