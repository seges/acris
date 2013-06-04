/**
 * 
 */
package sk.seges.acris.pap.service;

import java.util.List;

import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 */
public interface DummySuperExtendedServiceAsync<T extends IDomainObject<?>> {
	void findAll(String entityClassName, Page page, AsyncCallback<PagedResult<List<T>>> callback);
}
