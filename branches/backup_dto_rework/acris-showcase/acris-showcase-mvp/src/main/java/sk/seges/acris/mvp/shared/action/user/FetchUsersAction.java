package sk.seges.acris.mvp.shared.action.user;

import sk.seges.acris.mvp.shared.action.core.AbstractPageAction;
import sk.seges.acris.mvp.shared.result.user.FetchUsersResult;
import sk.seges.sesam.dao.Page;

public class FetchUsersAction extends AbstractPageAction<FetchUsersResult> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private FetchUsersAction() {
	}
	
	public FetchUsersAction(Page page) {
		super(page);
	}
}