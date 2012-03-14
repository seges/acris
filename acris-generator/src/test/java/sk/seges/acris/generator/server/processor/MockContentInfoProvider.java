package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

public class MockContentInfoProvider implements ContentDataProvider {

	private MockContentFactory factory;
	
	public MockContentInfoProvider(MockContentFactory factory) {
		this.factory = factory;
	}
	
	public ContentData getContentForLanguage(ContentData content, String targetLanguage) {
		return content;
	}
	
	public boolean exists(GeneratorToken token) {
		return true;
	}
	
	public List<String> getAvailableNiceurls(String lang, String webId) {
		ContentData mockContent = factory.constructMockContent();
		List<String> contents = new ArrayList<String>();
		contents.add(mockContent.getNiceUrl());
		return contents;
	}

	@Override
	public ContentData getContent(GeneratorToken token) {
		return factory.constructMockContent();
	}
}