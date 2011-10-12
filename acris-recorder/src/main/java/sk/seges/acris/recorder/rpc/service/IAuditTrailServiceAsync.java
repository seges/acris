package sk.seges.acris.recorder.rpc.service;

import java.util.List;

import sk.seges.acris.recorder.rpc.domain.SessionInfoDTO;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IAuditTrailServiceAsync {
	public void getActiveSessions(AsyncCallback<List<SessionInfoDTO>> callback);
	public void getAuditLogs(StringMapper mapper, AsyncCallback<int[]> callback);

	public void logUserActivity(int event, String targetId, AsyncCallback<Void> callback);
	public void logUserActivity(int[] event, String targetId, AsyncCallback<Void> callback);
	public void logUserActivity(int[] event, int deltaTime, String targetId, AsyncCallback<Void> callback);
	public void logUserActivity(int[] event, int[] deltaTimes, String targetId, AsyncCallback<Void> callback);
	public void logUserActivity(int event, AsyncCallback<Void> callback);
	public void logUserActivity(int[] event, AsyncCallback<Void> callback);
	public void logUserActivity(int event, int deltaTime, AsyncCallback<Void> callback);
	public void logUserActivity(int[] event, int deltaTime, AsyncCallback<Void> callback);
	public void logUserActivity(int[] events, String[] targetIds, AsyncCallback<Void> callback);
	public void logUserActivity(int[] events, int[] deltaTimes, String[] targetIds, AsyncCallback<Void> callback);
}