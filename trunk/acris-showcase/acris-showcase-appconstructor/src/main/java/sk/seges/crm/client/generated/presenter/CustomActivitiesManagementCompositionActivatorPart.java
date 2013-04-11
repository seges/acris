/**
 * 
 */
package sk.seges.crm.client.generated.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.seges.acris.pivo.ChocolateFactory;
import sk.seges.acris.scaffold.mvp.SlotPresenter;
import sk.seges.acris.scaffold.mvp.SlotPresenter.SlotDispay;
import sk.seges.crm.client.activity.ActivitiesManagementCompositionActivatorPart;
import sk.seges.crm.client.activity.presenter.ActivitiesManagementComposition_DetailPresenter;
import sk.seges.crm.client.activity.view.ActivitiesManagementComposition_DetailPanel;
import sk.seges.crm.client.activity.view.ActivitiesManagementComposition_MasterPanel;
import sk.seges.crm.shared.domain.dto.LeadActivityDto;
import sk.seges.crm.shared.service.GWTActivitiesServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 * 
 */
public class CustomActivitiesManagementCompositionActivatorPart extends
		ActivitiesManagementCompositionActivatorPart {

	public CustomActivitiesManagementCompositionActivatorPart(
			ChocolateFactory moduleFactory) {
		super(moduleFactory);
	}

	@Override
	protected Object constructActivitiesManagementComposition(Object... values) {
		return new SlotPresenter((SlotDispay) values[0],
				new CustomActivitiesManagementComposition_MasterPresenter(
						new ActivitiesManagementComposition_MasterPanel(),
						new MockActivitiesService()),
				new ActivitiesManagementComposition_DetailPresenter(
						new ActivitiesManagementComposition_DetailPanel()));
	}

	public class MockActivitiesService implements GWTActivitiesServiceAsync {
		@Override
		public void findAll(AsyncCallback<List<LeadActivityDto>> result) {
			List<LeadActivityDto> activities = new ArrayList<LeadActivityDto>();
			for (int i = 0; i < 20; i++) {
				LeadActivityDto activity = new LeadActivityDto();
				activity.setNote("huhu" + i);
				activity.setWhen(new Date());
				activities.add(activity);
			}
			result.onSuccess(activities);
		}
	}
}
