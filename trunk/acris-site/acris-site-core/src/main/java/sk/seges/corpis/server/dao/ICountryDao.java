package sk.seges.corpis.server.dao;

import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.sesam.dao.ICrudDAO;

public interface ICountryDao<T extends CountryData> extends ICrudDAO<CountryData> {

	T createDefaultEntity();

}