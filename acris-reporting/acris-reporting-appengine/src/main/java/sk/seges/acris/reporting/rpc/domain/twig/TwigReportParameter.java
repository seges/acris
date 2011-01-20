package sk.seges.acris.reporting.rpc.domain.twig;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

public class TwigReportParameter implements ReportParameterData {

	private static final long serialVersionUID = 2892837119494217776L;

	private	Long id;

	private Integer orderNumber;

	private String name;

	private String displayedName;

	private String description;

	private String className;

	private Boolean hidden;

	private ReportParameterData parent;
	
	public TwigReportParameter() {
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getDisplayedName() {
		return displayedName;
	}

	@Override
	public Boolean getHidden() {
		return hidden;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getOrderNumber() {
		return orderNumber;
	}

	@Override
	public ReportParameterData getParent() {
		return parent;
	}

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}

	@Override
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public void setParent(ReportParameterData parent) {
		this.parent = parent;
	}

	@Override
	public void setId(Long t) {
		this.id = t;
	}

	@Override
	public Long getId() {
		return id;
	}
}
