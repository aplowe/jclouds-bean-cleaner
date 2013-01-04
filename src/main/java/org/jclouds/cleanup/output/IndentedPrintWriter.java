/*
 * Copyright 2013 Cloudsoft Corporation
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
package org.jclouds.cleanup.output;

import com.google.common.base.Strings;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Attempts to indent java source code
 * TODO adjust to cope better with badly formatted input
 * (note: we're only using this for finding inner class boundaries so it'll do for now!)
 *
 * @author Adam Lowe
 */
public class IndentedPrintWriter extends PrintWriter {

   public final String indentString = "   ";
   private int beforeIndent = 0;
   private int afterIndent = 0;
   private boolean inComment = false;
   private boolean hangingIndent = false;
   private boolean hangingComment = false;
   private StringBuffer line = new StringBuffer();

   public IndentedPrintWriter(OutputStream outputStream) {
      super(outputStream);
      flush();
   }

   public int currentIndent() {
      return afterIndent;
   }

   @Override
   public void println() {
      String toPrint = line.toString().trim();

      if (hangingIndent && !toPrint.equals("}") && !toPrint.endsWith("{")) {
         beforeIndent += 2;
      }
      hangingIndent = (!inComment && !toPrint.isEmpty() && !toPrint.startsWith("@") && !toPrint.startsWith("//"));

      // Special case...
      if (toPrint.equals("}") || toPrint.equals(")")) beforeIndent--;

      if (beforeIndent < 0) beforeIndent = 0;

      // TODO avoid long lines here!
      printlnAux(toPrint);

      line.setLength(0);

      beforeIndent = afterIndent;
      hangingComment = inComment;

      if (toPrint.endsWith("}") || toPrint.endsWith(";") || toPrint.endsWith("{") || toPrint.endsWith("*/")) {
         hangingIndent = false;
      }
   }

   private void printlnAux(String toPrint) {
      super.print(Strings.repeat(indentString, beforeIndent) + (hangingComment ? " " : ""));
      super.print(toPrint);
      super.println();
   }

   @Override
   public void print(String stuff) {
      if (stuff == null) {
         super.print(stuff);
         return;
      }

      String trimmedStuff = stuff.trim();
      if (trimmedStuff.startsWith("/**")) {
         inComment = true; // nothing to see here methinks
      }

      // Note we do not cope with oddly formatted javadocs here!
      if (inComment) {
         if (trimmedStuff.contains("*/")) inComment = false;
      } else if (trimmedStuff.startsWith("//") || trimmedStuff.startsWith("@")) {
      } else {
         for (char c : trimmedStuff.toCharArray()) {
            switch (c) {
               case '(':
                  afterIndent++;
                  break;
               case ')':
                  afterIndent--;
                  break;
               case '{':
                  afterIndent++;
                  break;
               case '}':
                  afterIndent--;
                  break;
            }
         }
      }
      line.append(stuff);
   }

   @Override
   public void print(char[] chars) {
      print(new String(chars));
   }

   // Handle braces (alter indent)
   @Override
   public void println(char[] chars) {
      print(chars);
      println();
   }

   @Override
   public void println(String s) {
      print(s);
      println();
   }

   @Override
   public void println(Object o) {
      println(o.toString().toCharArray());
   }

   @Override
   public void println(char c) {
      println(new char[]{c});
   }

   // Overriding these just in case super.println() doesn't call println()
   @Override
   public void println(boolean b) {
      super.print(b);
      println();
   }


   @Override
   public void println(int i) {
      super.print(i);
      println();
   }

   @Override
   public void println(long l) {
      super.print(l);
      println();
   }

   @Override
   public void println(float v) {
      super.print(v);
      println();
   }

   @Override
   public void println(double v) {
      super.print(v);
      println();
   }

   @Override
   public void flush() {
      super.flush();
      afterIndent = beforeIndent = 0;
      inComment = hangingComment = hangingIndent = false;
   }
}
