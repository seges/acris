package sk.seges.corpis.appscaffold.data.model.entity;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Id;

public class MockEntity {

	private Blob blob;

	private String name;
	
	private Long id;

	@Basic(fetch = FetchType.LAZY)
	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}