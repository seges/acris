package sk.seges.corpis.appscaffold.model.pap;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import sk.seges.corpis.appscaffold.model.pap.accessor.DomainInterfaceAccessor;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;

public abstract class AbstractDataProcessor extends MutableAnnotationProcessor {

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
