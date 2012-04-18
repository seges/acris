package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import sk.seges.corpis.server.domain.invoice.PriceData;
import sk.seges.corpis.server.domain.invoice.base.InvoiceItemBase;

@Entity
@Table(name = "invoice_items")//$NON-NLS-1$
@SequenceGenerator(name = "seqInvoiceItems", sequenceName = "SEQ_INVOICE_ITEMS", initialValue = 1)//$NON-NLS-1$ //$NON-NLS-2$
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue(value="1")
public class JpaInvoiceItemBase extends InvoiceItemBase<Long> {
	private static final long serialVersionUID = 207883043243372294L;


	private static final short DESCRIPTION_LENGTH = 250;

	public JpaInvoiceItemBase() {
		setVat(new JpaVATBase());
	}

	@ManyToOne
	public JpaInvoiceBase getInvoice() {
		return (JpaInvoiceBase) super.getInvoice();
	}

	@Id
	@GeneratedValue(generator = "seqInvoiceItems")//$NON-NLS-1$
	public Long getId() {
		return super.getId();
	}

	@Version
	public Integer getVersion() {
		return super.getVersion();
	}

	@Column(name = "COUNT", nullable = false)//$NON-NLS-1$
	public Float getCount() {
		return super.getCount();
	}

	@Column(name = "DESCRIPTION", length = DESCRIPTION_LENGTH)//$NON-NLS-1$
	public String getDescription() {
		return super.getDescription();
	}

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = PriceData.VALUE, column = @Column(name = "PRICE")),
		@AttributeOverride(name = PriceData.CURRENCY, column = @Column(name = "CURRENCY", nullable = false)) })
	public JpaPrice getPrice() {
		return (JpaPrice) super.getPrice();
	}

	@Column(name = "UNIT")//$NON-NLS-1$
	public JpaUnitBase getUnit() {
		return (JpaUnitBase) super.getUnit();
	}

	@OneToOne
	public JpaVATBase getVat() {
		return (JpaVATBase) super.getVat();
	}
}