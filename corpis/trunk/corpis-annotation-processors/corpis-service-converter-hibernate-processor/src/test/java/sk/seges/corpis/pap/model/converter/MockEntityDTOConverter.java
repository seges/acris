package sk.seges.corpis.pap.model.converter;

import java.io.Serializable;
import java.sql.Blob;

import javax.annotation.Generated;
import javax.persistence.EntityManager;

import sk.seges.corpis.pap.model.converter.MockBlobConverter;
import sk.seges.corpis.pap.model.dto.MockEntityDTO;
import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;

@Generated(value = "sk.seges.corpis.pap.model.hibernate.HibernateTransferObjectConverterProcessor")
public class MockEntityDTOConverter extends BasicCachedConverter<MockEntityDTO, MockEntity> {

	public MockEntityDTOConverter(MapConvertedInstanceCache cache, EntityManager entityManager, TransactionPropagationModel[] transactionPropagations) {
		super(cache);
		this.cache = cache;
		this.entityManager = entityManager;
		this.transactionPropagations = transactionPropagations;
	}

	public MockEntityDTOConverter(EntityManager entityManager, TransactionPropagationModel[] transactionPropagations) {
		this(new sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache(), entityManager, transactionPropagations);
	}

	private MapConvertedInstanceCache cache;

	private EntityManager entityManager;

	private TransactionPropagationModel[] transactionPropagations;

	public boolean equals(MockEntity _domain,MockEntityDTO _dto) {
		if (_domain.getBlob() == null) {
			if (_dto.getContentDetached() != null)
				return false;
		} else if (!_domain.getBlob().equals(_dto.getContentDetached()))
			return false;
		if (_domain.getId() == null) {
			if (_dto.getId() != null)
				return false;
		} else if (!_domain.getId().equals(_dto.getId()))
			return false;
		if (_domain.getName() == null) {
			if (_dto.getName() != null)
				return false;
		} else if (!_domain.getName().equals(_dto.getName()))
			return false;
		return true;
	}

	protected boolean isInitialized(Object instance) {
		return org.hibernate.Hibernate.isInitialized(instance);
	}

	public MockEntityDTO createDtoInstance(Serializable id) {
		MockEntityDTO _result = new MockEntityDTO();
		return _result;
	}

	public MockEntityDTO toDto(MockEntity _domain) {

		if (_domain  == null) {
			return null;
		}

		MockEntityDTO _result = getDtoInstance(_domain, _domain.getId());
		if (_result != null) {
			return _result;
		}

		Long _id = _domain.getId();

		_result = createDtoInstance(_id);
		return convertToDto(_result, _domain);
	}

	public MockEntityDTO convertToDto(MockEntityDTO _result, MockEntity _domain) {

		if (_domain  == null) {
			return null;
		}

		MockEntityDTO dtoFromCache = getDtoFromCache(_domain, _domain.getId());

		if (dtoFromCache != null) {
			return dtoFromCache;
		}

		putDtoIntoCache(_domain, _result,_result.getId());

		if (isInitialized(_domain.getBlob())) {
			MockBlobConverter converterContentDetached = getMockBlobConverter();
			_result.setContentDetached(converterContentDetached.toDto((Blob)_domain.getBlob()));
		};
		if (isInitialized(_domain.getId())) {
			_result.setId(_domain.getId());
		};
		if (isInitialized(_domain.getName())) {
			_result.setName(_domain.getName());
		};
		return _result;
	}

	public MockEntity createDomainInstance(Serializable id) {
		if (id != null) {
			MockEntity _result = (MockEntity)entityManager.find(MockEntity.class, id);
			if (_result != null) {
				return _result;
			}
		}

		 return new MockEntity();
	}

	public MockEntity fromDto(MockEntityDTO _dto) {

		if (_dto == null) {
			return null;
		}

		MockEntity _result = getDomainInstance(_dto, _dto.getId());
		if (_result != null) {
			return _result;
		}
		Long _id = _dto.getId();

		_result = createDomainInstance(_id);

		return convertFromDto(_result, _dto);
	}

	public MockEntity convertFromDto(MockEntity _result, MockEntityDTO _dto) {

		if (_dto  == null) {
			return null;
		}

		MockEntity domainFromCache = getDomainFromCache(_dto, _dto.getId());

		if (domainFromCache != null) {
			return domainFromCache;
		}

		putDomainIntoCache(_dto, _result,_result.getId());

		if (ConverterUtils.convertArg(transactionPropagations, "blob")) {
			if (_result.getBlob() != null) {
				if (_dto.getContentDetached() != null) {
					MockBlobConverter converterContentDetached = getMockBlobConverter();
					_result.setBlob((Blob)converterContentDetached.convertFromDto(_result.getBlob(),_dto.getContentDetached()));
				} else {
					_result.setBlob(null);
				}
			} else {
				MockBlobConverter converterContentDetached = getMockBlobConverter();
				_result.setBlob((Blob)converterContentDetached.fromDto(_dto.getContentDetached()));
			}
		}
		_result.setId(_dto.getId());
		_result.setName(_dto.getName());
		return _result;
	}

	private MockBlobConverter getMockBlobConverter() {
		return new MockBlobConverter();
	}

}