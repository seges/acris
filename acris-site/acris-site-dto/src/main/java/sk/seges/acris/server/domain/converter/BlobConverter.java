package sk.seges.acris.server.domain.converter;

import java.sql.Blob;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.SerializableBlobProxy;

import sk.seges.sesam.shared.model.converter.BasicConverter;

public class BlobConverter extends BasicConverter<String, Blob> {

	private EntityManager entityManager;

	public BlobConverter(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public String toDto(Blob domain) {
		if (domain != null) {
			try {
				long length = 0;

				length = domain.length();

				if (length > 0) {
					return new String(domain.getBytes(1L, (int) length), "UTF-8");
				} else {
					return "";
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to convert blob to string", e);
			}
		}

		return null;
	}

	@Override
	public Blob fromDto(String dto) {
		if (dto != null) {
			return SerializableBlobProxy.generateProxy(Hibernate.getLobCreator((Session) entityManager.getDelegate()).createBlob(dto.getBytes()));
		}

		return null;
	}

	@Override
	public String convertToDto(String result, Blob domain) {
		return toDto(domain);
	}

	@Override
	public Blob convertFromDto(Blob result, String domain) {
		return fromDto(domain);
	}

	@Override
	public boolean equals(Object domain, Object dto) {
		return false;
	}
}