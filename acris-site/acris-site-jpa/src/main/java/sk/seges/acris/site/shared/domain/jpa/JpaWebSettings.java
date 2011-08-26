package sk.seges.acris.site.shared.domain.jpa;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO;
import sk.seges.corpis.shared.domain.api.CountryData;
import sk.seges.corpis.shared.domain.api.DBConstraints;
import sk.seges.corpis.shared.domain.jpa.JpaCountry;

@Entity
@Table(name = "web_settings")
public class JpaWebSettings extends WebSettingsDTO {

	@Entity
	@Table(name = "web_meta_data")
	@SequenceGenerator(name = "meta_data_id_seq", sequenceName = "meta_data_id_seq", initialValue = 1)
	public static class JpaMetaData extends MetaDataDTO {

		private static final long serialVersionUID = 3L;

		public JpaMetaData() {
		}
		
		@Override
		@Id
		@GeneratedValue(generator = "meta_data_id_seq")
		public Long getId() {
			return super.getId();
		}
		
		@Override
		@Column(nullable = false)
		public String getContent() {
			return super.getContent();
		}
		
		@Override
		@Column(nullable = false)
		public MetaDataType getType() {
			return super.getType();
		}
	}
	
	private static final long serialVersionUID = 2L;

	public JpaWebSettings() {
	}
	
	@Override
	@Id
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(nullable = true, length = DBConstraints.ANALYTICS_LENGTH)
	public String getAnalyticsScriptData() {
		return super.getAnalyticsScriptData();
	}

	@Override
	@Column(length=1024, nullable = true)
	public String getParameters() {
		return super.getParameters();
	}
		
	@Override
	@Column(length = DBConstraints.LANGUAGE_LENGTH)
	public String getLanguage() {
		return super.getLanguage();
	}

	@Override
	@Column
	public String getTopLevelDomain() {
		return super.getTopLevelDomain();
	}
	
	@Override
	@Column
	public String getWebId() {
		return super.getWebId();
	}
	
	@Override
	@OneToMany(fetch = FetchType.LAZY, targetEntity = JpaCountry.class)
	public Set<CountryData<?>> getTranslations() {
		return super.getTranslations();
	}
	
	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, targetEntity = JpaMetaData.class, cascade={CascadeType.ALL})
	public Set<MetaData> getMetaData() {
		return super.getMetaData();
	}
}