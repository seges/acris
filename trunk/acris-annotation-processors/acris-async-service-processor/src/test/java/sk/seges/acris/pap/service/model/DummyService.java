package sk.seges.acris.pap.service.model;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dummy")
public interface DummyService extends RemoteService {

	int method();
}
