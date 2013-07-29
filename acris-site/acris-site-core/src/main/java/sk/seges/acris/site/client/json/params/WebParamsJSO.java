package sk.seges.acris.site.client.json.params;

import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;

public class WebParamsJSO extends BaseJSONModel implements WebParams {

	private static final long serialVersionUID = -728318142595408871L;

	public WebParamsJSO(JSONModel fromJson) {
		super(fromJson);
	}

	@Override
	public Boolean isPublishOnSaveEnabled() {
		return data.getBoolean(PUBLISH_ON_SAVE_ENABLED);
	}

	@Override
	public void setPublishOnSaveEnabled(boolean publishOnSaveEnabled) {
		data.set(PUBLISH_ON_SAVE_ENABLED, publishOnSaveEnabled);
	}

	@Override
	public Boolean isOfflineAutodetectMode() {
		return data.getBoolean(OFFLINE_AUTODETECT_MODE);
	}

	@Override
	public void setOfflineAutodetectMode(boolean mode) {
		data.set(OFFLINE_AUTODETECT_MODE, mode);
	}

	@Override
	public Boolean isProductCategorySingleSelect() {
		return data.getBoolean(PRODUCT_CATEGORY_SINGLE_SELECT);
	}

	@Override
	public void setProductCategorySingleSelect(boolean productCategorySingleSelect) {
		data.set(PRODUCT_CATEGORY_SINGLE_SELECT, productCategorySingleSelect);
	}

	@Override
	public String[] getOfflinePostProcessorInactive() {
		return data.getStringArray(OFFLINE_POST_PROCESSOR_INACTIVE);
	}

	@Override
	public void setOfflinePostProcessorInactive(String[] processors) {
		data.set(OFFLINE_POST_PROCESSOR_INACTIVE, processors);
	}

	@Override
	public String[] getOfflineIndexProcessorInactive() {
		return data.getStringArray(OFFLINE_INDEX_PROCESSOR_INACTIVE);
	}

	@Override
	public void setOfflineIndexProcessorInactive(String[] processors) {
		data.set(OFFLINE_INDEX_PROCESSOR_INACTIVE, processors);
	}

	@Override
	public Boolean isFiltersEnabled() {
		return data.getBoolean(PRODUCT_LIST_FILTERS_ENABLED);
	}

	@Override
	public void setFiltersEnabled(boolean filtersEnabled) {
		data.set(PRODUCT_LIST_FILTERS_ENABLED, filtersEnabled);
	}

	@Override
	public Boolean isProductListSortEnabled() {
		return data.getBoolean(PRODUCT_LIST_SORT_ENABLED);
	}

	@Override
	public void setProductListSortEnabled(boolean productListSortEnabled) {
		data.set(PRODUCT_LIST_SORT_ENABLED, productListSortEnabled);
	}

	@Override
	public String getSearchMode() {
		return data.get(SEARCH_MODE);
	}

	@Override
	public void setSearchMode(String mode) {
		data.set(SEARCH_MODE, mode);
	}

	@Override
	public Boolean isSearchLocalePrefix() {
		return data.getBoolean(SEARCH_LOCALE_PREFIX);
	}

	@Override
	public void setSearchLocalePrefix(boolean prefix) {
		data.set(SEARCH_LOCALE_PREFIX, prefix);
	}

	@Override
	public String getBluewaveUrl() {
		return data.get(BLUEWAVE_URL);
	}

	@Override
	public void setBluewaveUrl(String bluewaveUrl) {
		data.set(BLUEWAVE_URL, bluewaveUrl);
	}

	@Override
	public String getBluewaveUsername() {
		return data.get(BLUEWAVE_USERNAME);
	}

	@Override
	public void setBluewaveUsername(String bluewaveUsername) {
		data.set(BLUEWAVE_USERNAME, bluewaveUsername);
	}

	@Override
	public String getBluewavePassword() {
		return data.get(BLUEWAVE_PASSWORD);
	}

	@Override
	public void setBluewavePassword(String bluewavePassword) {
		data.set(BLUEWAVE_PASSWORD, bluewavePassword);
	}

	@Override
	public String[] getBreadcrumbItemsList() {
		return data.getStringArray(BREADCRUMB_ITEMS_LIST);
	}

	@Override
	public void setBreadcrumbItemsList(String[] breadcrumbItemsList) {
		data.set(BREADCRUMB_ITEMS_LIST, breadcrumbItemsList);
	}

	@Override
	public Boolean isProductsWithMicrositeEnabled() {
		return data.getBoolean(PRODUCTS_WITH_MICROSITE_ENABLED);
	}

	@Override
	public void setProductsWithMicrositeEnabled(boolean productsWithMicrositeEnabled) {
		data.set(PRODUCTS_WITH_MICROSITE_ENABLED, productsWithMicrositeEnabled);
		
	}

	@Override
	public Boolean isProductsWithContentEnabled() {
		return data.getBoolean(PRODUCTS_WITH_CONTENT_ENABLED);
	}

	@Override
	public void setProductsWithContentEnabled(boolean productsWithContentEnabled) {
		data.set(PRODUCTS_WITH_CONTENT_ENABLED, productsWithContentEnabled);
		
	}
	
	@Override
	public Boolean isBackgroundManagementEnabled() {
		return data.getBoolean(BACKGROUND_MANAGEMENT_ENABLED);
	}

	@Override
	public void setBackgroundManagementEnabled(boolean backgroundManagementEnabled) {
		data.set(BACKGROUND_MANAGEMENT_ENABLED, backgroundManagementEnabled);
	}

	@Override
	public boolean isIncludeProductCategoryInSearch() {
		return data.getBoolean(INCLUDE_PRODUCT_CATEGORY_IN_SEARCH);
	}

	@Override
	public void setIncludeProductCategoryInSearch(boolean includeProductCategoryInSearch) {
		data.set(INCLUDE_PRODUCT_CATEGORY_IN_SEARCH, includeProductCategoryInSearch);		
	}
	
	@Override
	public String getScrollMode() {		
		return data.get(SCROLL_MODE);
	}

	@Override
	public void setScrollMode(String scrollMode) {
		data.set(SCROLL_MODE, scrollMode);		
	}

	@Override
	public Boolean isMasterCategoryRequired() {
		return data.getBoolean(MASTER_CATEGORY_REQUIRED);
	}

	@Override
	public void setMasterCategoryRequired(boolean masterCategoryRequired) {
		data.set(MASTER_CATEGORY_REQUIRED, masterCategoryRequired);
	}
}
