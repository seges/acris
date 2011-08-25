package sk.seges.sesam.core.pap.utils;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class CastHelper {

	private ProcessingEnvironment processingEnv;
	private NameTypesUtils nameTypesUtils;
	
	public CastHelper(ProcessingEnvironment processingEnv, NameTypesUtils nameTypesUtils) {
		this.processingEnv = processingEnv;
		this.nameTypesUtils = nameTypesUtils;
	}
	
	public boolean printCastForCollections(TypeMirror originalType, NamedType targetType, PrintWriter pw) {
		if (ProcessorUtils.isCollection(originalType, processingEnv)) {
			
			DeclaredType declaredList = ((DeclaredType)originalType);

			//TODO use getTargetEntity for the return type
			if (declaredList.getTypeArguments() != null && declaredList.getTypeArguments().size() == 1) {

				if (targetType instanceof HasTypeParameters) {
					TypeParameter typeParameter = ((HasTypeParameters)targetType).getTypeParameters()[0];
					HasTypeParameters modifiedList = TypedClassBuilder.get(nameTypesUtils.toType(processingEnv.getTypeUtils().erasure(declaredList)), typeParameter);
					pw.print("(" + modifiedList.toString(null, ClassSerializer.CANONICAL, true) + ")");
				};
				
			} else {
				return false;
			}
		}
		//TODO handle maps
		
		return true;
	}

	public void printCastForType(TypeMirror originalType, TypeMirror targetType, TypeElement castType, PrintWriter pw) {
		if (!processingEnv.getTypeUtils().isSameType(processingEnv.getTypeUtils().erasure(originalType), 
													 processingEnv.getTypeUtils().erasure(targetType))) {
			pw.print("(" + castType.toString() +")");
		}
	}	
}