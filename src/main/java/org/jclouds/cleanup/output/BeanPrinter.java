/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.cleanup.output;

import static com.google.common.io.ByteStreams.toByteArray;
import static com.google.common.io.Closeables.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.jclouds.util.Strings2;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.sun.javadoc.ClassDoc;

public class BeanPrinter extends ClassDocPrinter {
   // Used to re-order imports
   private static final List<String> ECLIPSE_INPUT_ORDER = ImmutableList.of("import static", "import java.", "import javax.", "import org.", "import ");
   private final String outputPath;
   private final String header;

   public BeanPrinter(String outputPath, List<ClassDocPrinter> contentsPrinters) throws IOException {
      this.outputPath = outputPath;
      InputStream stream = BeanPrinter.class.getClassLoader().getResourceAsStream("header.java");
      try {
         this.header = new String(toByteArray(stream), Charsets.UTF_8);
      } finally {
         closeQuietly(stream);
      }
      addChildren(contentsPrinters);
   }

   @Override
   public void write(ClassDoc theClass, PrintWriter out) {

      String className = theClass.simpleTypeName();
      String packageName = theClass.containingPackage().name();
      File outputFile = new File(outputPath, packageName.replaceAll("\\.", File.separator) + File.separator + className + ".java");
      outputFile.getParentFile().mkdirs();

      List<String> lines;
      try {
         // This is actually helpful (oddly!)
         lines = ImmutableList.copyOf(Strings2.toStringAndClose(new FileInputStream(theClass.position().file())).split("\n"));
      } catch (IOException ex) {
         throw Throwables.propagate(ex);
      }

      out.println(header);
      out.flush();

      out.println("package " + packageName + ";");
      out.println();

      addImports(Sets.filter(ImmutableSet.copyOf(lines), new Predicate<String>() {
         @Override
         public boolean apply(String input) {
            return input.trim().startsWith("import ");
         }
      }));

      for (String prefix : ECLIPSE_INPUT_ORDER) {
         boolean needGap = false;
         Iterator<String> importIt = imports.iterator();
         while (importIt.hasNext()) {
            String importLine = importIt.next();
            if (importLine.startsWith(prefix)) {
               out.println(importLine);
               importIt.remove();
               needGap = true;
            }
         }
         if (needGap) out.println();
      }

      writeComment(out, "class " + className, theClass);

      writeAnnotation(theClass, out);
      out.print("public " + (theClass.isAbstract() ? "abstract " : "") + "class " + className);

      if (extendsSomething(theClass)) {
         out.print(" extends " + theClass.superclass().simpleTypeName());
      }

      out.println(" {");

      // Inner classes
      for (ClassDoc clazz : theClass.innerClasses()) {
         if (!clazz.simpleTypeName().toLowerCase().endsWith("builder")) {
            IndentedPrintWriter ipw = (IndentedPrintWriter) out;
            writeComment(out, null, clazz);
            out.println("public" + (clazz.isEnum() ? " enum " : " class ") + clazz.simpleTypeName() + " {");
            for (int i = clazz.position().line(); ipw.currentIndent() > 1 && i < lines.size(); i++) {
               out.println(lines.get(i));
            }
            out.println();
         }
      }

      for (ClassDocPrinter printer : children) {
         printer.write(theClass, out);
      }
      out.println("}");
      out.close();
   }
}
