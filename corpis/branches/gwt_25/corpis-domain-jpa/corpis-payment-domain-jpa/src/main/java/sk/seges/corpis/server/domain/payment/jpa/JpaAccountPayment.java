package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sk.seges.corpis.server.domain.payment.server.model.data.AccountPaymentData;
import sk.seges.corpis.shared.domain.finance.api.ECurrency;

@Entity
@DiscriminatorValue("2")
public class JpaAccountPayment extends JpaPayment implements AccountPaymentData {

	private static final long serialVersionUID = 7148516696134113859L;

    private Double amount;
    private long bankSuffix;
    private long cSymbol;
    private long sSymbol;
    private long vSymbol;
    private ECurrency currency;

    @Column(name="amount")
    public Double getAmount() {
    	return amount;
    };

    @Override
    public void setAmount(Double amount) {
    	this.amount = amount;
    }
    
    @Column(name="bankSuffix")
    public long getBankSuffix() {
    	return bankSuffix;
    };
    
    @Override
    public void setBankSuffix(long bankSuffix) {
    	this.bankSuffix = bankSuffix;
    }
    
    @Column(name="cSymbol")
    public long getCSymbol() {
    	return cSymbol;
    };
    
    @Override
    public void setCSymbol(long cSymbol) {
    	this.cSymbol = cSymbol;
    }
    
    @Column(name="sSymbol")
    public long getSSymbol() {
    	return sSymbol;
    };

    @Override
    public void setSSymbol(long sSymbol) {
		this.sSymbol = sSymbol;
	}
    
    @Column(name="vSymbol")
    public long getVSymbol() {
    	return vSymbol;
    };
    
    @Override
    public void setVSymbol(long vSymbol) {
    	this.vSymbol = vSymbol;
    }
    
    @Column(name="ccurrency")
    public ECurrency getCurrency() {
    	return currency;
    };

    @Override
    public void setCurrency(ECurrency currency) {
    	this.currency = currency;
    }    
}