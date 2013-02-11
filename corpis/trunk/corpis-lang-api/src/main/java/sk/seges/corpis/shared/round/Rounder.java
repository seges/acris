/**
 * 
 */
package sk.seges.corpis.shared.round;

/**
 * @author eldzi
 */
public interface Rounder {
	<T extends Number> T round(T value);
}
