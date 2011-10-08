package sk.seges.acris.mvp.client.model.smartgwt;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.showcase.client.model.smartgwt.AbstractSmartGWTModelAdapter;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class SmartGWTModelAdapter extends AbstractSmartGWTModelAdapter {

	public ListGridRecord generateRecord(Class<?> clazz) {
		if (clazz.getName().equals(UserData.class)) {
			return GWT.create(UserDataBeanWrapper.class);
		}
		
		return null;
	}
}