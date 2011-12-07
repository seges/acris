package sk.seges.crm.client.activity;

import java.util.Date;

import sk.seges.acris.scaffold.model.view.compose.Conjunction;
import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.acris.scaffold.model.view.compose2.AllowEmpty;
import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.crm.shared.domain.api.CustomerModel;
import sk.seges.crm.shared.domain.api.LeadActivityModel;
import sk.seges.crm.shared.domain.api.SalesmanModel;

@ViewComposer
public interface ActivitiesManagementComposition {
	@sk.seges.acris.scaffold.annotation.View
	@Singleselect
	interface Master extends LeadActivityModel {
		Date when();
		SalesmanModel executedBy();
		
		@Conjunction
		interface FilterBy {
			@AllowEmpty
			SalesmanModel responsible();
			@AllowEmpty
			CustomerModel customer();
		}
	}
	
	@sk.seges.acris.scaffold.annotation.View
	@SelectedDetail(of = Master.class)
	interface Detail extends LeadActivityModel {
		
	}
	
}
