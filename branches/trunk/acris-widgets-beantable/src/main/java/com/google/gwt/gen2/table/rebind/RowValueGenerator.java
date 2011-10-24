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
package com.google.gwt.gen2.table.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.gen2.table.client.DateColumnDefinition;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.NumberColumnDefinition;
import com.google.gwt.gen2.table.client.RowValue;
import com.google.gwt.gen2.table.client.TextColumnDefinition;
import com.google.gwt.gen2.table.client.RowValue.ColumnDefinition;
import com.google.gwt.gen2.table.client.RowValue.DateColumnFormat;
import com.google.gwt.gen2.table.client.RowValue.NumberColumnFormat;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Generator used to bind classes extending the <code>RowValue</code> interface.
 */
public class RowValueGenerator extends Generator {
  /**
   * Generate an implementation for the given type.
   * 
   * @param logger error logger
   * @param context generator context
   * @param typeName target type name
   * @return generated class name
   * @throws UnableToCompleteException
   */
  @Override
  public final String generate(TreeLogger logger, GeneratorContext context,
      String typeName) throws UnableToCompleteException {
    TypeOracle typeOracle = context.getTypeOracle();
    assert (typeOracle != null);

    JClassType rowValue = typeOracle.findType(typeName);
    if (rowValue == null) {
      logger.log(TreeLogger.ERROR, "Unable to find metadata for type '"
          + typeName + "'", null);
      throw new UnableToCompleteException();
    }

    logger.branch(TreeLogger.DEBUG,
        "Generating table definition for row value '"
            + rowValue.getQualifiedSourceName() + "'", null);

    JPackage tableDefinitionPkg = rowValue.getPackage();
    String packageName = tableDefinitionPkg == null ? ""
        : tableDefinitionPkg.getName();
    String simpleName = "TableDefinition_" + rowValue.getSimpleSourceName();
    PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
    if (printWriter == null) {
      return null;
    }

    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
        packageName, simpleName);

    String[] imports = new String[] {
        rowValue.getQualifiedSourceName(),
        DateColumnDefinition.class.getCanonicalName(),
        DefaultTableDefinition.class.getCanonicalName(),
        NumberColumnDefinition.class.getCanonicalName(),
        TextColumnDefinition.class.getCanonicalName(),
        DateTimeFormat.class.getCanonicalName(), Date.class.getCanonicalName(),
        Double.class.getCanonicalName(), NumberFormat.class.getCanonicalName()};
    for (String imp : imports) {
      composerFactory.addImport(imp);
    }
    composerFactory.setSuperclass(DefaultTableDefinition.class.getSimpleName()
        + "<" + rowValue.getSimpleSourceName() + ">");
    SourceWriter srcWriter = composerFactory.createSourceWriter(context,
        printWriter);
    JMethod[] methods = rowValue.getMethods();
    List<JMethod> annotatedMethods = new ArrayList<JMethod>();
    for (JMethod method : methods) {
      ColumnDefinition annotation = method.getAnnotation(RowValue.ColumnDefinition.class);
      if (annotation != null) {
        annotatedMethods.add(method);
      }
    }
    Collections.sort(annotatedMethods, new Comparator<JMethod>() {
      public int compare(JMethod method1, JMethod method2) {
        return Integer.valueOf(
            method1.getAnnotation(RowValue.ColumnDefinition.class).column()).compareTo(
            Integer.valueOf(method1.getAnnotation(
                RowValue.ColumnDefinition.class).column()));
      }
    });
    // Create constructor
    srcWriter.println("public " + simpleName + "() {");
    srcWriter.indent();
    srcWriter.println("super();");
    for (JMethod method : annotatedMethods) {
      ColumnDefinition annotation = method.getAnnotation(RowValue.ColumnDefinition.class);
      String methodName = method.getName();
      String returnType = method.getReturnType().getSimpleSourceName();
      if (returnType.equals(String.class.getSimpleName())) {
        srcWriter.println("addColumnDefinition(new TextColumnDefinition<"
            + rowValue.getSimpleSourceName() + ">(\"" + annotation.header()
            + "\"," + annotation.sortable() + "," + annotation.filterable()
            + "," + annotation.editable() + ") {");
        srcWriter.indent();
        srcWriter.println("public String getCellValue("
            + rowValue.getSimpleSourceName() + " value) {");
        srcWriter.indentln("return value." + methodName + "();");
        srcWriter.println("}");
        srcWriter.outdent();
        srcWriter.println("});");
      } else if (returnType.equals(Date.class.getSimpleName())) {
        String dateTimeFormat = getDateTimeFormat(annotation.dateTimeFormat(),
            annotation.dateTimePattern());
        srcWriter.println("addColumnDefinition(new DateColumnDefinition<"
            + rowValue.getSimpleSourceName() + ">(\"" + annotation.header()
            + "\","+dateTimeFormat+", "
            + annotation.sortable() + ", " + annotation.filterable() + ","
            + annotation.editable() + ") {");
        srcWriter.indent();
        srcWriter.println("public Date getCellValue("
            + rowValue.getSimpleSourceName() + " value) {");
        srcWriter.indentln("return value." + methodName + "();");
        srcWriter.println("}");
        srcWriter.outdent();
        srcWriter.println("});");
      } else if (returnType.equals(Double.class.getSimpleName())
          || returnType.equals("double")) {
        String numberFormat = getNumberFormat(annotation.numberFormat(),
            annotation.numberPattern());
        srcWriter.println("addColumnDefinition(new NumberColumnDefinition<"
            + rowValue.getSimpleSourceName() + ">(\"" + annotation.header()
            + "\","+numberFormat+"," + annotation.filterable()
            + "," + annotation.editable() + ") {");
        srcWriter.indent();
        srcWriter.println("public Double getCellValue("
            + rowValue.getSimpleSourceName() + " value) {");
        srcWriter.indentln("return value." + methodName + "();");
        srcWriter.println("}");
        srcWriter.outdent();
        srcWriter.println("});");
      }
    }
    srcWriter.outdent();
    srcWriter.println("}");
    srcWriter.outdent();

    srcWriter.commit(logger);
    return packageName + "." + simpleName;
  }

  private String getDateTimeFormat(DateColumnFormat dateColumnFormat, String dateTimePattern) {
    switch (dateColumnFormat) {
      case SHORT_DATE_FORMAT : return "DateTimeFormat.getShortDateFormat()";
      case SHORT_TIME_FORMAT : return "DateTimeFormat.getShortTimeFormat()";
      case SHORT_DATE_TIME_FORMAT : return "DateTimeFormat.getShortDateTimeFormat()";
      case MEDIUM_DATE_FORMAT : return "DateTimeFormat.getMediumDateFormat()";
      case MEDIUM_TIME_FORMAT : return "DateTimeFormat.getMediumTimeFormat()";
      case MEDIUM_DATE_TIME_FORMAT : return "DateTimeFormat.getMediumDateTimeFormat()";
      case LONG_DATE_FORMAT : return "DateTimeFormat.getLongDateFormat()";
      case LONG_TIME_FORMAT : return "DateTimeFormat.getLongTimeFormat()";
      case LONG_DATE_TIME_FORMAT : return "DateTimeFormat.getLongDateTimeFormat()";
      case PATTERN : return "new DateTimeFormat("+dateTimePattern+")";
    }
    return "DateTimeFormat.getShortDateFormat()";
  }

  private String getNumberFormat(NumberColumnFormat numberColumnFormat, String numberPattern) {
    switch ( numberColumnFormat ) {
      case CURRENCY_FORMAT : return "NumberFormat.getCurrencyFormat()";
      case PERCENT_FORMAT : return "NumberFormat.getPercentFormat()";
      case DECIMAL_FORMAT : return "NumberFormat.getDecimalFormat()";
      case SCIENTIFIC_FORMAT : return "NumberFormat.getScientificFormat()";
      case PATTERN : return "new NumberFormat("+numberPattern+")";
    }
    return "NumberFormat.getDecimalFormat()";
  }
}
