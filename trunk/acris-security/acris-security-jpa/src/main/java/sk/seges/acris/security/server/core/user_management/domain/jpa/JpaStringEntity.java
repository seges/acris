package sk.seges.acris.security.server.core.user_management.domain.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaStringEntity.JpaStringEntityPK;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@IdClass(JpaStringEntityPK.class)
@Table(name = "STRING_ENTITY"/*, uniqueConstraints = @UniqueConstraint(columnNames = {JpaStringEntity.VALUE_COLUMN, JpaStringEntity.ID_COLUMN})*/)
public class JpaStringEntity implements IDomainObject<Long> {

	@Embeddable
	public static class JpaStringEntityPK implements Serializable {

		private static final long serialVersionUID = 6038377739805169457L;

		private String value;

		private Long id;

		public JpaStringEntityPK() {
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	
	private static final long serialVersionUID = 4215390700888836887L;

	public static final String VALUE_COLUMN = "value";
	public static final String ID_COLUMN = "id";

	@EmbeddedId
	private JpaStringEntityPK pk = new JpaStringEntityPK();

	public JpaStringEntity() {
	}

	public JpaStringEntity(Long id, String value) {
		pk.value = value;
		pk.id = id;
	}

	public void setId(Long id) {
		pk.id = id;
	}

	@Override
	public Long getId() {
		return pk.id;
	}

	public String getValue() {
		return pk.value;
	}

	public void setValue(String value) {
		this.pk.value = value;
	}
}