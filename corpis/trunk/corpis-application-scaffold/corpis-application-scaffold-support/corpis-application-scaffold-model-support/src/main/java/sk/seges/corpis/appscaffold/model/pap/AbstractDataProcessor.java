package sk.seges.corpis.appscaffold.model.pap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.corpis.appscaffold.model.pap.accessor.DomainInterfaceAccessor;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.printer.ConstantsPrinter;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;

public abstract class AbstractDataProcessor extends MutableAnnotationProcessor {

	@Override
	protected void processElement(ProcessorContext context) {
		new ConstantsPrinter(context.getPrintWriter(), processingEnv).copyConstants(context.getTypeElement());
	}
	
	private Set<MutableTypeMirror> toPrintableTypes(TypeElement owner, Set<? extends MutableTypeMirror> bounds) {
		Set<MutableTypeMirror> result = new HashSet<MutableTypeMirror>();
		for (MutableTypeMirror bound: bounds) {
			result.add(toPrintableType(owner, bound));
		}
		return result;
	}
	
	protected boolean isPrimitiveBoolean(MutableTypeMirror type) {
		return type.toString(ClassSerializer.CANONICAL).equals(TypeKind.BOOLEAN.toString().toLowerCase());
	}

	protected MutableTypeMirror toPrintableType(TypeElement owner, MutableTypeMirror mutableType) {
		
		if (mutableType.getKind().equals(MutableTypeKind.TYPEVAR)) {
			MutableTypeVariable mutableTypeVariable = ((MutableTypeVariable)mutableType);
			
			if (mutableTypeVariable.getVariable() != null && mutableTypeVariable.getVariable().length() > 0) {
				TypeMirror erasuredTypeVariable = ProcessorUtils.erasure(owner, mutableTypeVariable.getVariable());
				if (erasuredTypeVariable != null) {
					return processingEnv.getTypeUtils().getTypeVariable(null, processingEnv.getTypeUtils().toMutableType(erasuredTypeVariable));
				}
				return processingEnv.getTypeUtils().getTypeVariable(mutableTypeVariable.getVariable());
			}

			if (mutableTypeVariable.getUpperBounds().size() > 0 ||
				mutableTypeVariable.getLowerBounds().size() > 0) {
				mutableTypeVariable = mutableTypeVariable.clone();
			}
			
			if (mutableTypeVariable.getLowerBounds().size() > 0) {
				mutableTypeVariable.setLowerBounds(toPrintableTypes(owner, mutableTypeVariable.getLowerBounds()));
			}

			if (mutableTypeVariable.getUpperBounds().size() > 0) {
				mutableTypeVariable.setUpperBounds(toPrintableTypes(owner, mutableTypeVariable.getUpperBounds()));
			}

			return mutableTypeVariable;
		}
		
		if (mutableType.getKind().isDeclared() && ((MutableDeclaredType)mutableType).getTypeVariables().size() > 0) {
			List<? extends MutableTypeVariable> typeVariables = ((MutableDeclaredType)mutableType).getTypeVariables();
			
			List<MutableTypeVariable> printableTypeVariables = new ArrayList<MutableTypeVariable>();
					
			for (MutableTypeVariable typeVariable: typeVariables) {
				printableTypeVariables.add((MutableTypeVariable) toPrintableType(owner, typeVariable));
			}
			
			return ((MutableDeclaredType)mutableType).clone().setTypeVariables(printableTypeVariables.toArray(new MutableTypeVariable[] {}));
		}
		
		return mutableType;
	}

	protected MutableTypeMirror castToDomainDataInterface(TypeMirror type) {
		
		if (type == null) {
			return null;
		}
		
		MutableTypeMirror returnType = processingEnv.getTypeUtils().toMutableType(type);
		
		if (type.getKind().equals(TypeKind.DECLARED)) {
			Element element = ((DeclaredType)type).asElement();
			if (new DomainInterfaceAccessor(element, processingEnv).isValid()) {
				returnType = new DomainDataInterfaceType((MutableDeclaredType)returnType, processingEnv);
			}
			
			List<MutableTypeVariable> arguments = new LinkedList<MutableTypeVariable>();
			
			for (TypeMirror typeArgument: ((DeclaredType)type).getTypeArguments()) {
				switch (typeArgument.getKind()) {
				case TYPEVAR:
					//List<T extends DistributionItem>
					TypeVariable typeVariable = ((TypeVariable)typeArgument);
					arguments.add(processingEnv.getTypeUtils().getTypeVariable(typeVariable.asElement().getSimpleName().toString(), castToDomainDataInterface(typeVariable.getUpperBound()), castToDomainDataInterface(typeVariable.getLowerBound())));
					break;
				case WILDCARD:
					//List<? extends DistributionItem>
					WildcardType wildcardType = ((WildcardType)typeArgument);
					arguments.add(processingEnv.getTypeUtils().getWildcardType(castToDomainDataInterface(wildcardType.getExtendsBound()), castToDomainDataInterface(wildcardType.getSuperBound())));
					break;
				case DECLARED:
					//List<DistributionItem>
					DeclaredType declaredType = ((DeclaredType)typeArgument);
					arguments.add(processingEnv.getTypeUtils().getTypeVariable(null, castToDomainDataInterface(declaredType)));
					break;
				default:
					throw new RuntimeException("Unsupported argument kind " + typeArgument.getKind() + " !");
				}
			}
			
			((MutableDeclaredType)returnType).setTypeVariables(arguments.toArray(new MutableTypeVariable[] {}));
		}

		return returnType;
	}
}