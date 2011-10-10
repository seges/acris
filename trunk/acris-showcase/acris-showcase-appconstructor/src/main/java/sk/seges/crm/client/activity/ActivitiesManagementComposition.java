package sk.seges.crm.client.activity;

import sk.seges.crm.shared.domain.api.CustomerModel;
import sk.seges.crm.shared.domain.api.LeadActivityModel;
import sk.seges.crm.shared.domain.api.SalesmanModel;

//@ViewComposer
public interface ActivitiesManagementComposition {
	//@SingleSelect
	interface View extends LeadActivityModel {
		//@Conjunction
		interface FilterBy {
			//@AllowEmpty
			SalesmanModel responsible();
			//@AllowEmpty
			CustomerModel customer();
		}
	}
	
	//@SelectedDetail(of = View.class)
	interface Detail extends LeadActivityModel {
		
	}
}
