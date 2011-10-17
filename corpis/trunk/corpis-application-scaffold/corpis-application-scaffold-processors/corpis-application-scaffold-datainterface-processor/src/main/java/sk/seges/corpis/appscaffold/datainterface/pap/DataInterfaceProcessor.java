package sk.seges.corpis.appscaffold.datainterface.pap;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.appscaffold.shared.annotation.domain.Exclude;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
public class DataInterfaceProcessor extends FluentProcessor {

	public static final String DATA_SUFFIX = "Data";
	public static final String MODEL_SUFFIX = "Model";

	public DataInterfaceProcessor() {
		reactsOn(DomainInterface.class);

		Rule rule = new AlwaysRule() {
			@Override
			public List<MutableDeclaredType> getTypes(MutableDeclaredType typeElement) {
				MutableDeclaredType interfaceType = processingEnv.getTypeUtils().toMutableType(
						IMutableDomainObject.class);
				interfaceType.addTypeVariable(processingEnv.getTypeUtils().getTypeVariable("T"));
				return asList(interfaceType);
			}
		};
		addImplementedInterface(rule);
		setResultKind(MutableTypeKind.INTERFACE);
	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return inputType.removeClassSuffix(MODEL_SUFFIX).addClassSufix(DataInterfaceProcessor.DATA_SUFFIX)
				.addTypeVariable(processingEnv.getTypeUtils().getTypeVariable("T"));
	}

	@Override
	protected void doProcessElement(final ProcessorContext context) {

		MethodAction action = new MethodAction() {
			@Override
			public void doExecute(ExecutableElement fieldDef) {
				Exclude exclude = fieldDef.getAnnotation(Exclude.class);
				if (exclude != null) {
					return;
				}

				TypeMirror returnType = fieldDef.getReturnType();
				// processingEnv.getTypeUtils().toMutableType(javaType)
				if (returnType instanceof DeclaredType) {
					MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType(
							(DeclaredType) returnType);
					mutableType = mutableType.replaceClassSuffix(MODEL_SUFFIX, DATA_SUFFIX);

					context.getPrintWriter().println(getGetterSignature(mutableType, fieldDef) + ";");
					context.getPrintWriter().println(getSetterSignature(mutableType, fieldDef) + ";");
				} else {
					context.getPrintWriter().println(getGetterSignature(returnType, fieldDef) + ";");
					context.getPrintWriter().println(getSetterSignature(returnType, fieldDef) + ";");
				}
			}
		};

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());
		for (ExecutableElement fieldDef : methodsIn) {
			action.execute(fieldDef);
		}
	}

}