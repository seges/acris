package sk.seges.acris.pap.service.model;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("varargs")
public interface VarargsService extends RemoteService {

	int method(Integer...args);
}
