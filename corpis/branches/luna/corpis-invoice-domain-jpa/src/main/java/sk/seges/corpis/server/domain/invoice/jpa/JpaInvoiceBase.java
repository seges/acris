package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerBase;
import sk.seges.corpis.server.domain.customer.jpa.JpaPersonName;
import sk.seges.corpis.server.domain.invoice.RemittanceType;
import sk.seges.corpis.server.domain.invoice.TransportType;
import sk.seges.corpis.server.domain.invoice.base.InvoiceBase;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceItemData;
import sk.seges.corpis.shared.domain.invoice.api.RemittanceData;

@Entity
@SequenceGenerator(name = "seqInvoices", sequenceName = "SEQ_INVOICES", initialValue = 1)//$NON-NLS-1$ //$NON-NLS-2$
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue(value="1")
@Table(name = "INVOICE"/*, uniqueConstraints = {@UniqueConstraint(columnNames = {"invoiceId","prepaid","incomingInvoiceType"})}*/)//$NON-NLS-1$
public class JpaInvoiceBase extends InvoiceBase<Integer> {

	private static final long serialVersionUID = 7242853578333348764L;

	public JpaInvoiceBase() {
		setInvoiceItems(new HashSet<InvoiceItemData<?>>());
		setRemittances(new HashSet<RemittanceData<?>>());
	}

	@Column(name = "TAX_DATE")//$NON-NLS-1$
	public Date getTaxDate() {
		return super.getTaxDate();
	}

	@Column(name = "PAYBACK_DATE")//$NON-NLS-1$
	public Date getPaybackDate() {
		return super.getPaybackDate();
	}

	@Column(name = "CREATION_DATE")//$NON-NLS-1$
	public Date getCreationDate() {
		return super.getCreationDate();
	}

	@Column(name = "INVOICE_ID", insertable = true, updatable = true) //$NON-NLS-1$
	public Integer getInvoiceId() {
		return super.getInvoiceId();
	}

	@ManyToOne
	public JpaCustomerBase getCustomer() {
		return (JpaCustomerBase) super.getCustomer();
	}

	@Id
	@GeneratedValue(generator = "seqInvoices")//$NON-NLS-1$
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "CSYMBOL")//$NON-NLS-1$
	public String getCsymbol() {
		return super.getCsymbol();
	}

	@Column(name = "SSYMBOL")//$NON-NLS-1$
	public String getSsymbol() {
		return super.getSsymbol();
	}

	@Column(name = "VSYMBOL")//$NON-NLS-1$
	public String getVsymbol() {
		return super.getVsymbol();
	}

	@Column(name = "PAID")//$NON-NLS-1$
	public Boolean getPaid() {
		return super.getPaid();
	}

	@Column(name = "PREPAID")//$NON-NLS-1$
	public Boolean getPrepaid() {
		return super.getPrepaid();
	}

	@Column(name = "INCOMMING_INVOICE_TYPE")//$NON-NLS-1$
	public Boolean getIncomingInvoiceType() {
		return super.getIncomingInvoiceType();
	}

	@OneToMany(mappedBy = "invoice", cascade = { CascadeType.PERSIST })//$NON-NLS-1$
	public Set<InvoiceItemData<?>> getInvoiceItems() {
		return super.getInvoiceItems();
	}

	@Column(name="PENNY_BALANCE")
	public Double getPennyBalance() {
		return super.getPennyBalance();
	}

	@Version
	public Integer getVersion() {
		return super.getVersion();
	}

	@OneToMany(mappedBy = "invoice", cascade = { CascadeType.PERSIST }, targetEntity = JpaRemittanceBase.class) //$NON-NLS-1$
	public Set<RemittanceData<?>> getRemittances() {
		return super.getRemittances();
	}

	@ManyToOne
	public JpaPersonName getCreator() {
		return (JpaPersonName) super.getCreator();
	}

	@Column(name="TEXT")
	public String getInvoiceText() {
		return super.getInvoiceText();
	}

	@Column(name = "REMITTANCE_TYPE")//$NON-NLS-1$
	public RemittanceType getRemittanceType() {
		return super.getRemittanceType();
	}

	@Column(name = "TRANSPORT_TYPE")//$NON-NLS-1$
	public TransportType getTransportType() {
		return super.getTransportType();
	}

	@Column(name="ADD_VAT")
	public Boolean getAddVAT() {
		return super.getAddVAT();
	}
}