package sk.seges.corpis.shared.service;

import sk.seges.corpis.shared.domain.api.CountryData;

public interface ICountryService {

	CountryData findByCountry(String country);
	
	CountryData findDefaultCountry();

}