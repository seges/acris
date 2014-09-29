package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.domain.api.ContentPkData;
import sk.seges.acris.site.server.domain.base.ContentPkBase;
import sk.seges.acris.site.shared.domain.mock.MockContent;
import sk.seges.sesam.dao.Page;

public class MockContentInfoProvider implements ContentDataProvider {

	private MockContentFactory factory;
	
	public MockContentInfoProvider(MockContentFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public ContentData getContentForLanguage(ContentData content, String targetLanguage) {
		if(targetLanguage.equals("de")){
			//for testing navigation to home page
			return null;
		}
		MockContent foundContent = new MockContent();
		foundContent.setNiceUrl(targetLanguage + "/" + content.getNiceUrl());		
		return foundContent;
	}
	
	@Override
	public boolean exists(GeneratorToken token) {
		return true;
	}
	
	@Override
	public ContentData getContent(GeneratorToken token) {
		return factory.constructMockContent();
	}

	@Override
	public List<String> getAvailableNiceurls(Page page) {
		ContentData mockContent = factory.constructMockContent();
		List<String> contents = new ArrayList<String>();
		contents.add(mockContent.getNiceUrl());
		return contents;
	}

	@Override
	public ContentData getHomeContent(String targetLanguage, String webId) {
		MockContent homeContent = new MockContent();
		homeContent.setNiceUrl(targetLanguage + "/home");		
		ContentPkData id = new ContentPkBase();
		id.setLanguage(targetLanguage);
		id.setWebId(webId);
		homeContent.setId(id);
		return homeContent;
	}
}