package sk.seges.acris.pap.service;

import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("")
@ProcessorConfiguration(processor = AsyncServiceProcessor.class)
public class AsyncServiceProcessorTestConfiguration {
}