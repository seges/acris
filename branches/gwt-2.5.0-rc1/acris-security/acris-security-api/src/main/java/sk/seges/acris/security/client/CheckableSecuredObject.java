/**
 * 
 */
package sk.seges.acris.security.client;


/**
 * Secured object where it is possible to call security check in runtime.
 *  
 * @author ladislav.gazo
 */
public interface CheckableSecuredObject {
	/**
	 * Calling check triggers checking actual authorities of a user and their
	 * implication on the security of the object.
	 */
	void check();
}
