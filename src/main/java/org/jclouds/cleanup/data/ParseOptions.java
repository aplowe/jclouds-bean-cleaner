/*
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
package org.jclouds.cleanup.data;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class ParseOptions {

   public static enum NullableHandling {
      /**
       * Only those fields marked as @Nullable in the source are generated as @Nullable
       */
      DEFAULT,
      /**
       * The field called "id" is treated as not-Nullable (if present), otherwise same as DEFAULT
       */
      NOT_ID,
      /**
       * Field called "id" is not-nullable, otherwise field IS Nullable (use with caution - ignores source annotations!)
       */
      ONLY_ID,
      /**
       * Fields "id", "name", "key" or "code" are not-nullable, otherwise same as DEFAULT
       */
      NOT_KEY,
      /**
       * Fields called "id", "name", "key" or "code" are not-nullable, otherwise field IS Nullable (use with caution -
       * ignores source annotations!)
       */
      ONLY_KEY;

      protected static final Set<String> KEYS = ImmutableSet.of(
            "id", "name", "key", "code"
      );

      public static NullableHandling fromValue(String v) {
         return valueOf(v.toUpperCase());
      }

      public boolean mustBeNullable(String fieldName) {
         switch (this) {
            case ONLY_KEY: {
               return !KEYS.contains(fieldName);
            }
            case ONLY_ID: {
               return !Objects.equal(fieldName, "id");
            }
            default: {
               return false;
            }
         }
      }

      public boolean maybeNullable(String fieldName) {
         switch (this) {
            case ONLY_KEY:
            case NOT_KEY: {
               if (KEYS.contains(fieldName)) return false;
            }
            case ONLY_ID:
            case NOT_ID: {
               if (Objects.equal(fieldName, "id")) return false;
            }
            default: {
               return true;
            }
         }
      }
   }

   public static enum Format {
      JAXB,
      JSON,
      JSON_SERIALIZE,
      MIXED,
      MINIMAL;

      public static Format fromValue(String v) {
         return valueOf(v.toUpperCase());
      }

      public boolean serializes(Format other) {
         switch (other) {          
            case JSON: {
               return this == JSON_SERIALIZE || this == MIXED;
            }
            case JAXB: {
               return this == JAXB || this == MIXED;
            }
         }
         return false;
      }

      public boolean deserializes(Format other) {
         return this == other || serializes(other);
      }
   }

   private Format format;
   private NullableHandling nullableHandling;

   public ParseOptions(Format format, NullableHandling nullableHandling) {
      this.format = format;
      this.nullableHandling = nullableHandling;
   }

   public Format getFormat() {
      return format;
   }

   public NullableHandling getNullableHandling() {
      return nullableHandling;
   }

   public void setFormat(Format format) {
      this.format = format;
   }

   public void setNullableHandling(NullableHandling nullableHandling) {
      this.nullableHandling = nullableHandling;
   }
}