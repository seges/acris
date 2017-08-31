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

	private OfflineMode offlineMode;
	
	private String bluewaveUrl;
	private String bluewaveUsername;
	private String bluewavePassword;

	private String[] breadcrumbItemsList;
	
	private boolean productsWithMicrositeEnabled = false;
	private boolean masterCategoryRequired = true;
	private boolean productsWithContentEnabled = true;
	
	private boolean includeProductCategoryInSearch = false;
	
	private String scrollMode;
	private String importURL;
	private String importImageUrl;
	private String redirectLoginURL;
	private Integer countOfDaysToPayInvoice;
	
	private ImageSize[] imageSizes;
	private Boolean newProductSettingsEnabled;
	
	private String invoicePrefix;
	private Integer invoiceCurrentNumber;
	
	private Boolean useGeneratedVariants;
	
	private Boolean enableOrderOfNotPresentItem = false;
	private Boolean fastOrder = false;
	private Boolean orderItemsPricesWithDiscounts = false;
	
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
	public void setOfflineMode(OfflineMode offlineMode) {
		this.offlineMode = offlineMode;
	}
	
	@Override
	public OfflineMode getOfflineMode() {
		return offlineMode;
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

	@Override
	public String getScrollMode() {		
		return scrollMode;
	}

	@Override
	public void setScrollMode(String scrollMode) {
		this.scrollMode = scrollMode;
	}

	@Override
	public Boolean isMasterCategoryRequired() {
		return masterCategoryRequired;
	}

	@Override
	public void setMasterCategoryRequired(boolean masterCategoryRequired) {
		this.masterCategoryRequired = masterCategoryRequired;
	}
	
	@Override
	public String getImportURL() {
		return importURL;
	}
	
	@Override
	public void setImportURL(String importURL) {
		this.importURL = importURL;
	}
	
	@Override
	public String getImportImageURL() {
		return importImageUrl;
	}
	
	@Override
	public void setImportImageURL(String importImageURL) {
		this.importImageUrl = importImageURL;
	}
	
	@Override
	public String getRedirectLoginURL() {
		return redirectLoginURL;
	}
	
	@Override
	public void setRedirectLoginURL(String redirectLoginURL) {
		this.redirectLoginURL = redirectLoginURL;
	}

	@Override
	public Integer getCountOfDaysToPayInvoice() {
		return countOfDaysToPayInvoice;
	}

	@Override
	public void setCountOfDaysToPayInvoice(Integer countOfDaysToPayInvoice) {
		this.countOfDaysToPayInvoice = countOfDaysToPayInvoice;
	}

	@Override
	public ImageSize[] getImageSizes() {		
		return imageSizes;
	}

	@Override
	public void setImageSizes(ImageSize[] imageSizes) {
		this.imageSizes = imageSizes;		
	}
	
	@Override
	public Boolean isNewProductSettingsEnabled() {
		return newProductSettingsEnabled;
	}
	
	@Override
	public void setNewProductSettingsEnabled(Boolean newProductSettingsEnabled) {
		this.newProductSettingsEnabled = newProductSettingsEnabled;
	}

	@Override
	public String getInvoicePrefix() {
		return invoicePrefix;
	}

	@Override
	public void setInvoicePrefix(String invoicePrefix) {
		this.invoicePrefix = invoicePrefix;
	}

	@Override
	public Integer getInvoiceCurrentNumber() {
		return invoiceCurrentNumber;
	}

	@Override
	public void setInvoiceCurrentNumber(Integer invoiceCurrentNumber) {
		this.invoiceCurrentNumber = invoiceCurrentNumber;
	}

	@Override
	public Boolean getUseGeneratedVariants() {		
		return useGeneratedVariants;
	}

	@Override
	public void setUseGeneratedVariants(Boolean useGeneratedVariants) {
		this.useGeneratedVariants = useGeneratedVariants;		
	}

	@Override
	public Boolean getEnableOrderOfNotPresentItem() {		
		return enableOrderOfNotPresentItem;
	}

	@Override
	public void setEnableOrderOfNotPresentItem(Boolean enableOrderOfNotPresentItem) {
		this.enableOrderOfNotPresentItem = enableOrderOfNotPresentItem;
	}
	
	@Override
	public Boolean isFastOrder() {
		return fastOrder;
	}
	
	@Override
	public void setFastOrder(Boolean fastOrder) {
		this.fastOrder = fastOrder;
	}

	@Override
	public void setOrderItemsPricesWithDiscounts(Boolean orderItemsPricesWithDiscounts) {
		this.orderItemsPricesWithDiscounts = orderItemsPricesWithDiscounts;		
	}

	@Override
	public Boolean getOrderItemsPricesWithDiscounts() {		
		return orderItemsPricesWithDiscounts;
	}
}
