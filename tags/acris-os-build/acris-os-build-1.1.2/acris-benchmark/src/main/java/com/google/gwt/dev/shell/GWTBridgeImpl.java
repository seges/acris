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
package com.google.gwt.dev.shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwt.core.client.GWTBridge;
import com.google.gwt.dev.About;

/**
 * This class is the hosted-mode peer for {@link com.google.gwt.core.client.GWT}.
 */
public class GWTBridgeImpl extends GWTBridge {
	private File benchFile = null;
	private PrintWriter writer;
  private final ShellJavaScriptHost host;

  public GWTBridgeImpl(ShellJavaScriptHost host) {
    this.host = host;
    try {
    	DateFormat format = new SimpleDateFormat("yyyyMMDD_HHmmssSSS");
    	String date = format.format(new Date());
		benchFile = File.createTempFile("gwt-create-" + Thread.currentThread().getId() + "-", "-" + date);
		writer = new PrintWriter(benchFile);
		writer.println("<table>");
	} catch (IOException e) {
		throw new RuntimeException("fuuuk", e);
	}
  }

  /**
   * Resolves a deferred binding request and create the requested object.
   */
  @Override
  public <T> T create(Class<?> requestedClass) {
	  long millis = new Date().getTime();
    String className = requestedClass.getName();
    try {
      return host.<T> rebindAndCreate(className);
    } catch (Throwable e) {
      String msg = "Deferred binding failed for '" + className
          + "' (did you forget to inherit a required module?)";
      throw new RuntimeException(msg, e);
    } finally {
    	millis = new Date().getTime() - millis;
    	writer.println("<tr class=\"measurement\">");
    	writer.println("\t<td class=\"classType\">");
    	writer.println("\t\t" + requestedClass);
    	writer.println("\t</td>");
    	writer.println("\t<td class=\"time\">");
    	writer.println("\t\t" + millis);
    	writer.println("\t</td>");
    	writer.println("\t<td class=\"stack\">");
    	new RuntimeException().printStackTrace(writer);
    	writer.println("\t</td>");
    	writer.println("</tr>");
    	writer.flush();
    }
  }

  @Override
  public String getVersion() {
    return About.getGwtVersionNum();
  }

  /**
   * Yes, we're running as client code in the hosted mode classloader.
   */
  @Override
  public boolean isClient() {
    return true;
  }

  /**
   * Logs in dev shell.
   */
  @Override
  public void log(String message, Throwable e) {
    host.log(message, e);
  }

}
