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
package org.jclouds.cleanup.output.bean;

import java.io.PrintWriter;
import java.util.List;

import org.jclouds.cleanup.output.ClassDocPrinter;

import com.sun.javadoc.ClassDoc;

public class EqualsPrinter extends ClassDocPrinter {

   public void write(ClassDoc classDoc, PrintWriter out) {
      boolean callSuperDot = extendsSomething(classDoc);

      List<String> fieldNames = getFieldNames(classDoc);

      if (fieldNames.isEmpty()) return;
      out.println("@Override");
      out.println("public boolean equals(Object obj) {");
      if (callSuperDot) {
         out.println("if (!super.equals(obj)) return false;");
      } else {
         out.println("if (this == obj) return true;");
         out.println("if (obj == null) return false;");
      }
      out.println("if (getClass() != obj.getClass()) return false;");
      out.println(classDoc.simpleTypeName() + " other = (" + classDoc.simpleTypeName() + ") obj;");
      out.print("return Objects.equal(" + fieldNames.get(0) + ", other." + fieldNames.get(0) + ")");

      for (String f : fieldNames.subList(1, fieldNames.size())) {
         out.println();
         out.print("&& Objects.equal(" + f + ", other." + f + ")");
      }
      out.println(";");

      out.println("}");
      out.println();
   }

}
