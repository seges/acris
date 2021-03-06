package sk.seges.acris.widget.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.picker.client.SliderBar;
import com.google.gwt.gen2.picker.client.SliderBar.LabelFormatter;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.PageCountChangeEvent;
import com.google.gwt.gen2.table.event.client.PageCountChangeHandler;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.PagingFailureEvent;
import com.google.gwt.gen2.table.event.client.PagingFailureHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that wraps a {@link PagingScrollTable} and includes options to
 * manipulate the page.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul class="css">
 * 
 * <li>.gwt-PagingOptions { applied to the entire widget }</li>
 * 
 * <li>.gwt-PagingOptions .errorMessage { applied to the error message }</li>
 * 
 * </ul>
 */
public class TreeTablePagingOptions extends Composite {

	static {
		SliderBar.injectDefaultCss();
	}
	
	/**
	 * Interface used to allow the widget access to css style names.
	 * <p/>
	 * The class names indicate the default gwt names for these styles. <br>
	 * 
	 */
	public static interface Css extends CssResource {
		/**
		 * Widget style name.
		 * 
		 * @return the widget's style name
		 */
		@ClassName("gwt-PagingOptions")
		String defaultStyleName();

		String errorMessage();

		String currentPageBox();
	}

	public interface PagingOptionsResources {
		PagingOptionsStyle getStyle();

		PagingOptionsMessages getMessages();
	}

	public interface PagingOptionsMessages extends Messages {
		@DefaultMessage("Go to first page")
		String gotoFirstPage();

		@DefaultMessage("Go to last page")
		String gotoLastPage();

		@DefaultMessage("Go to next page")
		String gotoNextPage();

		@DefaultMessage("Go to previous page")
		String gotoPreviousPage();

		@DefaultMessage("{0} of {1} pages")
		String currentOfPages(int currentPage, int pages);
	}

	/**
	 * Resources used.
	 */
	public interface PagingOptionsStyle extends ClientBundle {
		/**
		 * The css file.
		 */
		@Source("com/google/gwt/gen2/widgetbase/public/PagingOptions.css")
		Css css();

		@Source("firstPage.png")
		ImageResource firstPage();

		@Source("firstPageDisabled.png")
		ImageResource firstPageDisabled();

		@Source("lastPage.png")
		ImageResource lastPage();

		@Source("lastPageDisabled.png")
		ImageResource lastPageDisabled();

		@Source("nextPage.png")
		ImageResource nextPage();

		@Source("nextPageDisabled.png")
		ImageResource nextPageDisabled();

		@Source("previousPage.png")
		ImageResource previousPage();

		@Source("previousPageDisabled.png")
		ImageResource previousPageDisabled();

		@Source("headerBackground.png")
		@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
		ImageResource pagingBackground();
	}	

	protected static class DefaultPagingOptionsResources implements PagingOptionsResources {
		private PagingOptionsStyle style;
		private PagingOptionsMessages messages;

		public PagingOptionsStyle getStyle() {
			if (style == null) {
				style = ((PagingOptionsStyle) GWT.create(PagingOptionsStyle.class));
			}
			return style;
		}

		public PagingOptionsMessages getMessages() {
			if (messages == null) {
				messages = ((PagingOptionsMessages) GWT.create(PagingOptionsMessages.class));
			}
			return messages;
		}
	}

	private HorizontalPanel buttonPanel;

	/**
	 * The label used to display errors.
	 */
	private HTML errorLabel;

	/**
	 * Buttons
	 */
	private PushButton firstPageButton, lastPageButton, nextPageButton, previousPageButton;

	/**
	 * The loading image.
	 */
	private Image loadingImage;

	/**
	 * The box where the user can select the current page.
	 */
	private TextBox currentPageBox = new TextBox();

	private SliderBar pageSlider = new SliderBar(1, 1, new LabelFormatter() {
		public String formatLabel(SliderBar slider, double value) {
			return String.valueOf((int) value);
		}
	});
	/**
	 * The table being affected.
	 */
	private PagingScrollTable<?> table;
	private PagingOptionsResources resources;
	private int pageCount;

	/**
	 * Constructor.
	 * 
	 * @param table
	 *            the table being ad
	 */
	public TreeTablePagingOptions(PagingScrollTable<?> table, boolean pageCountAvailable) {
		this(table, new DefaultPagingOptionsResources(), pageCountAvailable);
	}

	/**
	 * Constructor.
	 * 
	 * @param table
	 *            the table being affected
	 * @param resources
	 *            the images to use
	 */
	public TreeTablePagingOptions(PagingScrollTable<?> table, PagingOptionsResources resources, boolean pageCountAvailable) {
		this.table = table;
		this.resources = resources;
		 Css css = resources.getStyle().css();
		// if (Gen2CssInjector.isInjectionEnabled()) {
		// Gen2CssInjector.inject(css);
		// }

		// Create the main widget
		HorizontalPanel pagingPanel = new HorizontalPanel();
		initWidget(pagingPanel);
		pagingPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pagingPanel.setWidth("100%");
		setStyleName(resources.getStyle().css().defaultStyleName());

		// Create the paging image buttons
		createPageButtons();

		if (pageCountAvailable) {
			// Create the current page box
			createCurrentPageBox();

			pageSlider.setStepSize(1.0);
			pageSlider.setMaxValue(getPageCount());
			pageSlider.addValueChangeHandler(new ValueChangeHandler<Double>() {
				private Timer timer;

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					final int currentPage = (int) pageSlider.getCurrentValue();
					pageSlider.setTitle(TreeTablePagingOptions.this.resources.getMessages().currentOfPages(currentPage,
							getPageCount()));
					if (timer != null) {
						timer.cancel();
					}
					timer = new Timer() {
						public void run() {
							TreeTablePagingOptions.this.table.gotoPage(currentPage - 1, false);
						}
					};
					timer.schedule(250);
				}
			});
		}
		// Create the loading image
		loadingImage = new Image("scrollTableLoading.gif");
		loadingImage.setVisible(false);

		// Create the error label
		errorLabel = new HTML();
		errorLabel.setStylePrimaryName("errorMessage");

		buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(3);
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPanel.add(firstPageButton);
		buttonPanel.add(previousPageButton);
		if (pageCountAvailable) {
			buttonPanel.add(pageSlider);
			buttonPanel.add(currentPageBox);
		}
		buttonPanel.add(nextPageButton);
		if (pageCountAvailable) {
			buttonPanel.add(lastPageButton);
		}
		buttonPanel.add(loadingImage);
		pagingPanel.add(buttonPanel);
		// Add the widgets to the panel
		pagingPanel.add(errorLabel);

		// Add handlers to the table
		table.addPageLoadHandler(new PageLoadHandler() {
			public void onPageLoad(PageLoadEvent event) {
				loadingImage.setVisible(false);
				errorLabel.setHTML("");
				updateButtonState();
			}
		});
		table.addPageChangeHandler(new PageChangeHandler() {
			public void onPageChange(PageChangeEvent event) {
				currentPageBox.setText((event.getNewPage() + 1) + "");
				loadingImage.setVisible(true);
				errorLabel.setHTML("");
			}
		});
		table.addPagingFailureHandler(new PagingFailureHandler() {
			public void onPagingFailure(PagingFailureEvent event) {
				loadingImage.setVisible(false);
				errorLabel.setHTML(event.getException().getMessage());
			}
		});
		table.addPageCountChangeHandler(new PageCountChangeHandler() {
			public void onPageCountChange(PageCountChangeEvent event) {
				setPageCount(event.getNewPageCount());
				updateButtonState();
			}
		});
		setPageCount(table.getPageCount());
	}

	/**
	 * @return the {@link PagingScrollTable}.
	 */
	public PagingScrollTable<?> getPagingScrollTable() {
		return table;
	}

	public HorizontalPanel getButtonPanel() {
		return buttonPanel;
	}

	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Create a box that holds the current page.
	 */
	protected void createCurrentPageBox() {
		// Setup the widget
		currentPageBox.setStyleName(resources.getStyle().css().currentPageBox());
		currentPageBox.setText("1");

		// Disallow non-numeric pages
		KeyboardListenerAdapter listener = new KeyboardListenerAdapter() {
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == (char) KEY_ENTER) {
					TreeTablePagingOptions.this.table.gotoPage(getPagingBoxValue(), false);
				} else if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
						&& (keyCode != (char) KEY_BACKSPACE) && (keyCode != (char) KEY_DELETE)
						&& (keyCode != (char) KEY_ENTER) && (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
						&& (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP) && (keyCode != (char) KEY_RIGHT)
						&& (keyCode != (char) KEY_DOWN)) {
					((TextBox) sender).cancelKey();
				}
			}
		};

		// Add the listener
		currentPageBox.addKeyboardListener(listener);
	}

	/**
	 * Create a paging image buttons.
	 * 
	 * @param resources
	 *            the resources to use
	 */
	protected void createPageButtons() {
		firstPageButton = new PushButton(createImage(resources.getStyle().firstPage()));
		firstPageButton.getUpDisabledFace().setImage(createImage(resources.getStyle().firstPageDisabled()));
		firstPageButton.setTitle(resources.getMessages().gotoFirstPage());

		lastPageButton = new PushButton(createImage(resources.getStyle().lastPage()));
		lastPageButton.getUpDisabledFace().setImage(createImage(resources.getStyle().lastPageDisabled()));
		lastPageButton.setTitle(resources.getMessages().gotoLastPage());

		nextPageButton = new PushButton(createImage(resources.getStyle().nextPage()));
		nextPageButton.getUpDisabledFace().setImage(createImage(resources.getStyle().nextPageDisabled()));
		nextPageButton.setTitle(resources.getMessages().gotoNextPage());

		previousPageButton = new PushButton(createImage(resources.getStyle().previousPage()));
		previousPageButton.getUpDisabledFace().setImage(createImage(resources.getStyle().previousPageDisabled()));
		previousPageButton.setTitle(resources.getMessages().gotoPreviousPage());

		// Create the listener
		ClickHandler handler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				Object source = event.getSource();
				if (source == firstPageButton) {
					table.gotoFirstPage();
				} else if (source == lastPageButton) {
					table.gotoLastPage();
				} else if (source == nextPageButton) {
					table.gotoNextPage();
				} else if (source == previousPageButton) {
					table.gotoPreviousPage();
				}
			}
		};

		// Add the listener to each image
		firstPageButton.addClickHandler(handler);
		lastPageButton.addClickHandler(handler);
		nextPageButton.addClickHandler(handler);
		previousPageButton.addClickHandler(handler);
	}

	protected Image createImage(ImageResource imageResource) {
		Image image = new Image();
		applyImage(image, imageResource);
		return image;
	}

	protected void applyImage(Image image, ImageResource imageResource) {
		image.setUrlAndVisibleRect(imageResource.getURL(), imageResource.getLeft(), imageResource.getTop(),
				imageResource.getWidth(), imageResource.getHeight());
	}

	protected void updateButtonState() {
		int currentPage = getPagingBoxValue();
		if (currentPage <= 0) {
			firstPageButton.setEnabled(false);
			previousPageButton.setEnabled(false);
		} else {
			firstPageButton.setEnabled(true);
			previousPageButton.setEnabled(true);
		}
		if (currentPage >= pageCount - 1) {
			nextPageButton.setEnabled(false);
			lastPageButton.setEnabled(false);
		} else {
			nextPageButton.setEnabled(true);
			lastPageButton.setEnabled(true);
		}
		pageSlider.setCurrentValue(getPagingBoxValue() + 1);
	}

	/**
	 * Get the value of in the page box. If the value is invalid, it will be set
	 * to 1 automatically.
	 * 
	 * @return the value in the page box
	 */
	protected int getPagingBoxValue() {
		int page = 0;
		try {
			page = Integer.parseInt(currentPageBox.getText()) - 1;
		} catch (NumberFormatException e) {
			// This will catch an empty box
			currentPageBox.setText("1");
		}

		// Replace values less than 1
		if (page < 1) {
			currentPageBox.setText("1");
			page = 0;
		}

		// Return the 0 based page, not the 1 based visible value
		return page;
	}

	/**
	 * Set the page count.
	 * 
	 * @param pageCount
	 *            the current page count
	 */
	protected void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		if (pageCount < 0) {
			pageSlider.setVisible(false);
			lastPageButton.setVisible(false);
		} else {
			pageSlider.setVisible(true);
			pageSlider.setMaxValue(pageCount);
			pageSlider.setNumLabels(Math.min(getPageCount() - 1, 5));
			pageSlider.setNumTicks(Math.min(getPageCount() - 1, 10));
			lastPageButton.setVisible(true);
		}
	}
	
	public SliderBar getPageSlider() {
		return pageSlider;
	}

	public Image getLoadingImage() {
		return loadingImage;
	}
}
