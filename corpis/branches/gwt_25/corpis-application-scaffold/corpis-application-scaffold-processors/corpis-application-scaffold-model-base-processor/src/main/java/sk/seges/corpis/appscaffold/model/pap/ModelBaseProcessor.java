package sk.seges.corpis.appscaffold.model.pap;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.model.pap.configurer.ModelBaseProcessorConfigurer;
import sk.seges.corpis.appscaffold.model.pap.model.AbstractDataType;
import sk.seges.corpis.appscaffold.model.pap.model.BaseObjectType;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.accessor.ReadOnlyAccessor;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class ModelBaseProcessor extends AbstractDataProcessor {

	@Override
	protected void processElement(ProcessorContext context) {
		super.processElement(context);
		generateAccessors(context.getMutableType(), context.getPrintWriter(), context.getOutputType(), context.getTypeElement(), context.getTypeElement(), new ArrayList<String>());
	}

	private void generateMethodAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, MutableDeclaredType outputType, TypeElement owner, MutableTypeMirror mutableType, List<String> generatedProperties) {
		TypeElement typeElement = (TypeElement)((DeclaredType) processingEnv.getTypeUtils().fromMutableType(mutableType)).asElement();
		
		generateMethodAccessors(resultType, pw, outputType, owner, typeElement, mutableType instanceof AbstractDataType, generatedProperties);
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
	
	protected EntityResolver getEntityResolver() {
		return new HibernateEntityResolver(processingEnv);
	}

	private void generateMethodAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, MutableDeclaredType outputType, TypeElement owner, TypeElement processingElement, 
			boolean isDataInterface, List<String> generatedProperties) {
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());

		ElementSorter.sort(methods);

		boolean isAbstract = false;
		
		for (ExecutableElement method: methods) {

			if (!MethodHelper.isGetterMethod(method)) {
				//method is not getter method 
				if(!isDataInterface && !MethodHelper.isSetterMethod(method)) {
					//nor setter and is defined in standard interface (not in scaffold one) and there
					if (!ProcessorUtils.hasMethod(processingEnv, Object.class, method)) {
						//and is not defined in Object itself, like hashCode and equals
						isAbstract = true;
					}
				}
				
				continue;
			} else if (!isDataInterface && !ProcessorUtils.hasMethod(MethodHelper.toSetter(method), owner, true)) {
				//method is getter, is not defined in standard interface (not in scaffold one) and no setter is available
				//so that means that getter has custom logic and is not accessor for field
				isAbstract = true;
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

		if (isAbstract) {
			outputType.addModifier(Modifier.ABSTRACT);
		}
		
		for (TypeMirror interfaceType: processingElement.getInterfaces()) {
			TypeElement interfaceTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
			
			generateMethodAccessors(resultType, pw, outputType, owner, interfaceTypeElement, isDataInterface, generatedProperties);
		}

	}
		
	private void generateAccessors(MutableDeclaredType resultType, FormattedPrintWriter pw, MutableDeclaredType outputType, TypeElement owner, TypeElement typeElement, List<String> generatedProperties) {
		
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
				generateMethodAccessors(resultType, pw, outputType, owner, interfaceType, generatedProperties);
			}
		}
		
		if (outputType.getSuperClass() != null && outputType.getSuperClass().getModifiers() != null && outputType.getSuperClass().getModifiers().contains(Modifier.ABSTRACT)) {
			//if superclass is abstract output class should also be abstract
			outputType.addModifier(Modifier.ABSTRACT);
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