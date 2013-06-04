/**
 * 
 */
package sk.seges.crm.shared.service;

import java.util.List;

import sk.seges.crm.shared.domain.dto.LeadActivityDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 */
public interface GWTActivitiesServiceAsync {
	void findAll(AsyncCallback<List<LeadActivityDto>> result);
}
