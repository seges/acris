/**
 * 
 */
package sk.seges.pap.type;

import sk.seges.pap.ScaffoldConstant;
import sk.seges.pap.ScaffoldNameUtil;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

/**
 * @author ladislav.gazo
 *
 */
public class ActivatorPartType extends DelegateMutableDeclaredType {
	private final MutableDeclaredType modelType;

	public ActivatorPartType(MutableDeclaredType modelType) {
		super();
		this.modelType = modelType;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType clone = modelType.clone();
		ScaffoldNameUtil.prefixIfEnclosed(clone);
		return clone.addClassSufix(ScaffoldConstant.ACTIVATOR_PART_SUFFIX);
	}

}
