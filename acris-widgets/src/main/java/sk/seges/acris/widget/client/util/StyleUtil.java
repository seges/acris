/*
 * Copyright 2008 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.seges.acris.widget.client.util;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * This is a CSS manipulation util.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class StyleUtil {
    /**
     * This method sets a href attribute of the specified link element.
     *
     * @param linkElementId is a link element.
     * @param url is a URL to be applied for the href.
     */
    public static native void setLinkHref(String linkElementId, String url) /*-{
        var link = $doc.getElementById(linkElementId);
        if (link != null && link != undefined) {
            link.href = url;
        }
    }-*/;
    
    public static native String getComputedStyle(com.google.gwt.dom.client.Element element, String cssRule) /*-{
		var strValue = "";
		if (element) {
			if (element.currentStyle) {
				cssRule = cssRule.replace(/\-(\w)/g, function(strMatch, p1) {
					return p1.toUpperCase();
				});
				strValue = element.currentStyle[cssRule] + "";
			} else if ($wnd.getComputedStyle) {
				strValue = $wnd.getComputedStyle(element, null).getPropertyValue(cssRule.replace(/([A-Z])/g, "-$1").toLowerCase());
			}
		}
		return strValue;
	}-*/;

	public static Double getDoubleValueOfStringPropertyWithUnit(String value, Unit unit) {
		value = value.toUpperCase().replaceAll(unit + "", "");

		if (value != null && !value.isEmpty()) {
			return Double.parseDouble(value);
		} else {
			return 0D;
		}
	}

	public static double getBoxShadowWidth(com.google.gwt.dom.client.Element element) {
		String boxShadow = StyleUtil.getComputedStyle(element, "box-shadow");
		String[] split = boxShadow.toUpperCase().split(Unit.PX + "");
		if (split.length > 1) {
			String width = split[2].replace(" ", "");
			if (width != null && !width.isEmpty()) {
				return Double.parseDouble(width);
			} else {
				return 0;
			}
		} else {
			return 0;
		}

	}

	/**
	 * This method calculates sum of styles width (left and right border, padding, box-shadow).
	 * 
	 * @param element
	 * @return double value of styles width
	 */
	public static double calculateComputedStylesWidth(com.google.gwt.dom.client.Element element) {
		String borderLeft = StyleUtil.getComputedStyle(element, "border-left-width");
		double borderLeftDouble = StyleUtil.getDoubleValueOfStringPropertyWithUnit(borderLeft, Unit.PX);
		String borderRight = StyleUtil.getComputedStyle(element, "border-right-width");
		double borderRightDouble = StyleUtil.getDoubleValueOfStringPropertyWithUnit(borderRight, Unit.PX);
		double boxShadowWidth = getBoxShadowWidth(element);
		String paddingRight = StyleUtil.getComputedStyle(element, "padding-right");
		double paddingRightDouble = StyleUtil.getDoubleValueOfStringPropertyWithUnit(paddingRight, Unit.PX);
		String paddingLeft = StyleUtil.getComputedStyle(element, "padding-left");
		double paddingLeftDouble = StyleUtil.getDoubleValueOfStringPropertyWithUnit(paddingLeft, Unit.PX);

		return borderLeftDouble + borderRightDouble + paddingRightDouble + paddingLeftDouble + 2 * boxShadowWidth;
	}
	
	public static Double getMargin(String marginType, com.google.gwt.dom.client.Element element) {
		String margin = element.getStyle().getProperty(marginType).replace("%", "").replace("px", "");
		return Double.valueOf((margin == null || margin.isEmpty()) ? "0" : margin);
	}
	
	public static boolean onScreen(Element element) {
		int absoluteLeft = element.getAbsoluteLeft();
		int absoluteTop = element.getAbsoluteTop();
		int clientHeight = Window.getClientHeight();
		int clientWidth = Window.getClientWidth();
		if (absoluteLeft >= 0 && absoluteLeft <= clientWidth && absoluteTop >= 0 && absoluteTop <= clientHeight) {
			return true;
		}
		return false;
	}
}
