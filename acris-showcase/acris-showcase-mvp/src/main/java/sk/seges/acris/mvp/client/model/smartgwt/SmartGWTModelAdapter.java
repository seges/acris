package sk.seges.acris.mvp.client.model.smartgwt;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.GenericUserBeanWrapper;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class SmartGWTModelAdapter implements ModelAdapter {

	@SuppressWarnings("unchecked")
	public <T> T[] convertUsersForGrid(List<GenericUser> users) {
		List<ListGridRecord> result = new ArrayList<ListGridRecord>();

		for (GenericUser user : users) {
			ListGridRecord record = GWT.create(GenericUserBeanWrapper.class);
			((BeanWrapper<GenericUser>) record).setBeanWrapperContent(user);
			result.add(record);
		}

		return (T[]) result.toArray(new ListGridRecord[] {});
	}
}