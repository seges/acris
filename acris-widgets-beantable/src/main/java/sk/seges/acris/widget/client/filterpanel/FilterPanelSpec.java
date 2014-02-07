package sk.seges.acris.widget.client.filterpanel;

import com.google.gwt.event.dom.client.ChangeHandler;
import sk.seges.sesam.shared.model.dto.CriterionDTO;

public interface FilterPanelSpec {

	void setCommonChangeHandler(ChangeHandler handler);

	void removeCommonChangeHandler();

	CriterionDTO constructSearchCriteria();
}
