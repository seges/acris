package sk.seges.corpis.pap.service.hibernate.model;

import java.io.Serializable;

import javax.persistence.EntityManager;

import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;

public class MockEntityConverter extends BasicCachedConverter<MockEntityDTO, MockEntity>{

	public MockEntityConverter(EntityManager entityManager, MapConvertedInstanceCache cache) {
		super(cache);
	}
	
	public MockEntityConverter(EntityManager entityManager) {
		super(new MapConvertedInstanceCache());
	}

	@Override
	public MockEntityDTO convertToDto(MockEntityDTO result, MockEntity domain) {
		return new MockEntityDTO();
	}

	@Override
	public MockEntityDTO toDto(MockEntity domain) {
		return new MockEntityDTO();
	}

	@Override
	public MockEntity convertFromDto(MockEntity result, MockEntityDTO dto) {
		return new MockEntity();
	}

	@Override
	public MockEntity fromDto(MockEntityDTO dto) {
		return new MockEntity();
	}

	@Override
	public boolean equals(MockEntity domain, MockEntityDTO dto) {
		return false;
	}

	@Override
	protected MockEntity createDomainInstance(Serializable id) {
		return new MockEntity();
	}

	@Override
	protected MockEntityDTO createDtoInstance(Serializable id) {
		return new MockEntityDTO();
	}
}