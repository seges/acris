package sk.seges.acris.reporting.shared.domain.dto;

import java.util.Date;
import java.util.List;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

/**
 * domain object for store basic information about report and link to report from jasperserver
 * 
 * @author marta
 *
 */
public class ReportDescriptionDTO implements ReportDescriptionData {

	private static final long serialVersionUID = 3793554325796093693L;
	public static final String DESCRIPTION_ATTR = "description";
	public static final String OWNER_ATTR = "owner";
	public static final String PARAMETERS_ATTR = "parametersList";

	private Long id;
	private String name;
	private String description;
	private Date creationDate;
	private String reportUrl;
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
		this.parametersList = parametersList;
	}
	@Override
	public List<ReportParameterData> getParametersList() {
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
        ReportDescriptionDTO other = (ReportDescriptionDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }	
}
