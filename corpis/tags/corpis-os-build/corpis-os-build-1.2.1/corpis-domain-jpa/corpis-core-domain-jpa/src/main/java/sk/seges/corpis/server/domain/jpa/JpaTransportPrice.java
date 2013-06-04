package sk.seges.corpis.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;


@Entity
@Table(name = "TRANSPORTPRICE")
//Ids taken from voipac csv import files, id is set manualy
//@SequenceGenerator(name = "seqTransportList", sequenceName = "SEQ_TRANSPORT", initialValue = 1)
public class JpaTransportPrice implements IDomainObject<Integer> {
	private static final long serialVersionUID = 3960395362304929231L;

	private Integer id;
	private String country;
	private Double dhlPrice;
	private Double upsPrice;
	private Double fedExPrice;
	private Double dpdPrice;
	private Double tntPrice;
	private Double emsPrice;
	
	@Column(name = "DHL_PRICE")
	public Double getDhlPrice() {
		return dhlPrice;
	}

	public void setDhlPrice(Double dhlPrice) {
		this.dhlPrice = dhlPrice;
	}

	@Column(name = "FEDEX_PRICE")
	public Double getFedExPrice() {
		return fedExPrice;
	}

	public void setFedExPrice(Double fedExPrice) {
		this.fedExPrice = fedExPrice;
	}

	@Column(name = "TNT_PRICE")
	public Double getTntPrice() {
		return tntPrice;
	}

	public void setTntPrice(Double tntPrice) {
		this.tntPrice = tntPrice;
	}

	@Column(name = "EMS_PRICE")
	public Double getEmsPrice() {
		return emsPrice;
	}

	public void setEmsPrice(Double emsPrice) {
		this.emsPrice = emsPrice;
	}

	@Id
	//@GeneratedValue(generator = "seqTransportList")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "COUNTRY")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "DPD_PRICE")
	public Double getDpdPrice() {
		return dpdPrice;
	}

	public void setDpdPrice(Double dpdPrice) {
		this.dpdPrice = dpdPrice;
	}

	@Column(name = "UPS_PRICE")
	public Double getUpsPrice() {
		return upsPrice;
	}

	public void setUpsPrice(Double upsPrice) {
		this.upsPrice = upsPrice;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((dpdPrice == null) ? 0 : dpdPrice.hashCode());
		result = prime * result
				+ ((upsPrice == null) ? 0 : upsPrice.hashCode());
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
		JpaTransportPrice other = (JpaTransportPrice) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (dpdPrice == null) {
			if (other.dpdPrice != null)
				return false;
		} else if (!dpdPrice.equals(other.dpdPrice))
			return false;
		if (upsPrice == null) {
			if (other.upsPrice != null)
				return false;
		} else if (!upsPrice.equals(other.upsPrice))
			return false;
		return true;
	}
}
