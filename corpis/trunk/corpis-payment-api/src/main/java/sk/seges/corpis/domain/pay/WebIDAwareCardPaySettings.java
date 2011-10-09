/**
 * 
 */
package sk.seges.corpis.domain.pay;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;
import sk.seges.corpis.domain.pay.tatra.CardPaySettings;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "webid_aware_cardpay_settings")//, uniqueConstraints = @UniqueConstraint(columnNames = {WebIDAwareCardPaySettings.WEB_ID, CardPaySettings.MID}))
@SequenceGenerator(name = WebIDAwarePaymentMethodSettings.SEQ_PAYMENT_METHOD_SETTINGS, sequenceName = WebIDAwarePaymentMethodSettings.SEQ_DB_NAME_PAYMENT_METHOD_SETTINGS, initialValue = 1)
public class WebIDAwareCardPaySettings extends LightEntity implements WebIDAwarePaymentMethodSettings {
	private static final long serialVersionUID = 3252908128497837831L;
	
	@Id
	@GeneratedValue(generator = SEQ_PAYMENT_METHOD_SETTINGS)
	private Long id;
	
	private String webId;
	
	@Embedded
	private CardPaySettings settings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public CardPaySettings getSettings() {
		return settings;
	}

	public void setSettings(CardPaySettings settings) {
		this.settings = settings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((webId == null) ? 0 : webId.hashCode());
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
		WebIDAwareCardPaySettings other = (WebIDAwareCardPaySettings) obj;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (webId == null) {
			if (other.webId != null)
				return false;
		} else if (!webId.equals(other.webId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebIDAwareCardPaySettings [id=" + id + ", settings=" + settings + ", webId=" + webId + "]";
	}
}
