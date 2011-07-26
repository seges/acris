package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.AbstractElementPrinter;
import sk.seges.sesam.pap.model.model.ElementPrinter;
import sk.seges.sesam.pap.model.model.ProcessorContext;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
		
	class FieldPrinter extends AbstractElementPrinter {

		public FieldPrinter(PrintWriter pw) {
			super(pw);
		}
		
		public void print(ProcessorContext context) {
			//we do not use modifier from the param - fields should be always private
			pw.println(Modifier.PRIVATE.toString() + " " + context.getFieldType().toString(null, ClassSerializer.CANONICAL, true) + " " + 
					context.getFieldName() + ";");
			pw.println();
		}
	}
	
	class AccessorsPrinter extends AbstractElementPrinter {

		public AccessorsPrinter(PrintWriter pw) {
			super(pw);
		}
		
		@Override
		public void print(ProcessorContext context) {

			String fieldTypeName = context.getFieldType().toString(null, ClassSerializer.CANONICAL, true);
			
			pw.println((context.getModifier() != null ? (context.getModifier().toString() + " ") : "") + fieldTypeName + " " + 
					toHelper.toGetter(context.getFieldName()) + " {");
			pw.println("return " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();

			pw.println((context.getModifier() != null ? (context.getModifier().toString() + " ") : "") + "void " + toHelper.toSetter(context.getFieldName()) + 
					"(" + fieldTypeName + " " + context.getFieldName() + ") {");
			pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();
		}
	}
	
	@Override
	protected void writeClassAnnotations(PrintWriter pw, Element configurationElement) {
		pw.println("@SuppressWarnings(\"serial\")");
		
		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(configurationElement);
		
		ImmutableType configurationType = (ImmutableType)getNameTypes().toType(configurationElement);
		
		pw.print("@" + TransferObjectMapping.class.getSimpleName() + "(");

		pw.println("dtoClass = " + getOutputClass(configurationType).getSimpleName() + ".class,");
		pw.println("		domainClass = " + toHelper.getDomainTypeElement(configurationElement).getSimpleName().toString() + ".class, ");
		pw.println("		configuration = " + configurationElement.getSimpleName().toString() + ".class, ");
		pw.print("		converter = ");
		if (transferObjectConfiguration.getConverter() != null) {
			pw.print(transferObjectConfiguration.getConverter().toString());
		} else {
			ImmutableType generatedConverter = TransferObjectConvertorProcessor.getOutputClass(configurationType, new DefaultPackageValidatorProvider());
			pw.print(generatedConverter.getCanonicalName());
		}
		pw.print(".class");
		pw.println(")");
		
		super.writeClassAnnotations(pw, configurationElement);
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		NamedType dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
		List<Type> result = new ArrayList<Type>();
		ListUtils.add(result, super.getImports(typeElement));
		if (dtoSuperclass != null) {
			ListUtils.add(result, new Type[] {dtoSuperclass});
		}
		ListUtils.add(result, new Type[] {TransferObjectMapping.class});
		ListUtils.add(result, new Type[] {getNameTypes().toType(toHelper.getDomainTypeElement(typeElement))});
		return result.toArray(new Type[] {});
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(element);
		
		TypeElement dto = transferObjectConfiguration.getDto();
		if (dto != null) {
			return supportProcessorChain();
		}
		
		return super.processElement(element, roundEnv);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				NamedType dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
				if (dtoSuperclass != null) {
					
					TypeElement domainObjectClass = toHelper.getDomainTypeElement(typeElement);
	
					if (domainObjectClass != null) {
						TypeMirror superClassType = domainObjectClass.getSuperclass();
						return new Type[] { genericsSupport.applyGenerics(dtoSuperclass, (DeclaredType)superClassType) };
					}
					
					return new Type[] {dtoSuperclass};
				}
				break;
			case OUTPUT_INTERFACES:
				dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
				if (dtoSuperclass == null) {
					return new Type[] { Serializable.class };
				}
				break;
		}
		
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
				getOutputClass((ImmutableType)genericsSupport.applyVariableGenerics(mutableType, toHelper.getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))))
		};
	}

	protected ElementPrinter[] getElementPrinters(PrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw),
				new AccessorsPrinter(pw)
		};
	}

	public static ImmutableType getOutputClass(ImmutableType mutableType) {	
		return TransferObjectHelper.getDtoType(mutableType);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return false;
	}
}