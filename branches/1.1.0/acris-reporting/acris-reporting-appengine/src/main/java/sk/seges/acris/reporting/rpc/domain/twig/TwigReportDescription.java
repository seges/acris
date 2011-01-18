package sk.seges.acris.reporting.rpc.domain.twig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

import com.google.appengine.api.datastore.Text;
import com.vercer.engine.persist.annotation.Activate;
import com.vercer.engine.persist.annotation.Child;
import com.vercer.engine.persist.annotation.Embed;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Type;

public class TwigReportDescription implements ReportDescriptionData {

	private static final long serialVersionUID = -4140036031394317836L;

	private @Key
	Long id;
	private String name;
	private @Type(Text.class)
	String description;
	private Date creationDate;
	private String reportUrl;

	private @Embed //@Child @Activate(0)
	List<TwigReportParameter> parametersList = null;

	public TwigReportDescription() {
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ReportParameterData> getParametersList() {
		if (parametersList == null) {
			return null;
		}
		List<ReportParameterData> result = new ArrayList<ReportParameterData>();
		for (ReportParameterData param : parametersList) {
			result.add(param);
		}
		return result;
	}

	@Override
	public String getReportUrl() {
		return reportUrl;
	}

	@Override
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setParametersList(List<ReportParameterData> parametersList) {
		if (parametersList == null) {
			this.parametersList = null;
			return;
		}
		this.parametersList = new ArrayList<TwigReportParameter>();
		for (ReportParameterData param : parametersList) {
			this.parametersList.add((TwigReportParameter) param);
		}
//		this.parametersList = parametersList;
	}

	@Override
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

}
