package sk.seges.acris.widget.client.filterpanel;

import sk.seges.sesam.dao.Criterion;

import com.google.gwt.event.dom.client.ChangeHandler;

public interface FilterPanelSpec {
    public void setCommonChangeHandler(ChangeHandler handler);
    public void removeCommonChangeHandler();
    public Criterion constructSearchCriteria();
}
