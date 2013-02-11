package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.type.DeclaredType;

import sk.seges.corpis.appscaffold.model.pap.accessor.DomainInterfaceAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class AbstractDataType extends DelegateMutableDeclaredType {

	protected MutableDeclaredType domainDataType;
	protected MutableProcessingEnvironment processingEnv;
	
	protected AbstractDataType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		this.domainDataType = dataDefinition;
		this.processingEnv = processingEnv;
	}
	
	protected Set<? extends MutableTypeMirror> getDataTypes(Set<? extends MutableTypeMirror> types) {

		Set<MutableTypeMirror> result = new HashSet<MutableTypeMirror>();
		
		for (MutableTypeMirror type: types){
			
			MutableDeclaredType d = (MutableDeclaredType) type;
			
			if (d.asType() != null) {
				if (new DomainInterfaceAccessor(((DeclaredType)d.asType()).asElement(), processingEnv).isValid()) {
					DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType((MutableDeclaredType)d, processingEnv);
					result.add(domainDataInterfaceType);
				} else {
					result.add(d);
				}
			} else {
				result.add(type);
			}
		}
		
		return result;
	}

	protected void setDataTypeVariables() {
		
		List<? extends MutableTypeVariable> typeVariables = getTypeVariables();
		List<MutableTypeVariable> dataTypeVariables = new ArrayList<MutableTypeVariable>();

		for (MutableTypeVariable typeVariable: typeVariables) {
			dataTypeVariables.add(processingEnv.getTypeUtils().getTypeVariable(typeVariable.getVariable(), 
					getDataTypes(typeVariable.getUpperBounds()).toArray(new MutableTypeMirror[] {}), 
					getDataTypes(typeVariable.getLowerBounds()).toArray(new MutableTypeMirror[] {})));
		}
		
		setTypeVariables(dataTypeVariables.toArray(new MutableTypeVariable[] {}));
	}
}