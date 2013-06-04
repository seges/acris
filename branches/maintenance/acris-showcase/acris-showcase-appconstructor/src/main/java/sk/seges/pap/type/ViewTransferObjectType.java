package sk.seges.pap.type;

import sk.seges.pap.ScaffoldConstant;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class ViewTransferObjectType extends DelegateMutableDeclaredType {
	private final MutableDeclaredType modelType;

	public ViewTransferObjectType(MutableDeclaredType modelType) {
		super();
		this.modelType = modelType;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType clone = modelType.clone();
		int lastIndexOf = clone.getPackageName().lastIndexOf(".");
		clone.changePackage(clone.getPackageName().substring(0, lastIndexOf) + "."
				+ ScaffoldConstant.DTO_PKG);
		clone.replaceClassSuffix(ScaffoldConstant.MODEL_SUFFIX,
				ScaffoldConstant.DTO_SUFFIX);
		return clone;
	}

}