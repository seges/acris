package sk.seges.acris.recorder.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.seges.acris.recorder.rpc.domain.AuditLog;
import sk.seges.acris.recorder.rpc.domain.SessionInfoDTO;
import sk.seges.acris.recorder.rpc.domain.SessionLog;
import sk.seges.acris.recorder.rpc.event.decoding.AbstractGenericEventIterator;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.rpc.service.IAuditTrailService;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;
import sk.seges.acris.recorder.server.dao.IAuditLogDAO;
import sk.seges.acris.recorder.server.provider.ISessionLogDAO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author PSimun
 */
@Service
public class AuditTrailService extends RemoteServiceServlet implements IAuditTrailService {

	private static final long serialVersionUID = 4268680646784670671L;

	@Autowired
	private IAuditLogDAO auditLogDAO;

	@Autowired
	private ISessionLogDAO sessionLogDAO;

//	@Autowired
//	public AuditTrailService(@Qualifier("persistentBeanManager") PersistentBeanManager lazyManager) {
//		super(lazyManager);
//	}
		
	@Override
	public void logUserActivity(int event) {
		int[] eventList = new int[1];
		eventList[0] = event;
		logUserActivity(eventList);
	}

	@Override
	public void logUserActivity(int[] event) {
		logUserActivity(event, new String[0]);
	}

	@Override
	public void logUserActivity(int event, String targetId) {
		int[] eventList = new int[1];
		eventList[0] = event;
		logUserActivity(eventList, targetId);
	}

	@Override
	public void logUserActivity(int[] event, String targetId) {
		String[] targets = new String[1];
		targets[0] = targetId;
		logUserActivity(event, targets);
	}

	@Override
	public List<SessionInfoDTO> getActiveSessions() {
		List<SessionLog> sessionLogs = sessionLogDAO.load();
		List<SessionInfoDTO> resultSessions = new ArrayList<SessionInfoDTO>();
		
		for (SessionLog sessionLog : sessionLogs) {
			SessionInfoDTO sessionInfoDTO = new SessionInfoDTO();
			sessionInfoDTO.setSessionId(sessionLog.getSessionId());
			resultSessions.add(sessionInfoDTO);
		}
		
		return resultSessions;
	}

	@Override
	public void logUserActivity(int[] event, int deltaTime) {
		int[] deltaTimes = new int[1];
		deltaTimes[0] = deltaTime;
		logUserActivity(event, deltaTimes, new String[0]);
	}

	@Override
	public void logUserActivity(int[] event, int deltaTime, String targetId) {
		int[] deltaTimes = new int[1];
		deltaTimes[0] = deltaTime;

		String[] targets = new String[1];
		targets[0] = targetId;

		logUserActivity(event, deltaTimes, targets);
	}

	@Override
	public void logUserActivity(int[] event, int[] deltaTimes, String targetId) {
		String[] targets = new String[1];
		targets[0] = targetId;
		logUserActivity(event, deltaTimes, targets);		
	}

	@Override
	public void logUserActivity(int event, int deltaTime) {
		//ServletUtils.getRequest()
		int[] eventList = new int[1];
		eventList[0] = event;
		logUserActivity(eventList, deltaTime);
	}

	@Override
	public void logUserActivity(int[] events, String[] targetIds) {
		logUserActivity(events, new int[0], targetIds);
	}

	@Override
	public void logUserActivity(int[] events, int[] deltaTimes, String[] targetIds) {
		AbstractGenericEventIterator ageIterator = new AbstractGenericEventIterator(events, deltaTimes, targetIds);
		
		while (ageIterator.hasNext()) {
			AbstractGenericEvent age = ageIterator.next();
			
			AuditLog auditLog = new AuditLog();
			auditLog.setEvent(ageIterator.getDecodedEvent());
			//TODO auditLog.setEventTime(age.getDeltaTime());
			
			if (age instanceof AbstractGenericTargetableEvent) {
				auditLog.setTargetElement(((AbstractGenericTargetableEvent)age).getRelatedTargetId());
			}
			
			//TODO auditLog.setSessionInfo();
			auditLogDAO.add(auditLog);
		}
	}

	@Override
	public int[] getAuditLogs(StringMapper mapper) {
		List<AuditLog> auditLogs = auditLogDAO.load();
		
		int size = 0;
		
		for (AuditLog auditLog : auditLogs) {
			size += auditLog.getEvent().size();
		}
		
		int[] events = new int[size]; 

		int i = 0;
		for (AuditLog auditLog : auditLogs) {
			for (Integer event : auditLog.getEvent()) {
				events[i++] = event;
			}
			if (auditLog.getTargetElement() != null && auditLog.getTargetElement().length() > 0) {
				mapper.add(auditLog.getTargetElement());
			}
		}

		return events;
	}
}