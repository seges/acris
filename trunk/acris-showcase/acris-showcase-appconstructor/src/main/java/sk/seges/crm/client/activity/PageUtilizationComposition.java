package sk.seges.crm.client.activity;

import sk.seges.crm.shared.domain.vto.PageUtilizationModel;

//@ViewComposer
public interface PageUtilizationComposition  {
	//@SingleSelect
	public interface PageUtilizationViewModel extends PageUtilizationModel {
		
	}
	
	//@SelectedDetail(of = PageUtilizaitonViewModel.class)
	interface Detail extends PageUtilizationViewModel {
		
	}
}
