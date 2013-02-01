package sk.seges.acris.recorder.server.request;

import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

import sk.seges.acris.recorder.server.domain.SessionLog;

public class RequestInfoProvider {
	public static SessionLog getClientInfo(SessionLog sessionLog) {
		//sessionLog.setCity();
		//sessionLog.setCountry();
		//sessionLog.setIpAddress();
		HttpSession session = ServletUtils.getRequest().getSession();
		if (session != null) {
			sessionLog.setSessionId(session.getId());
			sessionLog.setSessionTime(sessionLog.getSessionTime());
		}
		//sessionLog.setState();
		return sessionLog;
	}
}
