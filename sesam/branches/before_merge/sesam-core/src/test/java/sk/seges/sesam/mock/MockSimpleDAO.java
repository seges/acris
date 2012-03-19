/**
 * 
 */
package sk.seges.sesam.mock;

import java.util.LinkedList;
import java.util.List;

import sk.seges.sesam.dao.IFinderDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author eldzi
 */
public class MockSimpleDAO implements IFinderDAO<Integer> {
	private Storage storage;
	
	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Override
	public PagedResult<List<Integer>> findAll(Page requestedPage) {
		List<Integer> trans = storage.getIntegers();
		
		List<Integer> result = new LinkedList<Integer>();
		
//		int page = requestedPage.getStartIndex() / requestedPage.getPageSize();
//		int start = page * requestedPage.getPageSize();
		int start = requestedPage.getStartIndex();
		int supposedEnd = start+requestedPage.getPageSize();
		int end = supposedEnd <= trans.size() ? supposedEnd : trans.size();
//		System.out.println("requesting page = " + requestedPage + ", suppo = "+supposedEnd);
		
		for (int i = start; i < end; i++)
			result.add(trans.get(i));
		
//		Page resultPage = new Page(start, requestedPage.getPageSize());
//		System.out.println("result page = " + resultPage);
		return new PagedResult<List<Integer>>(requestedPage, result, trans.size());
	}

	@Override
	public Integer findEntity(Integer entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findUnique(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

}
