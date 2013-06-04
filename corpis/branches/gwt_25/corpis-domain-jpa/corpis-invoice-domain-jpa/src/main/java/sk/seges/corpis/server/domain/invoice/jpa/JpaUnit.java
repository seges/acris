package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
public class JpaUnit extends JpaUnitBase {
	private static final long serialVersionUID = 2721171150179112866L;

	@Id
	public Long getId() {
		return super.getId();
	}
}
