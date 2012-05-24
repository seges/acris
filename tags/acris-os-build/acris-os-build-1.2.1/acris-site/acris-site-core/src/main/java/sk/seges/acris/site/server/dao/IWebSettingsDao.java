package sk.seges.acris.site.server.dao;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.sesam.dao.ICrudDAO;


public interface IWebSettingsDao<T extends WebSettingsData> extends ICrudDAO<WebSettingsData> {

	WebSettingsData createDefaultEntity();

}