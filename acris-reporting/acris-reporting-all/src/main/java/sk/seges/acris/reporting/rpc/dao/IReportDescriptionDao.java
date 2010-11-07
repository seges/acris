package sk.seges.acris.reporting.rpc.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public interface IReportDescriptionDao extends ICrudDAO<ReportDescription> {
	PagedResult<List<ReportDescription>> findPagedResultByCriteria(DetachedCriteria criteria, Page page);
	ReportDescription findById(Long id);

}