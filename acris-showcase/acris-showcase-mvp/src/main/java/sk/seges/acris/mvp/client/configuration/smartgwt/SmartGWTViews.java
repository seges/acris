package sk.seges.acris.mvp.client.configuration.smartgwt;

import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.mvp.client.model.smartgwt.SmartGWTModelAdapter;
import sk.seges.acris.mvp.client.presenter.ErrorPresenter;
import sk.seges.acris.mvp.client.presenter.ErrorPresenter.ErrorDisplay;
import sk.seges.acris.mvp.client.presenter.ErrorPresenter.ErrorProxy;
import sk.seges.acris.mvp.client.presenter.UserMaintenancePresenter;
import sk.seges.acris.mvp.client.presenter.UserMaintenancePresenter.UserMaintenanceDisplay;
import sk.seges.acris.mvp.client.presenter.UserMaintenancePresenter.UserMaintenanceProxy;
import sk.seges.acris.mvp.client.view.smartgwt.ErrorSmartView;
import sk.seges.acris.mvp.client.view.smartgwt.UserMaintenanceSmartView;

import com.google.inject.Singleton;
import com.philbeaudoin.gwtp.mvp.client.gin.AbstractPresenterModule;

public class SmartGWTViews extends AbstractPresenterModule {

	@Override
	protected void configure() {
		
		//Common stuff
		bind(ModelAdapter.class).to(SmartGWTModelAdapter.class).in(Singleton.class);

		//Views
		bindPresenter(ErrorPresenter.class, ErrorDisplay.class, ErrorSmartView.class, ErrorProxy.class);
	
		bindPresenter(UserMaintenancePresenter.class, UserMaintenanceDisplay.class, UserMaintenanceSmartView.class, UserMaintenanceProxy.class);
	}

}
