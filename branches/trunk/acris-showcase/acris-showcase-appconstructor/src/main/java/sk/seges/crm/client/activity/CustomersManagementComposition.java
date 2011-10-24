package sk.seges.crm.client.activity;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.crm.shared.domain.api.CustomerModel;

public interface CustomersManagementComposition {
	@Singleselect
	interface View extends CustomerModel {
		
	}
	
	@SelectedDetail(of = View.class)
	interface Detail extends CustomerModel {
		
	}
}
