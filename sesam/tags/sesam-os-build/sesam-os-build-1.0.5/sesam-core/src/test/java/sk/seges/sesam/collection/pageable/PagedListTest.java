/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sk.seges.sesam.dao.IDataLoader;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.mock.MockSimpleDAO;
import sk.seges.sesam.mock.Storage;


/**
 * @author eldzi
 */
public class PagedListTest {
	private MockSimpleDAO dao;
	private PagedList<Integer> adapter;
	
	@Before
	public void setUp() {
		dao = new MockSimpleDAO();
		
		adapter = new PagedList<Integer>();
		adapter.setDataLoader(new IDataLoader<List<Integer>>() {
			@Override
			public PagedResult<List<Integer>> load(Page page) {
				return dao.findAll(page);
			}
		});
		adapter.setDefaultPageSize(10);
	}
	
	@After
	public void tearDown() {
		dao = null;
	}
	
	@Test
	public void testGetElementWithinPage() throws Exception {
		dao.setStorage(new Storage(180));
		Assert.assertEquals(Integer.valueOf(4), adapter.get(4));
	}
	
	@Test
	public void testGetElementOutOfButWithinBoundPage() throws Exception {
		dao.setStorage(new Storage(180));
		Assert.assertEquals(Integer.valueOf(14), adapter.get(14));
	}
	
	@Test
	public void testGetNotExistentOutOfBoundElement() throws Exception {
		dao.setStorage(new Storage(180));
		try {
			adapter.get(250);
			Assert.fail("Should be out of bound");
		} catch(IndexOutOfBoundsException e) {
		}
	}
	
	@Test
	public void testGetCorrectNumberOfRecords() throws Exception {	
		dao.setStorage(new Storage(180));
		int expectedAllResults = dao.findAll(new Page(0, 2)).getTotalResultCount();
		Assert.assertEquals(expectedAllResults, adapter.size());
	}
	
	@Test
	public void testGetRecordOnTheCacheBoundAndDontFetch() throws Exception {
		dao.setStorage(new Storage(180));
		Assert.assertEquals(Integer.valueOf(4), adapter.get(4));
		Assert.assertFalse(adapter.isOutOfCachedPagedResult(9));
		Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(adapter.getPagedResult().getPage().getStartIndex()));
		Assert.assertEquals(Integer.valueOf(10), Integer.valueOf(adapter.getPagedResult().getResult().size()));
		Assert.assertEquals(Integer.valueOf(9), Integer.valueOf(adapter.getEndIndex()));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetElementFromSizeOne() throws Exception {
		dao.setStorage(new Storage(1));
		Assert.assertEquals(Integer.valueOf(0), adapter.get(0));
		adapter.get(1);
	}
}
