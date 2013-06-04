package sk.seges.acris.reporting.rpc.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public interface IReportDescriptionDao extends ICrudDAO<ReportDescriptionData> {
	PagedResult<List<ReportDescriptionData>> findPagedResultByCriteria(DetachedCriteria criteria, Page page);
	ReportDescriptionData findById(Long id);
	List<ReportDescriptionData> findByName(String name);
}