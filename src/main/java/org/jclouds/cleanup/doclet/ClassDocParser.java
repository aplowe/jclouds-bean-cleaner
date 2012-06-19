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
package org.jclouds.cleanup.doclet;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.NullOutputStream;
import com.sun.javadoc.*;
import org.jclouds.cleanup.data.*;
import org.jclouds.cleanup.output.IndentedPrintWriter;
import org.jclouds.util.Strings2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 */
public class ClassDocParser {

   /**
    * Patterns applied to annotations to determine serialized names
    * (always try all of them in case we're moving from one serialisation style or format to another)
    */
   private Map<String, String> serializedNameGrabbers = ImmutableMap.of(
         "Named", "value",
         "SerializedName", "value",
         "JsonProperty", "value",
         "XmlElement", "name",
         "XmlAttribute", "name");
   public static final String[] booleanAccesorNames = {"is", "may", "can", "should", "cannot", "not"};
   private List<String> annotationsToKill = ImmutableList.of("SerializedName", "Named", "Inject", "Nullable");

   private Collection<String> getAnnotations(ProgramElementDoc element) {
      List<String> result = Lists.newArrayList();
      for (AnnotationDesc atree : element.annotations()) {
         String tmp = "@" + atree.annotationType().simpleTypeName();
         if (!annotationsToKill.contains(atree.annotationType().simpleTypeName())) {
            if (atree.elementValues().length == 1 && Objects.equal(atree.elementValues()[0].element().name(), "value")) {
               tmp += "(" + atree.elementValues()[0].value() + ")";
            } else if (atree.elementValues().length > 0) {
               tmp += "(" + Joiner.on(", ").join(atree.elementValues()) + ")";
            }
            result.add(tmp);
         }
      }
      return result;
   }

   protected boolean annotatatedAsNullable(ProgramElementDoc element) {
      boolean shouldBeRequired = false;
      for (AnnotationDesc atree : element.annotations()) {
         if (Objects.equal("Nullable", atree.annotationType().simpleTypeName())) {
            return true;
         }
         if (ImmutableSet.of("XmlElement", "XmlAttribute", "XmlElementRef").contains(atree.annotationType().simpleTypeName())) {
            shouldBeRequired = true;
            for (AnnotationDesc.ElementValuePair evp : atree.elementValues()) {
               if (Objects.equal("required", evp.element().name()) && Objects.equal(Boolean.TRUE, evp.value().value())) {
                  return false;
               }
            }
         }
      }
      // Note: if we've determined that it should be marked as required we return true
      return shouldBeRequired;
   }

   private Collection<String> extractComment(ProgramElementDoc element) {
      return extractComment(null, null, element);
   }

   private Collection<String> extractComment(String defaultCommentText,
                                             Multimap<String, String> defaultTags, ProgramElementDoc... elements) {
      String commentText = null;
      for (ProgramElementDoc element : elements) {
         if (element != null && element.commentText() != null && !element.commentText().trim().isEmpty()) {
            commentText = element.commentText();
            break;
         }
      }

      if (commentText == null) {
         commentText = defaultCommentText;
      }

      Multimap<String, String> tagsToOutput = LinkedListMultimap.create();
      if (defaultTags != null) tagsToOutput.putAll(defaultTags);

      for (ProgramElementDoc element : elements) {
         if (element != null) {
            for (Tag tag : element.tags()) {
               tagsToOutput.put(tag.name(), tag.text());
            }
         }
      }

      if (commentText == null && tagsToOutput.isEmpty()) return ImmutableList.of();

      List<String> result = Lists.newArrayList();

      if (commentText != null) {
         for (String line : commentText.split("\n")) {
            result.add(line.trim());
         }
         if (!tagsToOutput.isEmpty()) {
            result.add("");
         }
      }

      for (Map.Entry<String, String> tag : tagsToOutput.entries()) {
         result.add(tag.getKey() + " " + tag.getValue());
      }

      return result;
   }

   protected String properTypeName(FieldDoc field, Collection<String> imports) {
      return fixupTypeName(field.type(), field.containingPackage(), imports);
   }

   protected String fixupTypeName(Type type, PackageDoc currentPackage, Collection<String> imports) {
      String fieldType = removeUnnecessaryPackages(type, currentPackage, imports);
      if (type.asParameterizedType() != null && type.asParameterizedType().typeArguments().length > 0) {
         fieldType += "<";
         for (Type paramType : type.asParameterizedType().typeArguments()) {
            fieldType += fixupTypeName(paramType, currentPackage, imports) + ", ";
         }
         fieldType = fieldType.substring(0, fieldType.length() - 2);
         fieldType += ">";
      }
      return fieldType;
   }

   protected String removeUnnecessaryPackages(Type type, PackageDoc currentPackage, Collection<String> imports) {
      String fieldType = type.simpleTypeName();
      if (type.isPrimitive() || type.asClassDoc().containingPackage().name().equals("java.lang")) {
      } else if (type.asClassDoc().containingPackage().equals(currentPackage) ||
            imports.contains("import " + type.qualifiedTypeName() + ";") ||
            imports.contains("import " + type.asClassDoc().containingPackage().name() + "*" + ";")) {
         if (type.asClassDoc().containingClass() != null) {
            fieldType = type.asClassDoc().containingClass().simpleTypeName() + "." + fieldType;
         }
      } else {
         fieldType = type.asClassDoc().containingPackage() + "." + fieldType;
      }
      return fieldType;
   }

   public BeanAndSuperClassName parseBean(ClassDoc element, Format format) {
      String superClass = null;
      if (element.superclassType() != null && !Objects.equal(element.superclassType().qualifiedTypeName(), "java.lang.Object")) {
         superClass = element.superclassType().simpleTypeName();
      }

      Bean bean = new Bean(element.containingPackage().toString(), element.isAbstract(), format, element.simpleTypeName(), getAnnotations(element), extractComment("Class " + element.name(), null, element));

      // Process imports
      List<String> lines;
      try {
         // This is actually helpful (oddly!)
         lines = ImmutableList.copyOf(Strings2.toStringAndClose(new FileInputStream(element.position().file())).split("\n"));
      } catch (IOException ex) {
         throw Throwables.propagate(ex);
      }

      bean.addImports(Sets.filter(ImmutableSet.copyOf(lines), new Predicate<String>() {
         @Override
         public boolean apply(String input) {
            return input.trim().startsWith("import ");
         }
      }));

      // TODO this is re-indenting inner classes (sometimes unpleasantly!)
      // Process inner classes
      for (ClassDoc clazz : element.innerClasses()) {
         if (!clazz.simpleTypeName().toLowerCase().endsWith("builder")) {
            List<String> content = Lists.newArrayList();
            IndentedPrintWriter ipw = new IndentedPrintWriter(new NullOutputStream());
            ipw.println("{");
            for (int i = clazz.position().line(); ipw.currentIndent() > 0 && i < lines.size(); i++) {
               ipw.println(lines.get(i));
               content.add(lines.get(i).trim());
            }
            content.remove(content.size() - 1);
            bean.addInnerClass(new InnerClass("public static " + (clazz.isEnum() ? "enum" : "class"), clazz.simpleTypeName(), getAnnotations(clazz), extractComment(clazz), content));
         }
      }

      // Extract these bits of information from the ClassDoc data
      Map<String, String> serializedNames = Maps.newHashMap();
      Set<String> nullableFields = Sets.newHashSet();
      Map<String, MethodDoc> accessors = Maps.newHashMap();

      for (ConstructorDoc constructor : element.constructors()) {
         for (Parameter parameter : constructor.parameters()) {
            for (AnnotationDesc anno : parameter.annotations()) {
               if (Objects.equal(anno.annotationType().typeName(), "Nullable")) {
                  nullableFields.add(parameter.name());
               }
            }
         }
      }

      // Inject/Named
      for (ConstructorDoc constructor : element.constructors()) {
         for (AnnotationDesc anno : constructor.annotations()) {
            if (Objects.equal(anno.annotationType().typeName(), "Inject") ||
                Objects.equal(anno.annotationType().typeName(), "ConstructorProperties")) {
               for (Parameter parameter : constructor.parameters()) {
                  // try to pick-up the associations with fields
                  String serializedName = getSerializedName(parameter.annotations());
                  if (serializedName != null) {
                     serializedNames.put(parameter.name(), serializedName);
                  }
               }
            }
         }
      }

      // ConstructorProperties
      for (ConstructorDoc constructor : element.constructors()) {
         Iterable<String> constructorProperties = null;
         for (AnnotationDesc anno : constructor.annotations()) {
            if (Objects.equal(anno.annotationType().typeName(), "ConstructorProperties")) {
               String stuff = anno.elementValues()[0].value().toString();
               constructorProperties = Splitter.on(",").trimResults(CharMatcher.anyOf("\t\n {}\"")).split(stuff);
               break;
            }
         }

         // Try to map to actual field names...
         if (constructorProperties != null) {
            Iterator<String> it = constructorProperties.iterator();
            for (int i = 0; i < constructor.parameters().length && it.hasNext(); i++) {
               serializedNames.put(constructor.parameters()[i].name(), it.next());
            }
            break;
         }
      }
      
      // Look for accessor and field annotations
      for (FieldDoc field : element.fields()) {
         
         if (!field.isStatic()) {
            String fieldName = field.name();

            if (annotatatedAsNullable(field)) {
               nullableFields.add(fieldName);
            }
            
            // Accessors first
            for (MethodDoc method : element.methods()) {
               if (Objects.equal(method.name(), getAccessorName(field)) ||
                     Objects.equal(method.name(), fieldName)) {
                  accessors.put(fieldName, method);
                  String serializedName = getSerializedName(method.annotations());
                  if (serializedName != null) {
                     serializedNames.put(fieldName, serializedName);
                  }
               }
            }
            
            // Fields
            String serializedName = getSerializedName(field.annotations());
            if (serializedName != null) {
               serializedNames.put(fieldName, serializedName);
            }
         }
      }

      // Construct the fields
      for (FieldDoc field : element.fields()) {
         if (field.isStatic()) {
            bean.addClassField(new ClassField(field.name(), properTypeName(field, bean.rawImports()), getAnnotations(field), extractComment(null, ImmutableMultimap.<String, String>of(), field)));
         } else {
            // Note we need to pick up any stray comments or annotations on accessors
            InstanceField instanceField = new InstanceField(field.name(),
                  serializedNames.get(field.name()),
                  getAccessorName(field), properTypeName(field, bean.rawImports()),
                  nullableFields.contains(field.name()),
                  getAnnotations(field),
                  extractComment(null, ImmutableMultimap.<String, String>of(), field, accessors.get(field.name())));
            bean.addInstanceField(instanceField);
         }
      }

      return new BeanAndSuperClassName(bean, superClass);
   }

   public static String getAccessorName(FieldDoc field) {
      String name = field.name();
      String fieldNameUC = name.substring(0, 1).toUpperCase() + name.substring(1);
      String getterName = null;
      if (Objects.equal(field.type().simpleTypeName().toLowerCase(), "boolean")) {
         for (String starter : booleanAccesorNames) {
            if (name.startsWith(starter)) {
               getterName = name;
            }
         }
         if (getterName == null) {
            getterName = "is" + fieldNameUC;
         }
      } else {
         getterName = "get" + fieldNameUC;
      }
      return getterName;
   }

   private String getSerializedName(AnnotationDesc... annotationDescs) {
      for (AnnotationDesc anno : annotationDescs) {
         if (serializedNameGrabbers.containsKey(anno.annotationType().simpleTypeName())) {
            for (AnnotationDesc.ElementValuePair pair : anno.elementValues()) {
               if (Objects.equal(serializedNameGrabbers.get(anno.annotationType().simpleTypeName()), pair.element().name())) {
                  return (String) pair.value().value();
               }
            }
         }
      }
      return null;
   }

}
