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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A POJO bean we are cleaning up!
 */
public class Bean extends BaseObject {
   // Used to re-order imports
   private static final List<String> ECLIPSE_INPUT_ORDER = ImmutableList.of("import static", "import java.", "import javax.", "import org.", "import ");
   private static final Set<String> DEFAULT_IMPORTS =
        // TODO remove unused imports at output (for now have optimise imports)
        ImmutableSet.of(
               "import static com.google.common.base.Preconditions.checkNotNull;",
               "import java.beans.ConstructorProperties;", // for gson only
               "import javax.inject.Named;",  // for gson only
               "import org.jclouds.javax.annotation.Nullable;",
               "import com.google.common.collect.ImmutableList;",
               "import com.google.common.collect.ImmutableMap;",
               "import com.google.common.collect.ImmutableMultimap;",
               "import com.google.common.collect.ImmutableSet;", 
               "import com.google.common.collect.Sets;", // for jaxb only
               "import com.google.common.base.Objects;", 
               "import com.google.common.base.Objects.ToStringHelper;"
         );
   protected final Set<String> imports = Sets.newHashSet(DEFAULT_IMPORTS);
   private final List<InstanceField> instanceFields = Lists.newArrayList();
   private final List<ClassField> staticFields = Lists.newArrayList();
   private final List<InnerClass> innerClasses = Lists.newArrayList();
   private final String packageName;
   private final Bean superClass;
   private final boolean isAbstract;
   private final ParseOptions options;

   public Bean(Bean superClass, String packageName, boolean isAbstract, ParseOptions options, String type, Collection<String> annotations, Collection<String> javadocComment) {
      super(type, annotations, javadocComment);
      this.superClass = superClass;
      this.packageName = packageName;
      this.isAbstract = isAbstract;
      this.options = options;
   }

   public void addInstanceField(InstanceField field) {
      instanceFields.add(field);
   }

   public void addClassField(ClassField field) {
      staticFields.add(field);
   }

   public void addImports(Collection<String> imports) {
      this.imports.addAll(imports);
   }

   public void addInnerClass(InnerClass innerClass) {
      this.innerClasses.add(innerClass);
   }

   public Collection<String> rawImports() {
      return imports;
   }

   // TODO this should probably be taken care of during parsing, not in this bean!
   public Collection<String> getImports() {
      List<String> result = Lists.newArrayList();
      for (String prefix : ECLIPSE_INPUT_ORDER) {
         boolean needGap = false;
         Iterator<String> importIt = imports.iterator();
         while (importIt.hasNext()) {
            String importLine = importIt.next();
            if (importLine.startsWith(prefix)) {
               result.add(importLine);
               importIt.remove();
               needGap = true;
            }
         }
         if (needGap) result.add("");
      }
      return result;
   }

   public String getPackageName() {
      return packageName;
   }

   /**
    * @return the fields of this class (excluding those inherited)
    */
   public List<InstanceField> getInstanceFields() {
      return instanceFields;
   }

   public InstanceField getInstanceField(String name) {
      for (InstanceField field : getInstanceFields()) {
         if (Objects.equal(name, field.getName())) {
            return field;
         }
      }
      return null;
   }

   public List<ClassField> getStaticFields() {
      return staticFields;
   }

   public List<InnerClass> getInnerClasses() {
      return innerClasses;
   }

   public Bean getSuperClass() {
      return superClass;
   }

   public String getSuperClassName() {
      return superClass == null ? null : superClass.getType();
   }

   public boolean isAbstract() {
      return isAbstract;
   }

   // Convenience methods!
   public boolean isJaxb() {
      return options.getFormat().deserializes(ParseOptions.Format.JAXB);
   }
   
   public boolean isJson() {
      return options.getFormat().deserializes(ParseOptions.Format.JSON);
   }

   public boolean isJsonSerialize() {
      return options.getFormat().serializes(ParseOptions.Format.JSON);
   }

   public boolean isSubclass() {
      return superClass != null;
   }

   /**
    * @return all the fields of this class, in descending order (super-first)
    */
   public List<InstanceField> getAllFields() {
      List<InstanceField> result = Lists.newArrayList();
      result.addAll(getSuperFields());
      result.addAll(getInstanceFields());
      return result;
   }

   /**
    * @return the fields of this class inherited from superclasses, in descending order (super-first)
    */
   public List<InstanceField> getSuperFields() {
      List<InstanceField> result = Lists.newArrayList();
      List<Bean> classes = Lists.newArrayList();
      for (Bean current = getSuperClass(); current != null; current = current.getSuperClass()) {
         classes.add(current);
      }
      for (Bean current : Lists.reverse(classes)) {
         result.addAll(current.getInstanceFields());
      }
      return result;
   }
   
   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("type", type).toString();
   }
}
