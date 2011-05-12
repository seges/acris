package sk.seges.acris.showcase.mora.client.gin.smartgwt;

import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter.ErrorDisplay;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter.ErrorProxy;
import sk.seges.acris.showcase.client.view.smartgwt.core.ErrorSmartView;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SmartGWTViews extends AbstractPresenterModule {

	@Override
	protected void configure() {
		
		//Views
		bindPresenter(ErrorPresenter.class, ErrorDisplay.class, ErrorSmartView.class, ErrorProxy.class);
	}

}
