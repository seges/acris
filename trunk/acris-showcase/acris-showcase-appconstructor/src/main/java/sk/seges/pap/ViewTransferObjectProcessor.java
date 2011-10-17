/**
 * 
 */
package sk.seges.pap;

import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

/**
 * @author ladislav.gazo
 */
public class ViewTransferObjectProcessor extends FluentProcessor {
	public ViewTransferObjectProcessor() {
		reactsOn(Singleselect.class);
	}
	
	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
		
	}

}
