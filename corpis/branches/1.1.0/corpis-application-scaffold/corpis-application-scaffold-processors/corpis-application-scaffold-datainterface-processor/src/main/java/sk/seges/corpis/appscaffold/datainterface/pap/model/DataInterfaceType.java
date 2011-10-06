package sk.seges.corpis.appscaffold.datainterface.pap.model;

import java.util.HashSet;
import java.util.Set;

import sk.seges.corpis.appscaffold.datainterface.pap.DataInterfaceProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.domain.IMutableDomainObject;

public class DataInterfaceType extends DelegateMutableDeclaredType {

	private MutableDeclaredType mutableType;
	
	public DataInterfaceType(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv) {
		this.mutableType = mutableType;

		Set<MutableTypeMirror> interfaces = new HashSet<MutableTypeMirror>();
		interfaces.add(processingEnv.getTypeUtils().toMutableType(IMutableDomainObject.class));
		setInterfaces(interfaces);
		
		setKind(MutableTypeKind.INTERFACE);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		if (mutableType.getSimpleName().endsWith(DataInterfaceProcessor.MODEL_SUFFIX)) {
			return mutableType.clone().setSimpleName(mutableType.getSimpleName().substring(0,
						mutableType.getSimpleName().length() - DataInterfaceProcessor.MODEL_SUFFIX.length()) + DataInterfaceProcessor.DATA_SUFFIX);
		}
		
		return mutableType.clone();
	}
}