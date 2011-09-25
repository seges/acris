package sk.seges.corpis.appscaffold.jpamodel.pap;

import java.lang.reflect.Type;
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
import sk.seges.sesam.core.pap.FluentProcessor.MethodAction;
import sk.seges.sesam.core.pap.FluentProcessor.Rule;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
public class JpaModelProcessor extends FluentProcessor {

	private static final String DATA_SUFFIX = "Data";
	private static final String JPA_PREFIX = "Jpa";
	private static final String MODEL_SUFFIX = "Model";

	public JpaModelProcessor() {
		Rule rule = new Rule() {
			@Override
			public boolean evaluate(OutputDefinition type, TypeElement typeElement) {
				JpaModel jpaModel = typeElement.getAnnotation(JpaModel.class);
				return jpaModel.portable();
			}
			
			@Override
			public List<Type> getTypes(ImmutableType typeElement) {
				String simpleName = typeElement.getSimpleName();
				
				if(simpleName.startsWith(JPA_PREFIX)) {
					simpleName = simpleName.substring(JPA_PREFIX.length());
				}
				if(simpleName.endsWith(MODEL_SUFFIX)) {
					simpleName = simpleName.substring(0, simpleName.length() - MODEL_SUFFIX.length());
				}
				return asList(typeElement.setName(simpleName + DATA_SUFFIX));
			}
		};
		addImplementedInterface(rule);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		ImmutableType type = mutableType;
		if (mutableType.getSimpleName().endsWith(MODEL_SUFFIX)) {
			type = mutableType.setName(mutableType.getSimpleName().substring(0,
					mutableType.getSimpleName().length() - MODEL_SUFFIX.length()));
		}
		return new NamedType[] { type };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new JpaModelProcessorConfigurer();
	}

	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv,
			final FormattedPrintWriter pw) {

		JpaModel jpaModel = typeElement.getAnnotation(JpaModel.class);

		final List<ExecutableElement> businessKeys = new ArrayList<ExecutableElement>();

		MethodAction action = new MethodAction() {
			@Override
			public void doExecute(ExecutableElement fieldDef) {
				List<? extends AnnotationMirror> annotationMirrors = fieldDef.getAnnotationMirrors();

				Exclude exclude = fieldDef.getAnnotation(Exclude.class);
				if (exclude != null) {
					return;
				}

				for (AnnotationMirror annotation : annotationMirrors) {
					if (typeEquals(BusinessKey.class, annotation)) {
						businessKeys.add(fieldDef);
					} else {
						pw.println(annotation);
					}
				}

				printField(pw, fieldDef.getReturnType(), fieldDef);
				printStandardGetter(pw, fieldDef.getReturnType(), fieldDef);
				printStandardSetter(pw, fieldDef.getReturnType(), fieldDef);

			}
		};

		if (FieldStrategyDefinition.IMPLICIT.equals(jpaModel.strategy())) {
			doForAllMembers(typeElement, ElementKind.METHOD, action);
		} else {
			List<ExecutableElement> methodsIn = ElementFilter.methodsIn(typeElement.getEnclosedElements());
			for (ExecutableElement fieldDef : methodsIn) {
				action.execute(fieldDef);
			}
		}

		printHashCode(pw, outputName, businessKeys);
		printEquals(pw, outputName, businessKeys);
	}

}
