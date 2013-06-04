package sk.seges.acris.site.client.json.params;

import sk.seges.acris.site.shared.json.ESearchMode;

public class WebParamsBean implements WebParams {

	private static final long serialVersionUID = -728318142595408871L;

	private boolean offlineAutodetectMode = false;
	private boolean publishOnSaveEnabled = false;
	private boolean productCategorySingleSelect = false;
	private boolean filtersEnabled = false;
	private boolean productListSortEnabled = false;
	private boolean backgroundManagementEnabled = false;
	
	private String searchMode = ESearchMode.EQ.name();
	private boolean searchLocalePrefix = false;

	private String[] offlinePostProcessorInactive;
	private String[] offlineIndexProcessorInactive;
	
	private String bluewaveUrl;
	private String bluewaveUsername;
	private String bluewavePassword;

	private String[] breadcrumbItemsList;
	
	private boolean productsWithMicrositeEnabled = false;
	private boolean productsWithContentEnabled = true;
	
	private boolean includeProductCategoryInSearch = false;
	
	@Override
	public Boolean isPublishOnSaveEnabled() {
		return publishOnSaveEnabled;
	}

	@Override
	public void setPublishOnSaveEnabled(boolean publishOnSaveEnabled) {
		this.publishOnSaveEnabled = publishOnSaveEnabled;
	}

	@Override
	public Boolean isOfflineAutodetectMode() {
		return offlineAutodetectMode;
	}

	@Override
	public void setOfflineAutodetectMode(boolean mode) {
		this.offlineAutodetectMode = mode;
	}

	@Override
	public Boolean isProductCategorySingleSelect() {
		return productCategorySingleSelect;
	}

	@Override
	public void setProductCategorySingleSelect(boolean productCategorySingleSelect) {
		this.productCategorySingleSelect = productCategorySingleSelect;
	}

	@Override
	public String[] getOfflinePostProcessorInactive() {
		return this.offlinePostProcessorInactive;
	}

	@Override
	public void setOfflinePostProcessorInactive(String[] processors) {
		this.offlinePostProcessorInactive = processors;
	}

	@Override
	public String[] getOfflineIndexProcessorInactive() {
		return this.offlineIndexProcessorInactive;
	}

	@Override
	public void setOfflineIndexProcessorInactive(String[] processors) {
		this.offlineIndexProcessorInactive = processors;
	}

	@Override
	public Boolean isFiltersEnabled() {
		return this.filtersEnabled;
	}

	@Override
	public void setFiltersEnabled(boolean filtersEnabled) {
		this.filtersEnabled = filtersEnabled;
	}

	@Override
	public Boolean isProductListSortEnabled() {
		return productListSortEnabled;
	}

	@Override
	public void setProductListSortEnabled(boolean productListSortEnabled) {
		this.productListSortEnabled = productListSortEnabled;
	}

	@Override
	public String getSearchMode() {
		return searchMode;
	}

	@Override
	public void setSearchMode(String mode) {
		this.searchMode = mode;
	}

	@Override
	public Boolean isSearchLocalePrefix() {
		return searchLocalePrefix;
	}

	@Override
	public void setSearchLocalePrefix(boolean prefix) {
		this.searchLocalePrefix = prefix;
	}

	@Override
	public String getBluewaveUrl() {
		return bluewaveUrl;
	}

	@Override
	public void setBluewaveUrl(String bluewaveUrl) {
		this.bluewaveUrl = bluewaveUrl;
	}

	@Override
	public String getBluewaveUsername() {
		return bluewaveUsername;
	}

	@Override
	public void setBluewaveUsername(String bluewaveUsername) {
		this.bluewaveUsername = bluewaveUsername;
	}

	@Override
	public String getBluewavePassword() {
		return bluewavePassword;
	}

	@Override
	public void setBluewavePassword(String bluewavePassword) {
		this.bluewavePassword = bluewavePassword;
	}

	@Override
	public String[] getBreadcrumbItemsList() {
		return breadcrumbItemsList;
	}

	@Override
	public void setBreadcrumbItemsList(String[] breadcrumbItemsList) {
		this.breadcrumbItemsList = breadcrumbItemsList;
	}

	@Override
	public Boolean isProductsWithMicrositeEnabled() {
		return productsWithMicrositeEnabled;
	}

	@Override
	public void setProductsWithMicrositeEnabled(boolean productsWithMicrositeEnabled) {
		this.productsWithMicrositeEnabled = productsWithMicrositeEnabled;
	}

	@Override
	public Boolean isProductsWithContentEnabled() {
		return productsWithContentEnabled;
	}

	@Override
	public void setProductsWithContentEnabled(boolean productsWithContentEnabled) {
		this.productsWithContentEnabled = productsWithContentEnabled;
	}
	
	@Override
	public Boolean isBackgroundManagementEnabled() {
		return backgroundManagementEnabled;
	}

	@Override
	public void setBackgroundManagementEnabled(boolean backgroundManagementEnabled) {
		this.backgroundManagementEnabled = backgroundManagementEnabled;
	}

	@Override
	public boolean isIncludeProductCategoryInSearch() {
		return includeProductCategoryInSearch;
	}

	@Override
	public void setIncludeProductCategoryInSearch(boolean includeProductCategoryInSearch) {
		this.includeProductCategoryInSearch = includeProductCategoryInSearch;
	}
}
