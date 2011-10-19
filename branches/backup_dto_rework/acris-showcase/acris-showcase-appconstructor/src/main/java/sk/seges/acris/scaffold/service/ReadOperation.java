/**
 * 
 */
package sk.seges.acris.scaffold.service;

import java.util.List;

/**
 * @author ladislav.gazo
 */
public interface ReadOperation extends ServiceOperation {
	List<String> findAll();
}
