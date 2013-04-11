/**
 * 
 */
package sk.seges.acris.bpm.client.engine;

/**
 * Serves as interface for various DI/Repository services responsible for instantiating specific bean by name.
 * 
 * @author ladislav.gazo
 */
public interface BeanProvider {
	<T extends Object> T getChocolate(String beanName);
}
