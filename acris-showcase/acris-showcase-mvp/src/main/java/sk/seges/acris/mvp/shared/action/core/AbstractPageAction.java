package sk.seges.acris.mvp.shared.action.core;

import sk.seges.sesam.dao.Page;

import com.philbeaudoin.gwtp.dispatch.shared.Result;

public abstract class AbstractPageAction<R extends Result> extends AbstractAction<R> {

	private static final long serialVersionUID = 1L;

	private Page page = Page.ALL_RESULTS_PAGE;

	protected AbstractPageAction() {
	}

	public AbstractPageAction(Page page) {
		this.page = page;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
