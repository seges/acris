/**
 * 
 */
package sk.seges.acris.pap.service;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

/**
 * @author ladislav.gazo
 */
@RemoteServiceDefinition
public interface DummyExtendedService<T extends IDomainObject<?>> extends DummySuperExtendedService<T> {

}
