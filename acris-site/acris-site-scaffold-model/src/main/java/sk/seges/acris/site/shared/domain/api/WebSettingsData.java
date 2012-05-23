package sk.seges.acris.site.shared.domain.api;

import java.util.Set;

import javax.validation.constraints.NotNull;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.corpis.server.domain.HasLanguage;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper 
@MetaModel
public interface WebSettingsData extends IMutableDomainObject<String>, HasWebId, HasLanguage {
 
	@BeanWrapper
	public static interface MetaData extends IMutableDomainObject<Long> {
		
		@NotNull
		MetaDataType getType();
		
		void setType(MetaDataType type);
		
		@NotNull
		String getContent();
		
		void setContent(String value);
	}
	
	public static interface ContentType {
		String name();
		
		public static enum RobotsContentType implements ContentType {
			ALL, NONE, NOINDEX, INDEX, NOFOLLOW, FOLLOW;
		}
		
		public static enum GoogleBotType implements ContentType {
			GOOGLEBOT;
		}
	}
	
	public static enum MetaContentValueType {
		SINGLE_TEXT_VALUE, SINGLE_LIST_VALUE, MULTI_LIST_VALUE, DATE_VALUE;
	}
	
	@BeanWrapper
	public static enum MetaDataType {
		AUTHOR("Author", MetaContentValueType.SINGLE_TEXT_VALUE),
		COPYRIGHT("Copyright", MetaContentValueType.SINGLE_TEXT_VALUE),
		EXPIRES("Expires", MetaContentValueType.DATE_VALUE),
		ROBOTS("Robots", MetaContentValueType.MULTI_LIST_VALUE, ContentType.RobotsContentType.values()),
		GENERATOR("Generator", MetaContentValueType.SINGLE_TEXT_VALUE),
		REFRESH("Refresh", MetaContentValueType.SINGLE_TEXT_VALUE),
		GOOGLEBOT("Googlebot", MetaContentValueType.SINGLE_LIST_VALUE, ContentType.GoogleBotType.values()),
		GOOGLE_WEBMASTER("google-site-verification", MetaContentValueType.SINGLE_TEXT_VALUE);
		
		private MetaContentValueType contentValueType;
		private ContentType[] contentTypes;
		private String name;
		
		MetaDataType(String name, MetaContentValueType contentValueType) {
			this.name = name;
			this.contentValueType = contentValueType;
		}

		MetaDataType(String name, MetaContentValueType contentValueType, ContentType... contentTypes) {
			this(name, contentValueType);
			this.contentTypes = contentTypes;
		}

		public MetaContentValueType getContentValueType() {
			return contentValueType;
		}
		
		public ContentType[] getContentTypes() {
			return contentTypes;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}

	String getParameters();
	
	void setParameters(String parameters);
	
	Set<CountryData> getTranslations();
	
	void setTranslations(Set<CountryData> translations);
	
	String getTopLevelDomain();
	
	void setTopLevelDomain(String tolLevelDomain);
	
	String getAnalyticsScriptData();
	
	void setAnalyticsScriptData(String analyticsScript);
	
	Set<MetaData> getMetaData();
	
	void setMetaData(Set<MetaData> metaData);
	
	Boolean getStockCountdown();
	
	void setStockCountdown(Boolean stockCountdown);
	
	Integer getStockAmountForWhichTheProductDisplay();
	
	void setStockAmountForWhichTheProductDisplay(Integer stockAmountForWhichTheProductDisplay);
	
	String getConstantSymbol();
	
	void setConstantSymbol(String constantSymbol);
	
	Integer getTermOfPayment();
	
	void setTermOfPayment(Integer termOfPayment);
	
	FTPWebSettingsData getFTPWebSettingsData();
	
	void setFTPWebSettingsData(FTPWebSettingsData ftpWebSettingsData);
	
	Integer getThumbnailMaxHeight();
	
	void setThumbnailMaxHeight(Integer maxHeight);
	
	Integer getThumbnailMaxWidth();
	
	void setThumbnailMaxWidth(Integer maxWidth);
	
	Integer getImageMaxHeight();
	
	void setImageMaxHeight(Integer maxHeight);
	
	Integer getImageMaxWidth();
	
	void setImageMaxWidth(Integer maxWidth);

	Integer getMaxProductCount();

	void setMaxProductCount(Integer maxProductCount);
}