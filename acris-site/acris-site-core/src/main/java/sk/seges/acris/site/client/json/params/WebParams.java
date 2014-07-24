package sk.seges.acris.site.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface WebParams extends ContentParameters {

	public static final String OFFLINE_POST_PROCESSOR_INACTIVE = "offlinePostProcessorInactive";
	public static final String OFFLINE_INDEX_PROCESSOR_INACTIVE = "offlineIndexProcessorInactive";
	public static final String OFFLINE_AUTODETECT_MODE = "offlineAutodetectMode";
	public static final String PUBLISH_ON_SAVE_ENABLED = "publishOnSaveEnabled";
	public static final String PRODUCT_CATEGORY_SINGLE_SELECT = "productCategorySingleSelect";
	public static final String PRODUCT_LIST_FILTERS_ENABLED = "productListFilterEnabled";
	public static final String PRODUCT_LIST_SORT_ENABLED = "productListSortEnabled";
	public static final String SEARCH_MODE = "searchMode";
	public static final String SEARCH_LOCALE_PREFIX = "searchLocalePrefix";
	public static final String BLUEWAVE_URL = "bluewaveUrl";
	public static final String BLUEWAVE_USERNAME = "bluewaveUsername";
	public static final String BLUEWAVE_PASSWORD = "bluewavePassword";
	public static final String BREADCRUMB_ITEMS_LIST = "breadcrumbItemsList";
	public static final String PRODUCTS_WITH_MICROSITE_ENABLED = "productsWithMicrositeEnabled";
	public static final String MASTER_CATEGORY_REQUIRED = "masterCategoryRequired";
	public static final String PRODUCTS_WITH_CONTENT_ENABLED = "productsWithContentsEnabled";
	public static final String BACKGROUND_MANAGEMENT_ENABLED = "backgroundManagementEnabled";
	public static final String INCLUDE_PRODUCT_CATEGORY_IN_SEARCH = "includeProductCategoryInSearch";
	public static final String SCROLL_MODE = "scrollMode";
	public static final String IMPORT_URL = "importURL";
	public static final String IMPORT_IMAGE_URL = "importImageUrl";
	public static final String REDIRECT_LOGIN_URL = "redirectLoginUrl";
	public static final String COUNT_OF_DAYS_TO_PAY_INVOICE = "countOfDaysToPayInvoice";
	public static final String IMAGE_SIZES = "imageSizes";	
	public static final String NEW_PRODUCT_SETTINGS_ENABLED = "newProductSettingsEnabled"; 
	
	String[] getOfflinePostProcessorInactive();

	void setOfflinePostProcessorInactive(String[] processors);

	String[] getOfflineIndexProcessorInactive();

	void setOfflineIndexProcessorInactive(String[] processors);

	Boolean isOfflineAutodetectMode();

	void setOfflineAutodetectMode(boolean mode);

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

	Boolean isProductCategorySingleSelect();

	void setProductCategorySingleSelect(boolean productCategorySingleSelect);

	Boolean isFiltersEnabled();

	void setFiltersEnabled(boolean filtersEnabled);

	Boolean isProductListSortEnabled();

	void setProductListSortEnabled(boolean sortEnabled);

	String getSearchMode();

	void setSearchMode(String mode);

	Boolean isSearchLocalePrefix();

	void setSearchLocalePrefix(boolean prefix);

	String getBluewaveUrl();

	void setBluewaveUrl(String bluewaveUrl);

	String getBluewaveUsername();

	void setBluewaveUsername(String bluewaveUsername);

	String getBluewavePassword();

	void setBluewavePassword(String bluewavePassword);
	
	String[] getBreadcrumbItemsList();
	
	void setBreadcrumbItemsList(String[] breadcrumbItemsList);
	
	Boolean isProductsWithMicrositeEnabled();
	
	void setProductsWithMicrositeEnabled(boolean productsWithMicrositeEnabled);
	
	Boolean isProductsWithContentEnabled();
	
	void setProductsWithContentEnabled(boolean productsWithContentEnabled);
	
	Boolean isMasterCategoryRequired();
	
	void setMasterCategoryRequired(boolean masterCategoryRequired);
	
	Boolean isBackgroundManagementEnabled();
	
	void setBackgroundManagementEnabled(boolean backgroundManagementEnabled);
	
	boolean isIncludeProductCategoryInSearch();

	void setIncludeProductCategoryInSearch(boolean includeProductCategoryInSearch);
	
	String getScrollMode();
	
	void setScrollMode(String scrollMode);

	String getImportURL();
	
	void setImportURL(String importURL);
	
	String getImportImageURL();
	
	void setImportImageURL(String importImageURL);
	
	String getRedirectLoginURL();
	
	void setRedirectLoginURL(String redirectLoginURL);

	Integer getCountOfDaysToPayInvoice();

	void setCountOfDaysToPayInvoice(Integer countOfDaysToPayInvoice);
	
	ImageSize[] getImageSizes();
	
	void setImageSizes(ImageSize[] imageSizes);
	
	Boolean isNewProductSettingsEnabled();
	
	void setNewProductSettingsEnabled(Boolean newProductSettingsEnabled);
}
