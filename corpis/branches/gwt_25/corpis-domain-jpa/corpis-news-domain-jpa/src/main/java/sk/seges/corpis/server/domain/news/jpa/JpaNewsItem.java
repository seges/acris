package sk.seges.corpis.server.domain.news.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sk.seges.corpis.server.domain.news.server.model.base.NewsItemBase;
import sk.seges.corpis.server.domain.news.server.model.data.NewsItemPKData;

@Entity
@Table(name = "news")
@SequenceGenerator(name = "seqNewsItem", sequenceName = "seq_news_item", initialValue = 1)
public class JpaNewsItem extends NewsItemBase {

	private static final long serialVersionUID = -8172425109207825339L;

	@Id
	public NewsItemPKData getId() {
		return super.getId();
	}

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return super.getCreated();
	}

	@Column
	public String getSubject() {
		return super.getSubject();
	};

	@Column
	public String getBody() {
		return super.getBody();
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCreated() == null) ? 0 : getCreated().hashCode());
		result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
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
		JpaNewsItem other = (JpaNewsItem) obj;
		if (getCreated() == null) {
			if (other.getCreated() != null)
				return false;
		} else if (!getCreated().equals(other.getCreated()))
			return false;
		if (getSubject() == null) {
			if (other.getSubject() != null)
				return false;
		} else if (!getSubject().equals(other.getSubject()))
			return false;
		return true;
	}
}