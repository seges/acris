package sk.seges.corpis.server.domain.transportation;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.customer.jpa.JpaPersonName;
import sk.seges.corpis.server.domain.transportation.server.model.base.TransportationBase;
import sk.seges.corpis.server.domain.transportation.server.model.data.TransportationUnitData;
import sk.seges.corpis.server.domain.transportation.server.model.data.VehicleData;

@SuppressWarnings("serial")
@Entity
@Table(name = "transportation")
@SequenceGenerator(name = JpaTransportation.SEQ_TRANSPORTATION, sequenceName = "seq_transportation", initialValue = 1)
public class JpaTransportation extends TransportationBase {

	protected static final String SEQ_TRANSPORTATION = "seqTransporation";

	public JpaTransportation() {
		setPerson(new JpaPersonName());
	}
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_TRANSPORTATION)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Embedded
	public JpaPersonName getPerson() {
		return (JpaPersonName) super.getPerson();
	}

	@Override
	@Column
	public Date getTransportationDate() {
		return super.getTransportationDate();
	}

	@Override
	@OneToMany(targetEntity = JpaTransportation.class)
	public List<TransportationUnitData> getUnits() {
		return super.getUnits();
	}

	@Override
	@ManyToOne(targetEntity = JpaVehicle.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	public VehicleData getVehicle() {
		return super.getVehicle();
	}	
}