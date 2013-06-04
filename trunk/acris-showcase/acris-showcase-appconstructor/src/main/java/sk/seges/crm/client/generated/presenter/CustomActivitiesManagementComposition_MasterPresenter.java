/**
 * 
 */
package sk.seges.crm.client.generated.presenter;

import java.util.List;

import sk.seges.crm.client.activity.presenter.ActivitiesManagementComposition_MasterDisplay;
import sk.seges.crm.client.activity.presenter.ActivitiesManagementComposition_MasterPresenter;
import sk.seges.crm.client.activity.view.ActivitiesManagementComposition_MasterPanel;
import sk.seges.crm.shared.domain.dto.LeadActivityDto;
import sk.seges.crm.shared.service.GWTActivitiesServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 *
 */
public class CustomActivitiesManagementComposition_MasterPresenter extends ActivitiesManagementComposition_MasterPresenter {
	private final GWTActivitiesServiceAsync service;
	
	public CustomActivitiesManagementComposition_MasterPresenter(
			ActivitiesManagementComposition_MasterDisplay display, GWTActivitiesServiceAsync service) {
		super(display);
		this.service = service;
	}

	@Override
	public void refresh() {
		service.findAll(new AsyncCallback<List<LeadActivityDto>>() {
			
			@Override
			public void onSuccess(List<LeadActivityDto> result) {
				((ActivitiesManagementComposition_MasterPanel)display).setValue(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		super.refresh();
	}
}
