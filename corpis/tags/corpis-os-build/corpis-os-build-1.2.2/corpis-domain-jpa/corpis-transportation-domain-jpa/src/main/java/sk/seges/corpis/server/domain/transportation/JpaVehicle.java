package sk.seges.corpis.server.domain.transportation;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.transportation.server.model.base.VehicleBase;

@SuppressWarnings("serial")
@Entity
@Table(name = "vehicles")
public class JpaVehicle extends VehicleBase {

	@Override
	@Id
	public String getId() {
		return super.getId();
	}
	
}
