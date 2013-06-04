/**
 * 
 */
package sk.seges.crm.client;

import sk.seges.acris.pivo.AbstractObjectFactory;
import sk.seges.acris.pivo.ChocolateFactory;
import sk.seges.acris.scaffold.mvp.FlowSlotPanel;
import sk.seges.acris.scaffold.mvp.SlotPresenter;
import sk.seges.crm.client.activity.ActivitiesManagementCompositionActivatorPart;
import sk.seges.crm.client.generated.presenter.CustomActivitiesManagementCompositionActivatorPart;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ladislav.gazo
 *
 */
public class Crm implements EntryPoint {
	public static final ChocolateFactory factory = new ChocolateFactory();
	
	@Override
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("rootContent");
		FlowPanel container = new FlowPanel();
		rootPanel.add(container);
		
		CustomActivitiesManagementCompositionActivatorPart activitiesManagementCompositionActivatorPart = new CustomActivitiesManagementCompositionActivatorPart(factory);
		activitiesManagementCompositionActivatorPart.ActivitiesManagementCompositionPresenter();
		
		SimplePanel spot = new SimplePanel();
		spot.addStyleName("spot");
		
		
		container.add(spot);
		
		factory.registerChocolate("slotPanel", new AbstractObjectFactory(factory) {
			
			@Override
			protected Object construct(Object... values) {
				return new FlowSlotPanel();
			}
		});
		
		SlotPresenter presenter = factory.getChocolate(ActivitiesManagementCompositionActivatorPart.ActivitiesManagementComposition);
		
		EventBus eventBus = new SimpleEventBus();
		presenter.start(spot, eventBus);

	}

}
