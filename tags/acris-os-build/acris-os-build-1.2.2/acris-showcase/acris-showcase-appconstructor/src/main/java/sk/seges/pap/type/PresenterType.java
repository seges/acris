package sk.seges.pap.type;

import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.ScaffoldNameUtil;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class PresenterType extends DelegateMutableDeclaredType {
	private final MutableDeclaredType modelType;

	public PresenterType(MutableDeclaredType modelType) {
		super();
		this.modelType = modelType;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType clone = modelType.clone();
		ScaffoldNameUtil.prefixIfEnclosed(clone);
		clone.addPackageSufix("." + ScaffoldConstant.PRES_PKG);
		return clone.addClassSufix(ScaffoldConstant.PRES_SUFFIX);
	}

}