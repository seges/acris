package sk.seges.acris.site.shared.domain.dto;

import java.util.Set;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.corpis.shared.domain.api.CountryData;

public class WebSettingsDTO implements WebSettingsData {

	private static final long serialVersionUID = 1L;

	public static class MetaDataDTO implements MetaData {

		private static final long serialVersionUID = 2L;

		private Long id;

		private MetaDataType type;

		private String content;

		public MetaDataDTO() {
		}
		
		@Override
		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public MetaDataType getType() {
			return type;
		}

		@Override
		public void setType(MetaDataType type) {
			this.type = type;
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public void setContent(String content) {
			this.content = content;
		}
	}

	private String webId;

	private String language;

	private String topLevelDomain;

	private String googleAnalyticsScript;

	private Set<MetaData> metadata;

	private Set<CountryData<?>> translations;
	
	private String parameters;
	
	private Boolean stockCountdown;
	
	private Integer stockAmountForWhichTheProductDisplay;
	
	private String constantSymbol;
	
	private Integer termOfPayment;
	
	private FTPWebSettingsData ftpWebSettings;
	
	private Integer thumbnailMaxHeight;
	
	private Integer thumbnailMaxWidth;
	
	public WebSettingsDTO() {
	}
	
	public Set<CountryData<?>> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<CountryData<?>> translations) {
		this.translations = translations;
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public String getTopLevelDomain() {
		return topLevelDomain;
	}

	public void setTopLevelDomain(String topLevelDomain) {
		this.topLevelDomain = topLevelDomain;
	}

	@Override
	public void setId(String id) {
		setWebId(id);
	}

	@Override
	public String getId() {
		return getWebId();
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String getAnalyticsScriptData() {
		return googleAnalyticsScript;
	}

	@Override
	public void setAnalyticsScriptData(String analyticsScript) {
		this.googleAnalyticsScript = analyticsScript;
	}

	@Override
	public Set<MetaData> getMetaData() {
		return metadata;
	}

	@Override
	public void setMetaData(Set<MetaData> metaData) {
		this.metadata = metaData;
	}

	@Override
	public String getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Override
	public Boolean getStockCountdown() {
		return stockCountdown;
	}

	@Override
	public void setStockCountdown(Boolean stockCountdown) {
		this.stockCountdown = stockCountdown;
	}

	@Override
	public Integer getStockAmountForWhichTheProductDisplay() {
		return stockAmountForWhichTheProductDisplay;
	}

	@Override
	public void setStockAmountForWhichTheProductDisplay(Integer stockAmountForWhichTheProductDisplay) {
		this.stockAmountForWhichTheProductDisplay = stockAmountForWhichTheProductDisplay;
	}

	@Override
	public String getConstantSymbol() {
		return constantSymbol;
	}

	@Override
	public void setConstantSymbol(String constantSymbol) {
		this.constantSymbol = constantSymbol;
	}

	@Override
	public Integer getTermOfPayment() {
		return termOfPayment;
	}

	@Override
	public void setTermOfPayment(Integer termOfPayment) {
		this.termOfPayment = termOfPayment;
	}

	@Override
	public FTPWebSettingsData getFTPWebSettingsData() {
		return ftpWebSettings;
	}

	@Override
	public void setFTPWebSettingsData(FTPWebSettingsData ftpWebSettingsData) {
		this.ftpWebSettings = ftpWebSettingsData;
	}

	@Override
	public Integer getThumbnailMaxHeight() {
		return thumbnailMaxHeight;
	}

	@Override
	public void setThumbnailMaxHeight(Integer thumbnailMaxHeight) {
		this.thumbnailMaxHeight = thumbnailMaxHeight;
	}

	@Override
	public Integer getThumbnailMaxWidth() {
		return thumbnailMaxWidth;
	}

	@Override
	public void setThumbnailMaxWidth(Integer thumbnailMaxWidth) {
		this.thumbnailMaxWidth = thumbnailMaxWidth;
	}
	
}