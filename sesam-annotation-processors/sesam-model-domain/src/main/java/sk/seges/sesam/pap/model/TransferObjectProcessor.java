package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

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
					toGetter(context.getFieldName()) + " {");
			pw.println("return " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();

			pw.println((context.getModifier() != null ? (context.getModifier().toString() + " ") : "") + "void " + toSetter(context.getFieldName()) + 
					"(" + fieldTypeName + " " + context.getFieldName() + ") {");
			pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();
		}
	}
	
	@Override
	protected void writeClassAnnotations(PrintWriter pw, Element el) {
		pw.println("@SuppressWarnings(\"serial\")");
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		NamedType dtoSuperclass = getDtoSuperclass(typeElement);
		if (dtoSuperclass != null) {
			return new Type[] {dtoSuperclass};
		}
		return super.getImports(typeElement);
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				NamedType dtoSuperclass = getDtoSuperclass(typeElement);
				if (dtoSuperclass != null) {
					
					TypeElement domainObjectClass = getDomainTypeElement(typeElement);

					if (domainObjectClass != null) {
						TypeMirror superClassType = domainObjectClass.getSuperclass();
						return new Type[] { genericsSupport.applyGenerics(dtoSuperclass, (DeclaredType)superClassType) };
					}
					
					return new Type[] {dtoSuperclass};
				}
				break;
			case OUTPUT_INTERFACES:
				dtoSuperclass = getDtoSuperclass(typeElement);
				if (dtoSuperclass == null) {
					return new Type[] { Serializable.class };
				}
				break;
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
				getOutputClass((MutableType)genericsSupport.applyVariableGenerics(mutableType, getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))))
		};
	}

	protected ElementPrinter[] getElementPrinters(PrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw),
				new AccessorsPrinter(pw)
		};
	}

	public static MutableType getOutputClass(MutableType mutableType) {	
		return getDtoType(mutableType);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return false;
	}
}