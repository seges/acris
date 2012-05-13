package sk.seges.corpis.core.pap.dao.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.dao.ICrudDAO;

public class DaoApiType extends AbstractDaoApiType {

	public DaoApiType(MutableDeclaredType dataInterfaceType, MutableProcessingEnvironment processingEnv) {
		super(dataInterfaceType, processingEnv);
	}

	public DaoApiType(TypeElement typeElement, MutableProcessingEnvironment processingEnv) {
		super(typeElement, processingEnv);
	}
	
	@Override
	protected MutableDeclaredType getDataType(MutableDeclaredType inputType) {
		return inputType;
	}

	@Override
	protected List<MutableDeclaredType> getTypeInterfaces() {
		List<MutableDeclaredType> interfaces = new ArrayList<MutableDeclaredType>();
		interfaces.add(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(ICrudDAO.class), 
				new MutableDeclaredType[] { mutableDeclaredType }));
		return interfaces;
	}
}