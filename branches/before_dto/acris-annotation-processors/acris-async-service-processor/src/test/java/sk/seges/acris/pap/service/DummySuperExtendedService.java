/**
 * 
 */
package sk.seges.acris.pap.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

/**
 * @author ladislav.gazo
 */
public interface DummySuperExtendedService<T extends IDomainObject<?>> extends RemoteService {
	PagedResult<List<T>> findAll(String entityClassName, Page page);
}
