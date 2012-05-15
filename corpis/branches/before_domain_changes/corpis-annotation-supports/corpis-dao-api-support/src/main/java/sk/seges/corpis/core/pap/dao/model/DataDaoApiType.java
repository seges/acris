package sk.seges.corpis.core.pap.dao.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

public class DataDaoApiType extends AbstractDaoApiType  {

	public DataDaoApiType(MutableDeclaredType mutableDeclaredType, MutableProcessingEnvironment processingEnv) {
		super(mutableDeclaredType, processingEnv);
	}

	protected MutableDeclaredType getDataType(MutableDeclaredType inputType) {
		if (inputType.getTypeVariables().size() > 0) {
			MutableTypeVariable[] typeVariables = new MutableTypeVariable[inputType.getTypeVariables().size()];
			for (int i = 0; i < inputType.getTypeVariables().size(); i++) {
				typeVariables[i] = processingEnv.getTypeUtils().getTypeVariable(MutableWildcardType.WILDCARD_NAME);
			}
			inputType = inputType.clone().setTypeVariables(typeVariables);
		}
		return new DomainDataInterfaceType(inputType, processingEnv);
	}

	@Override
	protected List<MutableDeclaredType> getTypeInterfaces() {
		MutableTypeVariable[] typeVariable = new MutableTypeVariable[] { processingEnv.getTypeUtils().getTypeVariable("T") };
		List<MutableDeclaredType> interfaces = new ArrayList<MutableDeclaredType>();
		interfaces.add(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(ICrudDAO.class), typeVariable));
		interfaces.add(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(IEntityInstancer.class), typeVariable));
		ElementSorter.sortMutableTypes(interfaces);
		return interfaces;
	}
}
