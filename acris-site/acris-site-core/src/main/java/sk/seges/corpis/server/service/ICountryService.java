package sk.seges.corpis.server.service;

import sk.seges.corpis.server.domain.server.model.data.CountryData;

public interface ICountryService {

	CountryData findByCountry(String country);
	
	CountryData findDefaultCountry();

}