package sk.seges.crm.client.activity;

import sk.seges.crm.shared.domain.api.CustomerModel;

public interface CustomersManagementComposition {
	//@SingleSelect
	interface View extends CustomerModel {
		
	}
	
	//@SelectedDetail(of = View.class)
	interface Detail extends CustomerModel {
		
	}
}
