package sk.seges.sesam.pap.metadata.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class MetaModelTypeElement extends DelegateMutableDeclaredType {

	public static final String META_MODEL_SUFFIX = "MetaModel";

	private final MutableDeclaredType metaModelMutableType;
	
	public MetaModelTypeElement(MutableDeclaredType metaModelMutableType) {
		this.metaModelMutableType = metaModelMutableType;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return metaModelMutableType.clone().addClassSufix(META_MODEL_SUFFIX);
	}
}