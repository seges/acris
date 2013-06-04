package sk.seges.acris.reporting.server.dao.api;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;

/**
 * dao interface for ReportDescriptionData
 * @author marta
 *
 * @param <T> extends {@link ReportDescriptionData}
 */
public interface IReportDescriptionDao<T extends ReportDescriptionData> extends ICrudDAO<T>, IEntityInstancer<T> {
}