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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import net.sf.gilead.pojo.java5.LightEntity;
import sk.seges.sesam.domain.IDomainObject;

/**
 * domain object for store basic information about report and link to report from jasperserver
 * 
 * @author marta
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "report_desc_id_seq", sequenceName = "report_desc_id_seq", initialValue = 1)
public class ReportDescription extends LightEntity implements IDomainObject<Long> {

	private static final long serialVersionUID = 3793554325796093693L;
	public static final String NAME_ATTR = "name";
	public static final String DESCRIPTION_ATTR = "description";
	public static final String OWNER_ATTR = "owner";
	public static final String CREATION_DATE_ATTR = "creationDate";
	public static final String PARAMETERS_ATTR = "parametersList";

	@Id
	@GeneratedValue(generator="report_desc_id_seq")
	@Column(name = "report_id")
	private Long id;
	private String name;
	private String description;
	private Date creationDate;
	private String reportUrl;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch=FetchType.LAZY)
	private List<ReportParameter> parametersList = null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setParametersList(List<ReportParameter> parametersList) {
		this.parametersList = parametersList;
	}
	public List<ReportParameter> getParametersList() {
		return parametersList;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReportDescription other = (ReportDescription) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }	
}
