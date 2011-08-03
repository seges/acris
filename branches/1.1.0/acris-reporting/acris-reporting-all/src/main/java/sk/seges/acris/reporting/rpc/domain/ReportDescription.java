package sk.seges.acris.reporting.rpc.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import net.sf.gilead.pojo.gwt.LightEntity;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

/**
 * domain object for store basic information about report and link to report from jasperserver
 * 
 * @author marta
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "report_desc_id_seq", sequenceName = "report_desc_id_seq", initialValue = 1)
public class ReportDescription extends LightEntity implements ReportDescriptionData {

	private static final long serialVersionUID = 3793554325796093693L;
	public static final String DESCRIPTION_ATTR = "description";
	public static final String OWNER_ATTR = "owner";
	public static final String PARAMETERS_ATTR = "parametersList";

	@Id
	@GeneratedValue(generator="report_desc_id_seq")
	@Column(name = "report_id")
	private Long id;
	private String name;
	private String description;
	private Date creationDate;
	private String reportUrl;
	private String displayName;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch=FetchType.LAZY, targetEntity = ReportParameter.class)
	@OrderBy(ReportParameterData.ORDER_NUMBER)
	@JoinColumn(name="owning_report")
	private List<ReportParameterData> parametersList = null;

	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	@Override
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@Override
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	@Override
	public String getReportUrl() {
		return reportUrl;
	}
	@Override
	public void setParametersList(List<ReportParameterData> parametersList) {
//		if (parametersList == null) {
//			this.parametersList = null;
//			return;
//		}
//		this.parametersList = new ArrayList<ReportParameter>();
//		for (ReportParameterData param : parametersList) {
//			this.parametersList.add((ReportParameter) param);
//		}		
		this.parametersList = parametersList;
	}
	@Override
	public List<ReportParameterData> getParametersList() {
//		if (parametersList == null) {
//			return null;
//		}
//		List<ReportParameterData> result = new ArrayList<ReportParameterData>();
//		for (ReportParameterData param : parametersList) {
//			result.add(param);
//		}
//		return result;
		return parametersList;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
			return true;
		}
        if (obj == null) {
			return false;
		}
        if (getClass() != obj.getClass()) {
			return false;
		}
        ReportDescription other = (ReportDescription) obj;
        if (id == null) {
            if (other.id != null) {
				return false;
			}
        } else if (!id.equals(other.id)) {
			return false;
		}
        return true;
    }
	@Override
	public String getDisplayName() {
		return displayName;
	}
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}	
}
