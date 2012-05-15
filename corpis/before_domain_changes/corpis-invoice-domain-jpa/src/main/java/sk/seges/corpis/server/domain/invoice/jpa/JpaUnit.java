package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "units")
public class JpaUnit extends JpaUnitBase {
	private static final long serialVersionUID = 2721171150179112866L;

	public static final String ID = "id";
		
	private Integer id;
		
	@Override
	@Id
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", toString()=" + super.toString() + "]";
	}
}
