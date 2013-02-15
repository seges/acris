package sk.seges.sesam.pap.model;

import java.io.Serializable;

import javax.annotation.Generated;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

@SuppressWarnings("serial")
@TransferObjectMapping(dtoClass = MockEntityDto.class,
		domainClassName = "sk.seges.sesam.pap.model.DomainObject", 
		configurationClassName = "sk.seges.sesam.pap.configuration.MockEntityDtoConfiguration", 
		generateConverter = false, generateDto = false, 
		converterClassName = "sk.seges.sesam.pap.model.MockEntityDtoConverter")
@Generated(value = "sk.seges.sesam.pap.model.TransferObjectConverterProcessor")
public class MockEntityDtoConverter extends BasicCachedConverter<MockEntityDto, DomainObject> {

	private ConverterProvider converterProvider;

	private ConvertedInstanceCache cache;

	public MockEntityDtoConverter(ConverterProvider converterProvider, ConvertedInstanceCache cache) {
		super(cache);
		this.converterProvider = converterProvider;
		this.cache = cache;
	}

	public boolean equals(DomainObject _domain, MockEntityDto _dto) {
		if (_domain.getField1() != _dto.getField1())
			return false;
		if (_domain.getField2() == null) {
			if (_dto.getField2() != null)
				return false;
		} else if (!_domain.getField2().equals(_dto.getField2()))
			return false;
		return true;
	}

	public MockEntityDto createDtoInstance(Serializable id) {
		MockEntityDto _result = new MockEntityDto();
		return _result;
	}

	public MockEntityDto toDto(DomainObject _domain) {

		if (_domain  == null) {
			return null;
		}

		MockEntityDto _result = createDtoInstance(null);
		return convertToDto(_result, _domain);
	}

	public MockEntityDto convertToDto(MockEntityDto _result, DomainObject _domain) {

		if (_domain  == null) {
			return null;
		}

		_result.setField1(_domain.getField1());
		_result.setField2(_domain.getField2());
		
		return _result;
	}

	public DomainObject createDomainInstance(Serializable id) {
		 return new DomainObject();
	}

	public DomainObject fromDto(MockEntityDto _dto) {

		if (_dto == null) {
			return null;
		}

		DomainObject _result = createDomainInstance(null);

		return convertFromDto(_result, _dto);
	}

	public DomainObject convertFromDto(DomainObject _result, MockEntityDto _dto) {

		if (_dto  == null) {
			return null;
		}

		_result.setField1(_dto.getField1());
		_result.setField2(_dto.getField2());
		return _result;
	}
}