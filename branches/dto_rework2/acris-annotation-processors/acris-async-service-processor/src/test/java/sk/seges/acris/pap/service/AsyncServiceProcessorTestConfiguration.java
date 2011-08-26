package sk.seges.acris.pap.service;

import sk.seges.sesam.core.annotation.configuration.Configuration;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("")
@Configuration(processor = AsyncServiceProcessor.class)
public class AsyncServiceProcessorTestConfiguration {
}