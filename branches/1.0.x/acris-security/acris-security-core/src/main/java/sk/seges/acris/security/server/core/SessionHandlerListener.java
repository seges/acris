package sk.seges.acris.security.server.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class SessionHandlerListener implements HttpSessionListener {

	private static final Logger log = Logger.getLogger(SessionHandlerListener.class.getCanonicalName());

	private static Map<String, HttpSession> activeSession = new Hashtable<String, HttpSession>();
	private static Map<String, Long> lastSessionAccessTimes = new Hashtable<String, Long>();
	private static Map<String, String> sessionsMappings = new Hashtable<String, String>();
	private static Map<String, MaxInactiveIntervalHistory> actualMaxInactiveIntervalMap = new Hashtable<String, MaxInactiveIntervalHistory>();
	

	private static final String DEFAULT_TIMEOUT = "DEFAULT_TIMEOUT";

	synchronized private void sessionActivate(HttpSessionEvent event) {
		final HttpSession session = event.getSession();
		session.setAttribute(DEFAULT_TIMEOUT, session.getMaxInactiveInterval());
		
		activeSession.put(session.getId(), session);

		long timeNow = System.currentTimeMillis();
		lastSessionAccessTimes.put(session.getId(), timeNow);
		actualMaxInactiveIntervalMap.put(session.getId(), new MaxInactiveIntervalHistory(session.getId()));
		if (log.isDebugEnabled()) {
			Date currentDate = new Date();
			currentDate.setTime(timeNow);
			String dateString = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(currentDate);
			log.debug(dateString + " Activating session with id: " + session.getId() + " with timeout " + session.getMaxInactiveInterval());
		}
	}

	public static void mapSessions(String containerSessionId, String clientSessionId) {
		sessionsMappings.put(clientSessionId, containerSessionId);
	}

	public static HttpSession getSession(String sessionId) {
		String mappedSessionId = sessionsMappings.get(sessionId);
		if (mappedSessionId == null) {
			return null;
		}
		return activeSession.get(mappedSessionId);
	}

	synchronized private void sessionPassivate(HttpSessionEvent event) {
		final HttpSession session = event.getSession();
		String sessionId = session.getId();
		activeSession.remove(sessionId);
		lastSessionAccessTimes.remove(sessionId);
		actualMaxInactiveIntervalMap.remove(sessionId);

		for (Entry<String, String> mappedSession : sessionsMappings.entrySet()) {
			if (sessionId.equals(mappedSession.getValue())) {
				sessionsMappings.remove(mappedSession.getKey());
				break;
			}
		}

		if (log.isDebugEnabled()) {
			Date currentDate = new Date();
			String dateString = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(currentDate);
			log.debug(dateString + " Passivating session with id: " + session.getId() + " with timeout " + session.getMaxInactiveInterval());
		}

	}

	public static void accessFromContainer(String sessionId) {
		long timeNow = System.currentTimeMillis();
		lastSessionAccessTimes.put(sessionId, timeNow);

		if (log.isDebugEnabled()) {
			Date currentDate = new Date();
			String dateString = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(currentDate);
			log.debug(dateString + " Accessing session from container with id: " + sessionId + " with timeout "
					+ activeSession.get(sessionId).getMaxInactiveInterval());
		}
	}

	public static HttpSession accessManually(String sessionId) {
		HttpSession session = getSession(sessionId);
		if (session == null) {
			return null;
		}
		Long lastAccessedTime = lastSessionAccessTimes.get(session.getId());

		long timeNow = System.currentTimeMillis();
		int delta = (int) ((timeNow - lastAccessedTime) / 1000L);

		Integer lat = (Integer) session.getAttribute(DEFAULT_TIMEOUT);
		if (actualMaxInactiveIntervalMap.containsKey(sessionId)) {
			actualMaxInactiveIntervalMap.get(sessionId).putActual(lat + delta);
		}
		session.setMaxInactiveInterval(lat + delta);

		if (log.isDebugEnabled()) {
			Date currentDate = new Date();
			String dateString = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(currentDate);
			log.debug(dateString + " Accessing session manually with id: " + sessionId + " with timeout " + session.getMaxInactiveInterval());
		}

		return session;
	}

	/**
	 * @param event httpsessionevent
	 */
	public void sessionCreated(HttpSessionEvent event) {
		sessionActivate(event);
	}

	/**
	 * @param event httpsessionevent
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		sessionPassivate(event);
	}
	
	public static Integer getPreviousMaxInactiveInterval(String sessionId) {
		if (!actualMaxInactiveIntervalMap.containsKey(sessionId)) {
			return null;
		}
		return actualMaxInactiveIntervalMap.get(sessionId).getPrevious();
	}
	
	public static void revertMaxInactiveInterval(String sessionId) {
		if (actualMaxInactiveIntervalMap.containsKey(sessionId)) {
			actualMaxInactiveIntervalMap.get(sessionId).revert();
		}
	}
	
}
