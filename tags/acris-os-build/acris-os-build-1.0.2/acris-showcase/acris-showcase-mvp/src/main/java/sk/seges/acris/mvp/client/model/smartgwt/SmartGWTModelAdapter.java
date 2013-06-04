package sk.seges.acris.mvp.client.model.smartgwt;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTOBeanWrapper;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class SmartGWTModelAdapter implements ModelAdapter {

	@SuppressWarnings("unchecked")
	public <T> T[] convertUsersForGrid(List<UserData> users) {
		List<ListGridRecord> result = new ArrayList<ListGridRecord>();

		for (UserData user : users) {
			ListGridRecord record = GWT.create(GenericUserDTOBeanWrapper.class);
			((BeanWrapper<UserData>) record).setBeanWrapperContent(user);
			result.add(record);
		}
		return (T[]) result.toArray(new ListGridRecord[] {});
	}
}