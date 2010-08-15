package sk.seges.acris.binding.client.samples.loaders;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.binding.client.samples.mocks.Company;
import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public class CompanyDataLoader implements IAsyncDataLoader<List<Company>> {

	@Override
	public void load(Page page, ICallback<PagedResult<List<Company>>> callback) {
		PagedResult<List<Company>> pagedResult = new PagedResult<List<Company>>();
		
		List<Company> companies = new ArrayList<Company>();
		
		Company zettaflops = new Company();
		zettaflops.setName("Zettaflops s.r.o");
		companies.add(zettaflops);

		Company seges = new Company();
		seges.setName("Seges s.r.o.");
		companies.add(seges);

		pagedResult.setResult(companies);
		pagedResult.setPage(Page.ALL_RESULTS_PAGE);

		callback.onSuccess(pagedResult);
	}
}
