package sk.seges.crm.client.activity;

import sk.seges.acris.scaffold.model.view.compose.SelectedDetail;
import sk.seges.acris.scaffold.model.view.compose.ViewComposer;
import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.crm.shared.domain.vto.PageUtilizationModel;

@ViewComposer
public interface PageUtilizationComposition  {
	@Singleselect
	public interface PageUtilizationViewModel extends PageUtilizationModel {
		
	}
	
	@SelectedDetail(of = PageUtilizationViewModel.class)
	interface Detail extends PageUtilizationViewModel {
		
	}
}
