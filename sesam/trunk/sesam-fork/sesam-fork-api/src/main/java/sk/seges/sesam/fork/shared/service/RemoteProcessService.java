/**
 * 
 */
package sk.seges.sesam.fork.shared.service;

import sk.seges.sesam.fork.shared.domain.RemoteProcess;
import sk.seges.sesam.fork.shared.domain.RemoteProcessResponse;

/**
 * @author ladislav.gazo
 */
public interface RemoteProcessService {
	RemoteProcessResponse execute(RemoteProcess process);
}
