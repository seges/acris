package sk.seges.acris.site.shared.domain.api;

import java.util.Set;

import javax.validation.constraints.NotNull;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.domain.shared.domain.ftp.FTPWebSettings;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasLanguage;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper 
@MetaModel
@DomainInterface
@BaseObject
public interface WebSettings extends IMutableDomainObject<String>, HasWebId, HasLanguage {
 
	@BeanWrapper
	@DomainInterface
	@BaseObject
	public static interface MetaData extends IMutableDomainObject<Long> {
		
		@NotNull
		MetaDataType type();
		
		@NotNull
		String content();		
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

	String parameters();
	
	Set<CountryData> translations();
	
	String topLevelDomain();
	
	String analyticsScriptData();
	
	Set<MetaData> metaData();
	
	Boolean stockCountdown();
	
	Integer stockAmountForWhichTheProductDisplay();
	
	String constantSymbol();
	
	Integer termOfPayment();
	
	FTPWebSettings ftpWebSettings();
	
	Integer thumbnailMaxHeight();
	
	Integer thumbnailMaxWidth();
	
	Integer imageMaxHeight();
	
	Integer imageMaxWidth();
	
	Integer maxProductCount();
}