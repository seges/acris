package sk.seges.corpis.appscaffold.model.pap;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.model.pap.configurer.ModelBaseProcessorConfigurer;
import sk.seges.corpis.appscaffold.model.pap.model.BaseObjectType;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class ModelBaseProcessor extends AbstractDataProcessor {

	@Override
	protected void processElement(ProcessorContext context) {
		generateAccessors(context.getPrintWriter(), context.getTypeElement(), context.getTypeElement(), new ArrayList<String>());
	}

	private void generateMethodAccessors(FormattedPrintWriter pw, TypeElement owner, MutableTypeMirror mutableType, List<String> generatedProperties) {
		TypeElement typeElement = (TypeElement)((DeclaredType) processingEnv.getTypeUtils().fromMutableType(mutableType)).asElement();
		
		generateMethodAccessors(pw, owner, typeElement, generatedProperties);
	}
	
	private void generateMethodAccessors(FormattedPrintWriter pw, TypeElement owner, TypeElement processingElement, List<String> generatedProperties) {
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());

		ElementSorter.sort(methods);

		for (ExecutableElement method: methods) {

			if (!MethodHelper.isGetterMethod(method)) {
				continue;
			}

			if (generatedProperties.contains(MethodHelper.toField(method))) {
				continue;
			}
			
			generatedProperties.add(MethodHelper.toField(method));
			
			TypeMirror returnType = method.getReturnType();
			
			if (returnType.getKind().equals(TypeKind.TYPEVAR)) {
				TypeMirror erasuredReturnType = ProcessorUtils.erasure(owner, (TypeVariable) returnType);
				if (erasuredReturnType != null) {
					returnType = erasuredReturnType;
				}
			}
			
			MutableTypeMirror mutableReturnType = castToDomainDataInterface(returnType);

			String fieldName = MethodHelper.toField(method);
			
			pw.println("private ", mutableReturnType, " " + fieldName + ";");
			pw.println();
			
			//TODO copied from accessors printer
			pw.println("public ", mutableReturnType, " " + MethodHelper.toGetter(fieldName) + " {");
			pw.println("return " + fieldName + ";");
			pw.println("}");
			pw.println();
			pw.println("public void " + MethodHelper.toSetter(fieldName) +  "(", mutableReturnType, " " + fieldName + ") {");
			pw.println("this." + fieldName + " = " + fieldName + ";");
			pw.println("}");
			pw.println();
		}

		for (TypeMirror interfaceType: processingElement.getInterfaces()) {
			TypeElement interfaceTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
			
			generateMethodAccessors(pw, owner, interfaceTypeElement, generatedProperties);
		}

	}
	
	private void generateAccessors(FormattedPrintWriter pw, TypeElement owner, TypeElement typeElement, List<String> generatedProperties) {
		
		TypeElement processingElement = typeElement;
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());
		
		ElementSorter.sort(methods);

		for (ExecutableElement method: methods) {
			
			if (generatedProperties.contains(method.getSimpleName().toString())) {
				continue;
			}
			
			generatedProperties.add(method.getSimpleName().toString());
			
			MutableTypeMirror returnType = castToDomainDataInterface(method.getReturnType());

			pw.println("private ", returnType, " " + method.getSimpleName() + ";");
			pw.println();
			
			//TODO copied from accessors printer
			pw.println("public ", returnType, " " + MethodHelper.toGetter(method) + " {");
			pw.println("return " + method.getSimpleName() + ";");
			pw.println("}");
			pw.println();
			pw.println("public void " + MethodHelper.toSetter(method) + 
					"(", returnType, " " + method.getSimpleName() + ") {");
			pw.println("this." + method.getSimpleName() + " = " + method.getSimpleName() + ";");
			pw.println("}");
			pw.println();
		}

		DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(typeElement.asType()), processingEnv);
		
		if (domainDataInterfaceType.isHierarchy()) {
			generateMethodAccessors(pw, owner, domainDataInterfaceType.getInterfaces().iterator().next(), generatedProperties);
		}
	}
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { 
				new BaseObjectType(context.getMutableType(), processingEnv)
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ModelBaseProcessorConfigurer();
	}

}
