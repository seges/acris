package sk.seges.acris.mvp.client.presenter.user;

import java.util.List;

import sk.seges.acris.mvp.client.configuration.NameTokens;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter.UserMaintenanceDisplay;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter.UserMaintenanceProxy;
import sk.seges.acris.mvp.shared.action.user.FetchUsersAction;
import sk.seges.acris.mvp.shared.result.user.FetchUsersResult;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class UserMaintenancePresenter extends Presenter<UserMaintenanceDisplay, UserMaintenanceProxy> {

	private final ActionManager actionManager;

	@Inject
	public UserMaintenancePresenter(ActionManager actionManager, EventBus eventBus, UserMaintenanceDisplay view, UserMaintenanceProxy proxy) {
		super(eventBus, view, proxy);
		this.actionManager = actionManager;
	}

	@ProxyStandard
	@NameToken(NameTokens.USER_MAINTENANCE_PAGE)
	public interface UserMaintenanceProxy extends Proxy<UserMaintenancePresenter>, Place {}

	public interface UserMaintenanceDisplay extends View {
		void setData(PagedResult<List<UserData<?>>> users);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		fetchData();
	}
	
	protected void fetchData() {
		actionManager.execute(new FetchUsersAction(Page.ALL_RESULTS_PAGE), new DefaultAsyncCallback<FetchUsersResult>() {

			@Override
			public void onSuccess(FetchUsersResult result) {
				getView().setData(result.getUsers());
			}
		});
	}
	
	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}
}