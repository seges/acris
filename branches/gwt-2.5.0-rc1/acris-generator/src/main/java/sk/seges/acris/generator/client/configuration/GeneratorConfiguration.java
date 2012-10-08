package sk.seges.acris.generator.client.configuration;

public interface GeneratorConfiguration {

	String getWebId();
	String getProperties();
	String getLanguage();
	String getAlias();
	
	int getContentStartIndex();
	int getContentPageSize();
}