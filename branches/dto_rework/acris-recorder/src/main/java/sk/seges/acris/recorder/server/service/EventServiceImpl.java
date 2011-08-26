package sk.seges.acris.recorder.server.service;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl extends  de.novanic.eventservice.service.EventServiceImpl {

	private static final long serialVersionUID = 7364041897495306444L;
	
	protected String getClientId(boolean isInitSession) {
		return ServletUtils.getRequest().getSession(isInitSession).getId();
    }
}
