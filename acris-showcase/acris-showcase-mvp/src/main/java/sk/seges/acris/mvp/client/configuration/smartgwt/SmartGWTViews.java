package sk.seges.acris.mvp.client.configuration.smartgwt;

import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.mvp.client.model.smartgwt.SmartGWTModelAdapter;
import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter;
import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter.ErrorDisplay;
import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter.ErrorProxy;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginDisplay;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginProxy;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter.UserMaintenanceDisplay;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter.UserMaintenanceProxy;
import sk.seges.acris.mvp.client.view.smartgwt.core.ErrorSmartView;
import sk.seges.acris.mvp.client.view.smartgwt.user.LoginSmartView;
import sk.seges.acris.mvp.client.view.smartgwt.user.UserMaintenanceSmartView;

import com.google.inject.Singleton;
import com.philbeaudoin.gwtp.mvp.client.gin.AbstractPresenterModule;

public class SmartGWTViews extends AbstractPresenterModule {

	@Override
	protected void configure() {
		
		//Common stuff
		bind(ModelAdapter.class).to(SmartGWTModelAdapter.class).in(Singleton.class);

		//Views
		bindPresenter(ErrorPresenter.class, ErrorDisplay.class, ErrorSmartView.class, ErrorProxy.class);
	
		bindPresenter(LoginPresenter.class, LoginDisplay.class, LoginSmartView.class, LoginProxy.class);
		bindPresenter(UserMaintenancePresenter.class, UserMaintenanceDisplay.class, UserMaintenanceSmartView.class, UserMaintenanceProxy.class);
	}

}
