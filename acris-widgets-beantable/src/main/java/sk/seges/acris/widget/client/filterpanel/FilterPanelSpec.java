package sk.seges.acris.widget.client.filterpanel;

import com.google.gwt.event.dom.client.ChangeHandler;

import sk.seges.sesam.dao.Criterion;

public interface FilterPanelSpec {
    public void setCommonChangeHandler(ChangeHandler handler);
    public void removeCommonChangeHandler();
    public Criterion constructSearchCriteria();
}
