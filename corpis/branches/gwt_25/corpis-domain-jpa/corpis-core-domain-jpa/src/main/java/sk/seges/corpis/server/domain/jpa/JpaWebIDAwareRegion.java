package sk.seges.corpis.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.server.model.data.RegionCoreData;
import sk.seges.corpis.server.domain.server.model.data.WebIDAwareRegionData;

@Entity
@Table(name = "webid_aware_region", uniqueConstraints = @UniqueConstraint(columnNames = {WebIDAwareRegionData.WEB_ID, RegionCoreData.NAME}))
public class JpaWebIDAwareRegion {

	private String webId;

	@Column
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}
}