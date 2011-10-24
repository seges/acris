package sk.seges.acris.test.shared.service;

import sk.seges.acris.core.client.annotation.RemoteServicePath;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServicePath("test-service/cardpay")
public interface CardPayRemoteService extends RemoteService {

	String computeSign(String vs, String res, String ac);

}