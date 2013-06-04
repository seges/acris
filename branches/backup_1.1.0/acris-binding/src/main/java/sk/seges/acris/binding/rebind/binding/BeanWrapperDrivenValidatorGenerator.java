/**
 * 
 */
package sk.seges.acris.binding.rebind.binding;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;

import com.google.gwt.validation.rebind.InterfaceTypeNameStrategy;
import com.google.gwt.validation.rebind.ValidatorGenerator;

/**
 * @author eldzi
 */
public class BeanWrapperDrivenValidatorGenerator extends ValidatorGenerator {
	public BeanWrapperDrivenValidatorGenerator() {
		typeStrategy = new InterfaceTypeNameStrategy(BeanWrapper.class, "BeanWrapper");
	}
}
