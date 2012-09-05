package sk.seges.acris.site.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.acris.site.server.domain.api.server.model.base.WebSitesBase;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.corpis.server.domain.DBConstraints;

/**
 * @author psloboda
 *
 */
@Entity
@Table(name = "web_sites")
@SequenceGenerator(name = "web_sites_id_seq", sequenceName = "web_sites_id_seq", initialValue = 1)
public class JpaWebSites extends WebSitesBase {
	private static final long serialVersionUID = 2586451235625774160L;

	@Override
	@Id
	@GeneratedValue(generator = "web_sites_id_seq")
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column(nullable = false, length = 256)
	public String getWebId() {
		return super.getWebId();
	}
	
	@Override
	@Column(nullable = false, length = 1024)
	public String getDomain() {
		return super.getDomain();
	}

	@Override
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public SiteType getType() {
		return super.getType();
	}
	
	@Override
	@Column(length = DBConstraints.LANGUAGE_LENGTH)
	public String getLanguage() {
		return super.getLanguage();
	}

}
