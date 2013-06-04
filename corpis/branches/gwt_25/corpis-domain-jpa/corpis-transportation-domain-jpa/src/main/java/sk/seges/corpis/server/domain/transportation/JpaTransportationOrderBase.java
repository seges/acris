package sk.seges.corpis.server.domain.transportation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;

import sk.seges.corpis.server.domain.server.model.data.PersonNameData;
import sk.seges.corpis.server.domain.transportation.server.model.base.TransportationOrderBase;
import sk.seges.corpis.server.domain.transportation.server.model.data.TransportationData;

@MappedSuperclass
public abstract class JpaTransportationOrderBase extends TransportationOrderBase {
	private static final long serialVersionUID = -7365885741525730451L;

	@Override
	@Column
	public Date getOrderDate() {
		return super.getOrderDate();
	}

	@Override
	@Embedded
	public PersonNameData getPerson() {
		return super.getPerson();
	}
	
	@Override
	@OneToOne(targetEntity = JpaTransportation.class)
	public TransportationData getTransportation() {
		return super.getTransportation();
	}
}