package sk.seges.corpis.server.domain.manufacture.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.manufacture.server.model.data.ManufactureProductData;
import sk.seges.corpis.server.domain.product.jpa.JpaProduct;

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = JpaManufactureProduct.SEQ_MANUFACTURE_PRODUCT, sequenceName = "seq_manufacture_product", initialValue = 1)
@Table(name = "manufacture_product")
public class JpaManufactureProduct extends JpaProduct implements ManufactureProductData {

	protected static final String SEQ_MANUFACTURE_PRODUCT = "seqManufactureProduct";

	private float productionRate;

	@Column
	public float getProductionRate() {
		return productionRate;
	}

	public void setProductionRate(float productionRate) {
		this.productionRate = productionRate;
	}
}