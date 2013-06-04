package sk.seges.acris.reporting.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

/**
 * parameters for reports
 * 
 * @author marta
 * 
 */
@Entity
@Table(name = "reportparameter")
@SequenceGenerator(name = "report_param_id_seq", sequenceName = "report_param_id_seq", initialValue = 1)
public class JpaReportParameter implements ReportParameterData, Comparable<JpaReportParameter> {

	private static final long serialVersionUID = 6868805794198135658L;
	@Id
	@GeneratedValue(generator="report_param_id_seq")
	@Column(name = "parameter_id")
	private Long id;

	private Integer orderNumber;

	private String name;

	private String displayedName;

	private String description;

	private String className;

	private Boolean hidden;

	@ManyToOne(targetEntity = JpaReportParameter.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private ReportParameterData parent;

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
	public void setParent(ReportParameterData parent) {
		this.parent = parent;
	}

	@Override
	public ReportParameterData getParent() {
		return parent;
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
	public String getClassName() {
		return className;
	}

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public Boolean getHidden() {
		return hidden;
	}

	@Override
	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}

	@Override
	public String getDisplayedName() {
		return displayedName;
	}

	@Override
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public Integer getOrderNumber() {
		return orderNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
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
		JpaReportParameter other = (JpaReportParameter) obj;
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
	public int compareTo(JpaReportParameter o) {
		Integer other = o.getOrderNumber();
		if (other == null) {
			other = 0;
		}
		if (orderNumber == null) {
			orderNumber = 0;
		}
		if (orderNumber-other == 0) {
			return -1;
		}
		return orderNumber-other;
	}
}
