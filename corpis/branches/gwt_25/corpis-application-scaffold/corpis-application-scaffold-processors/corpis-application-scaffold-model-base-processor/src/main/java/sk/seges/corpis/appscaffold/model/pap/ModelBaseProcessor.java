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

import sk.seges.corpis.appscaffold.model.pap.accessor.ReadOnlyAccessor;
import sk.seges.corpis.appscaffold.model.pap.configurer.ModelBaseProcessorConfigurer;
import sk.seges.corpis.appscaffold.model.pap.model.AbstractDataType;
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
		super.processElement(context);
		generateAccessors(context.getMutableType(), context.getPrintWriter(), context.getTypeElement(), context.getTypeElement(), new ArrayList<String>());
	}

	private void generateMethodAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, TypeElement owner, MutableTypeMirror mutableType, List<String> generatedProperties) {
		TypeElement typeElement = (TypeElement)((DeclaredType) processingEnv.getTypeUtils().fromMutableType(mutableType)).asElement();
		
		generateMethodAccessors(resultType, pw, owner, typeElement, generatedProperties);
	}

	private void printAccessorForField(FormattedPrintWriter pw, TypeElement owner, MutableTypeMirror mutableReturnType, ExecutableElement method) {
		
		String fieldName = MethodHelper.toField(method);
		
		boolean readOnlyProperty = new ReadOnlyAccessor(method, processingEnv).isReadonly();

		if (!readOnlyProperty) {
			pw.println("private ", toPrintableType(owner, mutableReturnType), " " + fieldName + ";");
			pw.println();
		}
		
		pw.print("public ");
		
		if (readOnlyProperty) {
			pw.print("abstract ");
		}
		
		pw.print(toPrintableType(owner, mutableReturnType), " ");
		if (isPrimitiveBoolean(mutableReturnType)) {
			pw.print(MethodHelper.toIsGetter(fieldName));
		} else {
			pw.print(MethodHelper.toGetter(fieldName));
		}
		
		if (!readOnlyProperty) {
			pw.println(" {");
			pw.println("return " + fieldName + ";");
			pw.println("}");
		} else {
			pw.println(";");
		}
		pw.println();
		
		if (!readOnlyProperty) {
			pw.println("public void " + MethodHelper.toSetter(fieldName) +  "(", toPrintableType(owner, mutableReturnType), " " + fieldName + ") {");
			pw.println("this." + fieldName + " = " + fieldName + ";");
			pw.println("}");
			pw.println();
		}
	}
	
	private void generateMethodAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, TypeElement owner, TypeElement processingElement, List<String> generatedProperties) {
		
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

			printAccessorForField(pw, owner, mutableReturnType, method);
		}

		for (TypeMirror interfaceType: processingElement.getInterfaces()) {
			TypeElement interfaceTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
			
			generateMethodAccessors(resultType, pw, owner, interfaceTypeElement, generatedProperties);
		}

	}
		
	private void generateAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, TypeElement owner, TypeElement typeElement, List<String> generatedProperties) {
		
		TypeElement processingElement = typeElement;
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());
		
		ElementSorter.sort(methods);

		for (ExecutableElement method: methods) {
			
			if (generatedProperties.contains(method.getSimpleName().toString())) {
				continue;
			}
			
			generatedProperties.add(method.getSimpleName().toString());
			
			MutableTypeMirror returnType = castToDomainDataInterface(method.getReturnType());

			printAccessorForField(pw, owner, returnType, method);
		}

		DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(typeElement.asType()), processingEnv);
		
		for (MutableTypeMirror interfaceType: domainDataInterfaceType.getInterfaces()) {
			if (!(interfaceType instanceof AbstractDataType)) {
				//it is not a data interface so have to implement methods from this interface
				//TODO check if it not generated yet!
				generateMethodAccessors(resultType, pw, owner, interfaceType, generatedProperties);
			}
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