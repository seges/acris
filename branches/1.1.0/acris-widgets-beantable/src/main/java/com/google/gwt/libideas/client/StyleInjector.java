/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.libideas.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ResourcePrototype;

/**
 * Used to add stylesheets to the document.
 */
public class StyleInjector {

  /**
   * The DOM-compatible way of adding stylesheets.
   */
  public static class StyleInjectorImpl {
    private static final StyleInjectorImpl IMPL = GWT.create(StyleInjectorImpl.class);

    public StyleElement injectStyleSheet(String contents) {
      StyleElement style = Document.get().createStyleElement();
      style.setPropertyString("language", "text/css");
      setContents(style, contents);
      Document.get().getElementsByTagName("head").getItem(0).appendChild(style);
      return style;
    }

    public void setContents(StyleElement style, String contents) {
      style.setInnerText(contents);
    }
  }

  /**
   * IE doesn't allow manipulation of a style element through DOM methods.
   */
  public static class StyleInjectorImplIE extends StyleInjectorImpl {
    @Override
    public StyleElement injectStyleSheet(String contents) {
      StyleElement style = createElement();
      setContents(style, contents);
      return style;
    }

    @Override
    public native void setContents(StyleElement style, String contents) /*-{
      style.cssText = contents;
    }-*/;

    private native StyleElement createElement() /*-{
      return $doc.createStyleSheet();
    }-*/;
  }

  /**
   * Inject all CSSResources in an ImmutableResourceBundle. This method is
   * deprecated to isolate StyleInjector from the bundle API.
   * 
   * @param resources the ImmutableResourceBundle to inject
   * @deprecated Use {@link #injectStyleSheet(String)} and
   *             {@link CssResource#getText()}.
   */
//  @Deprecated
//  public static StyleElement injectStylesheet(ClientBundle resources) {
//    StringBuilder sb = new StringBuilder();
//    for (ResourcePrototype p : resources.getResources()) {
//      if (p instanceof CssResource) {
//        sb.append(((CssResource) p).getText()).append("\n");
//      }
//    }
//    return injectStylesheet(sb.toString());
//  }

  /**
   * Add a stylesheet to the document.
   * 
   * @param contents the CSS contents of the stylesheet
   */
  public static StyleElement injectStylesheet(String contents) {
    return StyleInjectorImpl.IMPL.injectStyleSheet(contents);
  }

  /**
   * Add a stylesheet to the document. Any occurrence of
   * <code>%resourceName%</code> in the stylesheet's contents will be replaced
   * by the URL of the named {@link DataResource} contained in
   * <code>references</code>.
   * 
   * @param contents the CSS contents of the stylesheet
   * @param references the resources to substitute into the stylesheet.
   * @deprecated Use {@link CssResource} with {@code @url} rules instead.
   */
//  @Deprecated
//  public static StyleElement injectStylesheet(String contents,
//      ClientBundle references) {
//    if (references != null) {
//      for (ResourcePrototype p : references.getResources()) {
//        if (p instanceof DataResource) {
//          contents = contents.replaceAll("%" + p.getName() + "%",
//              ((DataResource) p).getUrl());
//        }
//      }
//    }
//
//    return injectStylesheet(contents);
//  }

  /**
   * Replace the contents of a previously-injected stylesheet. Updating the
   * stylesheet in-place is typically more efficient than removing a
   * previously-created element and adding a new one.
   * 
   * @param style a StyleElement previously-returned from
   *          {@link #injectStylesheet(String)}.
   * @param contents the new contents of the stylesheet.
   */
  public static void setContents(StyleElement style, String contents) {
    StyleInjectorImpl.IMPL.setContents(style, contents);
  }

  /**
   * Utility class.
   */
  private StyleInjector() {
  }
}
