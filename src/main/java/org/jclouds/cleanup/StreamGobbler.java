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
package org.jclouds.cleanup;

import com.google.common.collect.ImmutableSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamGobbler extends Thread {
   private static final Set<String> levels = ImmutableSet.of(
         "DEBUG:", "INFO:", "WARN:", "ERROR:", "FINE:", "FINER:", "FINEST:"
   );
   
   InputStream is;
   String type;

   public StreamGobbler(InputStream is, String type) {
      this.is = checkNotNull(is, "is");
      this.type = checkNotNull(type, "type");
   }

   public void run() {
      try {
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);
         String line;
         while ((line = br.readLine()) != null) {
            // Stripping out boring timestamps
            if (line.contains("org.jclouds.logging.jdk.JDKLogger") || line.contains("freemarker.log.JDK14LoggerFactory")) continue;
            boolean isLoggingLine = false;
            for (String level : levels) {
               isLoggingLine = isLoggingLine || line.startsWith(level);
            } 
            System.out.println(isLoggingLine ? line : type + ": " + line);
         }
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }
}