package sk.seges.corpis.appscaffold.jpamodel.pap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.persistence.Entity;
import javax.persistence.Id;

import sk.seges.corpis.appscaffold.jpamodel.pap.configurer.JpaModelProcessorConfigurer;
import sk.seges.corpis.appscaffold.jpamodel.pap.model.JpaModelType;
import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;
import sk.seges.corpis.appscaffold.shared.annotation.domain.Exclude;
import sk.seges.corpis.appscaffold.shared.annotation.domain.FieldStrategyDefinition;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
public class JpaModelProcessor extends FluentProcessor {

	private static final String DATA_SUFFIX = "Data";
	private static final String JPA_PREFIX = "Jpa";
	public static final String MODEL_SUFFIX = "Model";

	public JpaModelProcessor() {
		Rule rule = new Rule() {
			@Override
			public boolean evaluate(OutputDefinition type, TypeElement typeElement) {
				JpaModel jpaModel = typeElement.getAnnotation(JpaModel.class);
				return jpaModel.portable();
			}

			@Override
			public List<MutableDeclaredType> getTypes(MutableDeclaredType typeElement) {
				return asList(typeElement.replaceClassPrefix(JPA_PREFIX, "").replaceClassSuffix(MODEL_SUFFIX, DATA_SUFFIX));
			}
		};
		addImplementedInterface(rule);
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new JpaModelType(context.getMutableType()) };
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new JpaModelProcessorConfigurer();
	}

	@Override
	protected void printAnnotations(ProcessorContext context) {
		List<? extends AnnotationMirror> annotationMirrors = context.getTypeElement().getAnnotationMirrors();
		for (AnnotationMirror annotation : annotationMirrors) {
			if (!typeEquals(JpaModel.class, annotation)) {
				context.getPrintWriter().println(annotation);
			}
		}
		context.getPrintWriter().println("@", Entity.class);
		super.printAnnotations(context);
	}

	@Override
	protected void processElement(ProcessorContext context) {

		final JpaModel jpaModel = context.getTypeElement().getAnnotation(JpaModel.class);
		final FormattedPrintWriter pw = context.getPrintWriter();
		
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

				MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType((DeclaredType)fieldDef.getReturnType());
				mutableType = mutableType.replaceClassSuffix(MODEL_SUFFIX, DATA_SUFFIX);
				
				printField(pw, mutableType, fieldDef);
				printStandardGetter(pw, mutableType, fieldDef);
				
				Id id = fieldDef.getAnnotation(Id.class);
				if(id != null && jpaModel.portable()) {
					printObjectSetter(pw, mutableType, fieldDef);
				} else {
					printStandardSetter(pw, mutableType, fieldDef);
				}

			}
		};

		if (FieldStrategyDefinition.IMPLICIT.equals(jpaModel.strategy())) {
			doForAllMembers(context.getTypeElement(), ElementKind.METHOD, action);
		} else {
			List<ExecutableElement> methodsIn = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());
			for (ExecutableElement fieldDef : methodsIn) {
				action.execute(fieldDef);
			}
		}

		printHashCode(pw, context.getOutputType(), businessKeys);
		printEquals(pw, context.getOutputType(), businessKeys);
	}

}
