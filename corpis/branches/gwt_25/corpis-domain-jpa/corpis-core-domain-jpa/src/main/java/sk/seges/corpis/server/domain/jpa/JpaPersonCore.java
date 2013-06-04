package sk.seges.corpis.server.domain.jpa;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.server.model.base.PersonCoreBase;

@Entity
@Table(name="person")
@SequenceGenerator(name = "seqPersons", sequenceName = "SEQ_PERSONS", initialValue = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
public class JpaPersonCore extends PersonCoreBase {

	private static final long serialVersionUID = -3023607067512088213L;

	public JpaPersonCore() {
		setPerson(new JpaPersonName());
	}

	@Id
	@GeneratedValue(generator = "seqPersons")//$NON-NLS-1$
	public Long getId() {
		return super.getId();
	}

	@Embedded
	public JpaPersonName getPerson() {
		return (JpaPersonName) super.getPerson();
	}
}