package sk.seges.sesam.core.pap.printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

public class TypePrinter {

	private final MutableProcessingEnvironment processingEnv;
	private final HierarchyPrintWriter hierarchyPrintWriter;
	
	public TypePrinter(HierarchyPrintWriter hierarchyPrintWriter, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.hierarchyPrintWriter = hierarchyPrintWriter;
	}

	public TypePrinter print(final MutableDeclaredType type) {
		
		if (hierarchyPrintWriter.getOudentLevel() != 0) {
			//Type is nested so print an empty line
			hierarchyPrintWriter.println();
		}
		
		HierarchyPrintWriter typePrintWriter = type.getPrintWriter();
		hierarchyPrintWriter.addNestedPrinter(typePrintWriter);

		typePrintWriter.addNestedPrinter(new FormattedPrintWriter(processingEnv) {
			@Override
			public void flush() {
				printAnnotations(this, type);
				super.flush();
			}
		});
		typePrintWriter.addNestedPrinter(new FormattedPrintWriter(processingEnv) {
			@Override
			public void flush() {
				printTypeDefinition(this, type);
				super.flush();
			}
		});
		typePrintWriter.println(" {");
		
		HierarchyPrintWriter bodyPrinter = new HierarchyPrintWriter(processingEnv);
		typePrintWriter.addNestedPrinter(bodyPrinter);
		
		bodyPrinter.addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				printNestedTypes(this, type);
				super.flush();
			}
		});
		
		bodyPrinter.addNestedPrinter(new FormattedPrintWriter(processingEnv) {
			@Override
			public void flush() {
				printFields(this, type);
				super.flush();
			}
		});
		bodyPrinter.addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				printMethods(this, type);
				super.flush();
			}
		});

		typePrintWriter.println("}");

		typePrintWriter.setCurrentPrinter(bodyPrinter);
		
		return this;
	}

	private void printMethods(HierarchyPrintWriter printWriter, MutableDeclaredType type) {

		if (!type.getConstructor().isDefault()) {
			type.getConstructor().setReturnType(null);
			printWriter.addNestedPrinter(type.getConstructor().getPrintWriter());
			printWriter.println();
		}
		
		for (MutableExecutableType method: type.getMethods()) {
			printWriter.addNestedPrinter(method.getPrintWriter());
			printWriter.println();
		}
	}
	
	private void printAnnotations(FormattedPrintWriter pw, MutableDeclaredType type) {
		Set<MutableAnnotationMirror> annotations = type.getMutableAnnotations();
		
		for (MutableAnnotationMirror annotation: annotations) {
			AnnotationPrinter annotationPrinter = new AnnotationPrinter(pw, processingEnv);
			annotationPrinter.print(annotation);
		}
	}

	private void printNestedTypes(HierarchyPrintWriter pw, MutableDeclaredType type) {
		List<MutableDeclaredType> nestedTypes = type.getNestedTypes();
		
		for (MutableDeclaredType nestedType: nestedTypes) {
			new TypePrinter(pw, processingEnv).print(nestedType);
		}
	}
	
	private void printFields(FormattedPrintWriter pw, MutableDeclaredType type) {
		
		List<MutableVariableElement> fields = type.getFields();

		if (fields != null) {

			for (MutableVariableElement field: fields) {
				pw.println();
				
				Set<MutableAnnotationMirror> mutableAnnotations = field.getMutableAnnotations();
				
				for (MutableAnnotationMirror mutableAnnotation: mutableAnnotations) {
					new AnnotationPrinter(pw, processingEnv).print(mutableAnnotation);
				}
				
				Set<Modifier> modifiers = field.getModifiers();
				
				if (modifiers != null) {
					for (Modifier modifier: modifiers) {
						pw.print(modifier.toString() + " ");
					}
				}
				
				pw.println(field.asType(), " " + field.getSimpleName() + ";");
			}
			
			if (fields.size() > 0) {
				pw.println();
			}
		}
	}

	private void printTypeDefinition(FormattedPrintWriter pw, MutableDeclaredType type) {
		
		for (Modifier modifier: type.getModifiers()) {
			pw.print(modifier.name().toLowerCase() + " ");
		}
		
		pw.print(type.getKind().toString() + " " + type.toString(ClassSerializer.SIMPLE, false));
		
		MutableDeclaredType superClassType = type.getSuperClass();
		
		if (type.getTypeVariables().size() > 0) {
			pw.print("<");

			int i = 0;

			for (MutableTypeVariable typeParameter : type.getTypeVariables()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(typeParameter);
				i++;
			}

			pw.print(">");
		}
		
		if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && !type.getKind().equals(MutableTypeKind.INTERFACE)) {
			pw.print(" extends ", superClassType);
		}

		if (type.getInterfaces() != null && type.getInterfaces().size() > 0) {

			boolean supportedType = false;
			
			if (type.getKind().equals(MutableTypeKind.CLASS)) {
				pw.print(" implements ");
				supportedType = true;
			} else 	if (type.getKind().equals(MutableTypeKind.INTERFACE)) {
				pw.print(" extends ");
				supportedType = true;
			}

			if (supportedType) {
				int i = 0;

				if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && 
						type.getKind().equals(MutableTypeKind.INTERFACE)) {
					pw.print(toPrintableType(superClassType));
					i++;
				}
				
				for (MutableTypeMirror interfaceType : type.getInterfaces()) {
					if (i > 0) {
						pw.print(", ");
					}
					pw.print(toPrintableType(interfaceType));
					i++;
				}
			}
		} else if (superClassType != null && !superClassType.toString(ClassSerializer.CANONICAL).equals(Object.class.getCanonicalName()) && 
						type.getKind().equals(MutableTypeKind.INTERFACE)) {
			pw.print(" extends ");
			pw.print(superClassType);
		}
	}
	
	protected MutableTypeMirror toPrintableType(MutableTypeMirror mutableType) {
		if (mutableType.getKind().isDeclared()) {
			MutableDeclaredType declaredType = (MutableDeclaredType)mutableType;
			List<? extends MutableTypeVariable> typeVariables = declaredType.getTypeVariables();
			List<MutableTypeVariable> strippedTypeVariables = new ArrayList<MutableTypeVariable>();
			for (MutableTypeVariable typeVariable: typeVariables) {
				if (typeVariable.getVariable() != null) {
					strippedTypeVariables.add(processingEnv.getTypeUtils().getTypeVariable(typeVariable.getVariable()));
				} else {
					strippedTypeVariables.add(typeVariable);
				}
			}
			return declaredType.clone().setTypeVariables(strippedTypeVariables.toArray(new MutableTypeVariable[] {}));
		}
		
		return mutableType;
	}
}