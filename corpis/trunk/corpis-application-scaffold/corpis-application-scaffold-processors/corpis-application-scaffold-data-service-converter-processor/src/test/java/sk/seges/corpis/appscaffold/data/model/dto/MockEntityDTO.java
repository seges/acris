package sk.seges.corpis.appscaffold.data.model.dto;

import java.io.Serializable;

import javax.annotation.Generated;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@SuppressWarnings("serial")
@TransferObjectMapping(dtoClass = MockEntityDTO.class,
		domainClassName = "sk.seges.corpis.pap.model.entity.MockEntity", 
		configurationClassName = "sk.seges.corpis.pap.model.configuration.MockEntityDTOConfiguration", 
		converterClassName = "sk.seges.corpis.pap.model.converter.MockEntityDTOConverter")
@Generated(value = "sk.seges.corpis.pap.model.hibernate.HibernateTransferObjectProcessor")
public class MockEntityDTO implements Serializable {

	private String contentDetached;

	private Long id;

	private String name;

	public MockEntityDTO() {
	}
	public MockEntityDTO(String contentDetached, Long id, String name) {
		this.contentDetached = contentDetached;
		this.id = id;
		this.name = name;
	}

	public String getContentDetached() {
		return contentDetached;
	}

	public void setContentDetached(String contentDetached) {
		this.contentDetached = contentDetached;
	}

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

	private boolean processingEquals = false;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockEntityDTO other = (MockEntityDTO) obj;
		if (contentDetached == null) {
			if (other.contentDetached != null)
				return false;
		} else { 
			if (!processingEquals) {
				processingEquals = true;
				if (!contentDetached.equals(other.contentDetached)) {
					processingEquals = false;
					return false;
				} else {
					processingEquals = false;
				}
			}
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else { 
			if (!processingEquals) {
				processingEquals = true;
				if (!name.equals(other.name)) {
					processingEquals = false;
					return false;
				} else {
					processingEquals = false;
				}
			}
		}
		return true;
	}

	private boolean processingHashCode = false;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (!processingHashCode) {
			processingHashCode = true;
			result = prime * result + ((contentDetached == null) ? 0 : contentDetached.hashCode());
			processingHashCode = false;
		}
		if (!processingHashCode) {
			processingHashCode = true;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			processingHashCode = false;
		}
		return result;
	}
}