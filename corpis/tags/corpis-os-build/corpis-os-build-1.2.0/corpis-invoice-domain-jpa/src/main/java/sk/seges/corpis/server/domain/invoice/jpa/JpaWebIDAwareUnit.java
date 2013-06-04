/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.shared.domain.api.HasWebId;

/**
 * @author eldzi
 */
@Entity
@Table(name = "webid_aware_units", uniqueConstraints = {@UniqueConstraint(columnNames = {JpaWebIDAwareUnit.WEB_ID, JpaUnitBase.LABEL_KEY})})
public class JpaWebIDAwareUnit extends JpaUnitBase implements HasWebId {
	private static final long serialVersionUID = 5570124910177903411L;

	public static final String WEB_ID = "webId";

	private Integer id;
		
	private String webId;
	
	@Override
	@Id
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((webId == null) ? 0 : webId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaWebIDAwareUnit other = (JpaWebIDAwareUnit) obj;
		if (webId == null) {
			if (other.webId != null)
				return false;
		} else if (!webId.equals(other.webId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebIDAwareUnit [webId=" + webId + ", " + super.toString() + "]";
	}
}