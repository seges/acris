package sk.seges.acris.generator.offline;

import sk.seges.acris.generator.client.GwtTestGenerateOfflineContent;
import sk.seges.acris.generator.client.HolyBridge;
import sk.seges.acris.generator.client.gin.mock.MockGinjector;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class OfflineGenerator extends GwtTestGenerateOfflineContent {

	private MockGinjector mockGinjector;
	
	@Override
	public String getModuleName() {
		return "sk.seges.acris.generator.Showcase";
	}
	
	@Override
	protected EntryPoint getEntryPoint(String webId, String lang) {
		return new HolyBridge();
	}

	@Override
	protected IGeneratorServiceAsync getGeneratorService() {
		if (mockGinjector == null) {
			mockGinjector = GWT.create(MockGinjector.class);
		}
		return mockGinjector.getGeneratorService();
	}
}