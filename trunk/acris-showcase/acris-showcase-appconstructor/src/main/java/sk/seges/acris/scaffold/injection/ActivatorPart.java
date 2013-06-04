/**
 * 
 */
package sk.seges.acris.scaffold.injection;

import sk.seges.acris.pivo.ChocolateFactory;

/**
 * @author ladislav.gazo
 */
public abstract class ActivatorPart {
	protected final ChocolateFactory moduleFactory;

	public ActivatorPart(ChocolateFactory moduleFactory) {
		super();
		this.moduleFactory = moduleFactory;
	}
	
	
}
