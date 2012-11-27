package sk.seges.acris.site;

import java.util.Set;

import javax.validation.constraints.NotNull;

import sk.seges.acris.site.ftp.FTPWebSettings;
import sk.seges.acris.site.shared.domain.api.MetaDataType;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasLanguage;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface WebSettings extends IMutableDomainObject<String>, HasWebId, HasLanguage {
 
	@DomainInterface
	@BaseObject
	static interface MetaData extends IMutableDomainObject<Long> {
		
		@NotNull
		MetaDataType type();
		
		@NotNull
		String content();		
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