package sk.seges.sesam.core.test.selenium.report.model;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.selenium.model.EnvironmentInfo;
import sk.seges.sesam.core.test.selenium.report.ScreenshotsWebDriverEventListener;

public class CommandResult {

	enum Tag {
		ANCHOR {
			@Override
			public String getTagName() {
				return "a";
			}

			@Override
			public String getName() {
				return "anchor";
			}
		},
		BUTTON {

			@Override
			public String getTagName() {
				return "button";
			}

			@Override
			public String getName() {
				return "button";
			}
			
		},
		CODE {

			@Override
			public String getTagName() {
				return "code";
			}

			@Override
			public String getName() {
				return "code";
			}
			
		},
		DIV {

			@Override
			public String getTagName() {
				return "div";
			}

			@Override
			public String getName() {
				return "div";
			}
		},
		FORM {

			@Override
			public String getTagName() {
				return "form";
			}

			@Override
			public String getName() {
				return "form";
			}
			
		},
		H1 {
			@Override
			public String getTagName() {
				return "h1";
			}

			@Override
			public String getName() {
				return "h1";
			}
		},
		H2 {

			@Override
			public String getTagName() {
				return "h2";
			}

			@Override
			public String getName() {
				return "h2";
			}
		},
		H3 {

			@Override
			public String getTagName() {
				return "h3";
			}

			@Override
			public String getName() {
				return "h3";
			}
		},
		H4 {

			@Override
			public String getTagName() {
				return "h4";
			}

			@Override
			public String getName() {
				return "h4";
			}
		},
		H5 {

			@Override
			public String getTagName() {
				return "h5";
			}

			@Override
			public String getName() {
				return "h5";
			}
		},
		H6 {

			@Override
			public String getTagName() {
				return "h6";
			}

			@Override
			public String getName() {
				return "h6";
			}
		},

		IFRAME {

			@Override
			public String getTagName() {
				return "iframe";
			}

			@Override
			public String getName() {
				return "iframe";
			}
		},
		IMG {

			@Override
			public String getTagName() {
				return "img";
			}

			@Override
			public String getName() {
				return "img";
			}
		},

		TEXT {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "text";
			}
			
			@Override
			public String getName() {
				return "input.text";
			}
		},

		CHECKBOX {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "checkbox";
			}
			
			@Override
			public String getName() {
				return "input.checkbox";
			}
		},

		FILE {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "file";
			}
			
			@Override
			public String getName() {
				return "input.file";
			}
		},

		HIDDEN {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "hidden";
			}
			
			@Override
			public String getName() {
				return "input.hidden";
			}
		},

		INPUTIMAGE {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "image";
			}
			
			@Override
			public String getName() {
				return "input.image";
			}
		},

		PASSWORD {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "password";
			}
			
			@Override
			public String getName() {
				return "input.password";
			}
		},

		RADIO {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "radio";
			}
			
			@Override
			public String getName() {
				return "input.radio";
			}
		},

		RESET {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "reset";
			}
			
			@Override
			public String getName() {
				return "input.reset";
			}
		},

		SUBMIT {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "submit";
			}
			
			@Override
			public String getName() {
				return "input.submit";
			}
		},

		INPUTBUTTON {

			@Override
			public String getTagName() {
				return "input";
			}

			@Override
			public String getAttributeName() {
				return "type";
			}
			
			@Override
			public String getAttributeValue() {
				return "button";
			}
			
			@Override
			public String getName() {
				return "input.button";
			}
		},

		LABEL {

			@Override
			public String getTagName() {
				return "label";
			}

			@Override
			public String getName() {
				return "label";
			}
			
		},
		LI {

			@Override
			public String getTagName() {
				return "li";
			}

			@Override
			public String getName() {
				return "li";
			}
			
		},
		OBJECT {

			@Override
			public String getTagName() {
				return "object";
			}

			@Override
			public String getName() {
				return "object";
			}
			
		},
		OL {

			@Override
			public String getTagName() {
				return "ol";
			}

			@Override
			public String getName() {
				return "ol";
			}
			
		},
		P {

			@Override
			public String getTagName() {
				return "p";
			}

			@Override
			public String getName() {
				return "paragraph";
			}
			
		},
		PRE {

			@Override
			public String getTagName() {
				return "pre";
			}

			@Override
			public String getName() {
				return "pre";
			}
			
		},
		SCRIPT {

			@Override
			public String getTagName() {
				return "script";
			}

			@Override
			public String getName() {
				return "script";
			}
			
		},
		SPAN {

			@Override
			public String getTagName() {
				return "span";
			}

			@Override
			public String getName() {
				return "span";
			}
			
		},
		TABLE {

			@Override
			public String getTagName() {
				return "table";
			}

			@Override
			public String getName() {
				return "table";
			}
			
		},
		TD {

			@Override
			public String getTagName() {
				return "td";
			}

			@Override
			public String getName() {
				return "td";
			}
			
		},
		TEXTAREA {

			@Override
			public String getTagName() {
				return "textarea";
			}

			@Override
			public String getName() {
				return "textarea";
			}
			
		},
		TR {

			@Override
			public String getTagName() {
				return "tr";
			}

			@Override
			public String getName() {
				return "tr";
			}
			
		},
		UL {

			@Override
			public String getTagName() {
				return "ul";
			}

			@Override
			public String getName() {
				return "ul";
			}
			
		};
		
		public abstract String getTagName();
		public abstract String getName();

		public String getAttributeName() {
			return null;
		}
		
		public String getAttributeValue() {
			return null;
		}
		
		public static Tag of(String tagname, String type) {
			for (Tag tag: Tag.values()) {
				if (tag.getTagName().toLowerCase().equals(tagname.toLowerCase())) {
					if (tag.getAttributeName() != null && tag.getAttributeValue() != null && type != null) {
						if (tag.getAttributeName().toLowerCase().equals("type".toLowerCase()) &&
							tag.getAttributeValue().toLowerCase().equals(type.toLowerCase())) {
							return tag;
						}
					} else {
						return tag;
					}
				}
			}
			
			return null;
		}
	}
	
	class WebElementWrapper implements WebElement {

		private final WebElement webElement;
		private String name;
		private Point location;
		private Dimension size;
		
		public WebElementWrapper(WebElement webElement, WebDriver webDriver) {
			this.webElement = webElement;
				
			try {
				Tag tag = Tag.of(webElement.getTagName(), webElement.getAttribute("type"));
				if (tag != null) {
					String result = bundle.getString(tag.getName());
					
					if (webElement.getText() != null && webElement.getText().length() > 0) {
						result += " " + bundle.getString("text") + " \"" + webElement.getText() + "\"";
					}
					
					name = result;
				}

				this.location = this.webElement.getLocation();
				this.size = this.webElement.getSize();
			} catch (Exception ex) {
				name = super.toString();
			};
		}
		
		@Override
		public void clear() {
			webElement.clear();
		}

		@Override
		public void click() {
			webElement.click();
		}

		@Override
		public WebElement findElement(By arg0) {
			return webElement.findElement(arg0);
		}

		@Override
		public List<WebElement> findElements(By arg0) {
			return webElement.findElements(arg0);
		}

		@Override
		public String getAttribute(String arg0) {
			return webElement.getAttribute(arg0);
		}

		@Override
		public String getCssValue(String arg0) {
			return webElement.getCssValue(arg0);
		}

		@Override
		public Point getLocation() {
			return location;
		}

		@Override
		public Dimension getSize() {
			return size;
		}

		@Override
		public String getTagName() {
			return webElement.getTagName();
		}

		@Override
		public String getText() {
			return webElement.getText();
		}

		@Override
		public boolean isDisplayed() {
			return webElement.isDisplayed();
		}

		@Override
		public boolean isEnabled() {
			return webElement.isEnabled();
		}

		@Override
		public boolean isSelected() {
			return webElement.isSelected();
		}

		@Override
		public void sendKeys(CharSequence... arg0) {
			webElement.sendKeys(arg0);
		}

		@Override
		public void submit() {
			webElement.submit();
		}
				
		@Override
		public String toString() {
			return name;
		}
	}

	private SeleniumOperation operation;
	private SeleniumOperationResult result;
	private SeleniumOperationState state;
	private String screenshotName;
	
	private Object[] parameters;
	private Throwable throwable;
	
	private final ResourceBundle bundle;
	
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;
	
	private final CommandResult previousCommandResult;
	
	public CommandResult(CommandResult previousCommandResult, String locale, WebDriver webDriver, EnvironmentInfo environmentInfo) {
		this.bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".MessagesBundle", new Locale(locale));
		this.webDriver = webDriver;
		this.environmentInfo = environmentInfo;
		this.previousCommandResult = previousCommandResult;
	}

	public boolean isScreenshotUpdated() {
		return screenshotName != null;
	}
	
	public Long getWindowSize() {
		return environmentInfo.getWindowSize();
	}

	public String getScreenshotName() {
		if (screenshotName != null) {
			return screenshotName;
		}
		
		if (previousCommandResult != null) {
			return previousCommandResult.getScreenshotName();
		}
		
		return ScreenshotsWebDriverEventListener.DEFAULT_SCREENSHOT;
	}
	
	public void setScreenshotName(String screenshotName) {
		this.screenshotName = screenshotName;
	}
	
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public String getThrowableMessage() {
		if (getThrowable() == null) {
			return "";
		}
		ThrowableLocalizer throwableLocalizer = ThrowableLocalizer.get(getThrowable());
		if (throwableLocalizer == null) {
			return getThrowable().getLocalizedMessage();
		}
		
		return bundle.getString(throwableLocalizer.getKey());
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public void setOperation(SeleniumOperation operation) {
		this.operation = operation;
	}
	
	public SeleniumOperation getOperation() {
		return operation;
	}

	public String getOperationDescription() {
		return operation.getDescription();
	}

	public void setResult(SeleniumOperationResult result) {
		this.result = result;
	}
	
	public SeleniumOperationResult getResult() {
		return result;
	}

	public void setState(SeleniumOperationState state) {
		this.state = state;
	}
	
	public SeleniumOperationState getState() {
		return state;
	}
	
	public void setParameters(Object... parameters) {
		Object[] params = new Object[parameters.length];
		
		int i = 0;
		for (Object param: parameters) {
			if (param instanceof WebElement) {
				params[i++] = new WebElementWrapper((WebElement)param, webDriver);
			} else {
				params[i++] = param;
			}
		}
		
		this.parameters = params;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	public boolean isFailure() {
		return getResult().equals(SeleniumOperationResult.FAILURE);
	}
}