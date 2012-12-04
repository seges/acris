package sk.seges.sesam.pap.model;

import java.io.Serializable;

import javax.annotation.Generated;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@SuppressWarnings("serial")
@TransferObjectMapping(dtoClass = MockEntityDto.class,
		domainClassName = "sk.seges.sesam.pap.model.DomainObject", 
		configurationClassName = "sk.seges.sesam.pap.configuration.MockEntityDtoConfiguration", 
		generateConverter = false, generateDto = false, 
		converterClassName = "sk.seges.sesam.pap.model.MockEntityDtoConverter")
@Generated(value = "sk.seges.sesam.pap.model.TransferObjectProcessor")
public class MockEntityDto implements Serializable {

	private int field1;

	private String field2;

	private String referenceField1;

	public MockEntityDto() {
	}
	public MockEntityDto(int field1, String field2, String referenceField1) {
		this.field1 = field1;
		this.field2 = field2;
		this.referenceField1 = referenceField1;
	}

	public int getField1() {
		return field1;
	}

	public void setField1(int field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getReferenceField1() {
		return referenceField1;
	}

	public void setReferenceField1(String referenceField1) {
		this.referenceField1 = referenceField1;
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
		MockEntityDto other = (MockEntityDto) obj;
		if (field1 != other.field1)
			return false;
		if (field2 == null) {
			if (other.field2 != null)
				return false;
		} else { 
			if (!processingEquals) {
				processingEquals = true;
				if (!field2.equals(other.field2)) {
					processingEquals = false;
					return false;
				} else {
					processingEquals = false;
				}
			}
		}
		if (referenceField1 == null) {
			if (other.referenceField1 != null)
				return false;
		} else { 
			if (!processingEquals) {
				processingEquals = true;
				if (!referenceField1.equals(other.referenceField1)) {
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
		result = prime * result + field1;
		if (!processingHashCode) {
			processingHashCode = true;
			result = prime * result + ((field2 == null) ? 0 : field2.hashCode());
			processingHashCode = false;
		}
		if (!processingHashCode) {
			processingHashCode = true;
			result = prime * result + ((referenceField1 == null) ? 0 : referenceField1.hashCode());
			processingHashCode = false;
		}
		return result;
	}
}