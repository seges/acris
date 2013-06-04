/**
 * 
 */
package sk.seges.acris.binding.client;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;

public interface ForestBeanWrapper extends BeanWrapper<Forest> {
	public static final String PERIL_LEVEL = "perilLevel";
	public static final String TIGERS_IN_VICINITY = "tigersInVicinity";
	public static final String LIONS_IN_VICINITY = "lionsInVicinity";
	public static final String FIGHT_PROBABILITY = "fightProbability";
	public static final String DESCRIPTION = "description";
}
