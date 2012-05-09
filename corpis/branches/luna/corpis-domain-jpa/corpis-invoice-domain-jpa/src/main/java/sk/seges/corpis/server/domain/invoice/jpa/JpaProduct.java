package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerBase;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerData;
import sk.seges.corpis.server.domain.invoice.server.model.base.ProductBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductCategoryData;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductData;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductPriceData;
import sk.seges.corpis.server.domain.invoice.server.model.data.TagData;
import sk.seges.corpis.server.domain.invoice.server.model.data.VatData;
import sk.seges.corpis.server.domain.server.model.data.NameData;

@SuppressWarnings("serial")
@Entity
@Table(name = "product")
@SequenceGenerator(name = JpaProduct.SEQ_PRODUCT, sequenceName = "seq_products", initialValue = 1)
public class JpaProduct extends ProductBase {

	protected static final String SEQ_PRODUCT = "seqProduct";

	private static final int EXT_ID_MIN_LENGTH = 1;
	private static final int EXT_ID_MAX_LENGTH = 30;

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PRODUCT)
	public Long getId() {
		return super.getId();
	}

	@Override
	@NotNull
	@Size(min = EXT_ID_MIN_LENGTH, max = EXT_ID_MAX_LENGTH)
	@Column(name = ProductData.EXT_ID, nullable = false, length = EXT_ID_MAX_LENGTH)
	public String getExtId() {
		return super.getExtId();
	}

	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
	public List<NameData> getNames() {
		return super.getNames();
	}

	@Override
	@Column(name = "description")
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	@Column(name = "weight")
	public Float getWeight() {
		return super.getWeight();
	}

	@Override
	@Column(name = "units_per_package")
	public Integer getUnitsPerPackage() {
		return super.getUnitsPerPackage();
	}

	@Override
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderBy(ProductPriceData.PRIORITY)
	@JoinTable(name = "product_product_prices")
	public List<ProductPriceData> getPrices() {
		return super.getPrices();
	}

	@Override
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderBy(value = ProductPriceData.PRIORITY)
	@JoinTable(name = "product_product_fees")
	public Set<ProductPriceData> getFees() {
		return super.getFees();
	}

	@Override
	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	public VatData getVat() {
		return super.getVat();
	}

	@Override
	@ManyToMany(mappedBy = "products")
	public List<ProductCategoryData> getCategories() {
		return super.getCategories();
	}

	@Override
	@ManyToMany
	@OrderBy(value = TagData.PRIORITY)
	public Set<TagData> getTags() {
		return super.getTags();
	}

	@Override
	@ManyToOne(targetEntity = JpaCustomerBase.class)
	public CustomerData getManufacturer() {
		return super.getManufacturer();
	}

	@Override
	@ManyToOne(targetEntity = JpaCustomerBase.class)
	public CustomerData getSeller() {
		return super.getSeller();
	}

	@Override
	@ManyToOne(targetEntity = JpaProduct.class)
	public ProductData getVariant() {
		return super.getVariant();
	}
}