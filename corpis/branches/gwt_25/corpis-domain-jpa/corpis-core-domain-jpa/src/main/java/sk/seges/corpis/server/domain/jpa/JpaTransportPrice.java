package sk.seges.corpis.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.server.model.base.TransportPriceBase;


@Entity
@Table(name = "transportprice")
//Ids taken from voipac csv import files, id is set manualy
//@SequenceGenerator(name = "seqTransportList", sequenceName = "SEQ_TRANSPORT", initialValue = 1)
public class JpaTransportPrice extends TransportPriceBase {
	private static final long serialVersionUID = 3960395362304929231L;

	@Column(name = "dhl_price")
	public Double getDhlPrice() {
		return super.getDhlPrice();
	}

	@Column(name = "fedex_price")
	public Double getFedExPrice() {
		return super.getFedExPrice();
	}

	@Column(name = "tnt_price")
	public Double getTntPrice() {
		return super.getTntPrice();
	}

	@Column(name = "ems_price")
	public Double getEmsPrice() {
		return super.getEmsPrice();
	}

	@Id
	public Long getId() {
		return super.getId();
	}

	@Column(name = "country")
	public String getCountry() {
		return super.getCountry();
	}

	@Column(name = "dpd_price")
	public Double getDpdPrice() {
		return super.getDpdPrice();
	}

	@Column(name = "ups_price")
	public Double getUpsPrice() {
		return super.getUpsPrice();
	}
}