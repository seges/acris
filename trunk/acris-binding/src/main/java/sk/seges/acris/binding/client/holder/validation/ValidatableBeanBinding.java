/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.validation.client.InvalidConstraint;

/**
 * Interface determining whether there is possibility to validate the underlying
 * beans binding and work with it at graphical level. GUI widgets marked with it
 * would use e.g. different binding holder supporting validation. External
 * components would have access to highlighting - especially usefull for
 * on-demand validation from form dialogs.
 * 
 * @author eldzi
 */
public interface ValidatableBeanBinding<T extends Serializable> {
	/**
	 * Highlights constraints that are invalid. If there are no invalid
	 * constraints all highlights will be cleared.
	 * 
	 * @param constraints
	 *            Invalid constraints for which property widgets should be
	 *            highlighted.
	 */
	void highlightConstraints(Set<InvalidConstraint<T>> constraints);
	
	/**
	 * Clears highlighted property widgets.
	 */
	void clearHighlight();
}
