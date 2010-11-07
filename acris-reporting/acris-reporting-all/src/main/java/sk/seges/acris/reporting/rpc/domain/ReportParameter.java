package sk.seges.acris.reporting.rpc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import net.sf.gilead.pojo.java5.LightEntity;
import sk.seges.sesam.domain.IDomainObject;

/**
 * parameters for reports
 * 
 * @author marta
 * 
 */
@Entity
@SequenceGenerator(name = "report_param_id_seq", sequenceName = "report_param_id_seq", initialValue = 1)
public class ReportParameter extends LightEntity implements IDomainObject<Long>, Comparable<ReportParameter> {

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

	@ManyToOne
	@JoinColumn(name = "parent_id", nullable = true)
	private ReportParameter parent;

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

	public void setParent(ReportParameter parent) {
		this.parent = parent;
	}

	public ReportParameter getParent() {
		return parent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}

	public String getDisplayedName() {
		return displayedName;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportParameter other = (ReportParameter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(ReportParameter o) {
		Integer other = o.getOrderNumber();
		if (other == null)
			other = 0;
		if (orderNumber == null)
			orderNumber = 0;
		if (orderNumber-other == 0)
			return -1;
		return orderNumber-other;
	}
}
