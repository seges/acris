package sk.seges.corpis.server.domain.news.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import sk.seges.corpis.server.domain.news.server.model.base.NewsItemPKBase;

@Embeddable
public class JpaNewsItemPK extends NewsItemPKBase {

	private static final long serialVersionUID = 685942486238412666L;
	
	@Column(length = 8, nullable = false)
	public Long getId() {
		return super.getId();
	}
	
	@Column(length = 20, nullable = false)
	public String getCategory() {
		return super.getCategory();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getCategory() == null) ? 0 : getCategory().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result
				+ ((getLanguage() == null) ? 0 : getLanguage().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaNewsItemPK other = (JpaNewsItemPK) obj;
		if (getCategory() == null) {
			if (other.getCategory() != null)
				return false;
		} else if (!getCategory().equals(other.getCategory()))
			return false;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (getLanguage() == null) {
			if (other.getLanguage() != null)
				return false;
		} else if (!getLanguage().equals(other.getLanguage()))
			return false;
		return true;
	}
}