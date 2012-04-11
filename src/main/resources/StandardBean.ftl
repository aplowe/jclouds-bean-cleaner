[#ftl]
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
package ${packageName};

[#list imports as import]
${import}
[/#list]
/**
 [#list javadocComment as line]
 * ${line}
 [/#list]
*/
[#list annotations as anno]
${anno}
[/#list]
public [#if abstract]abstract [/#if]class ${type} [#if subclass]extends ${superClass} [/#if]{

[#list innerClasses![] as inc] 
   /**
    [#list inc.javadocComment as line]
    * ${line}
    [/#list]
    */
   [#list inc.annotations as anno]
   ${anno}
   [/#list]
   ${inc.modifiers} ${inc.type} {
      [#list inc.contents as line]
      ${line}
      [/#list]
   }

[/#list]
[#list classFields![] as field]
   public static ${field.javaType} ${field.name};

[/#list]
   [#-- Builder --]
   [#if ! abstract]
   public static Builder<?> builder() { 
      return new ConcreteBuilder();
   }
   
   public Builder<?> toBuilder() { 
      return new ConcreteBuilder().from${type}(this);
   }

   [/#if]   
   public static abstract class Builder<T extends Builder<T>> [#if subclass]extends ${superClass}.Builder<T> [/#if] {
   [#if ! subclass]
      protected abstract T self();

   [/#if]
   [#-- Print fields --]
   [#list instanceFields![] as field]
      [#if field.set]
      private ${field.type} ${field.name} = Sets.newLinkedHashSet();
      [#elseif field.list]
      private ${field.type} $field.name = Lists.newArrayList();
      [#else]
      private ${field.type} ${field.name};
      [/#if]
   [/#list]
   
   [#-- Print setters --]
   [#list instanceFields![] as field]
      public T ${field.name}(${field.type} ${field.name}) {
         this.${field.name} = ${field.name};
         return self();
      }

   [/#list]
      public ${type} build() {
         return new ${type}(this);
      }
      
      public T from${type}(${type} in) {
         return [#if subclass]super.from${superClass}(in)[#else]this[/#if]
[#list instanceFields![] as field]
            .${field.name}(in.${field.accessorName}())
[/#list]            ;
      }

   }

   [#-- Concrete builder --]
   [#if ! abstract]
   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }

   [/#if]
[#-- Print fields --]
[#list instanceFields![] as field]
   [#list field.annotations![] as anno]
   ${anno}
   [/#list]
   private final ${field.type} ${field.name};
[/#list]

   [#-- Print constructor --]
   protected ${type}(Builder<?> builder) {
   [#if subclass]
      super(builder);
   [/#if]
   [#list instanceFields![] as field]
      [#if field.set]
      this.${field.name} = ImmutableSet.copyOf(checkNotNull(builder.${field.name}, "${field.name}"));      
      [#elseif field.list]
      this.${field.name} = ImmutableList.copyOf(checkNotNull(builder.${field.name}, "${field.name}"));     
      [#else]
      this.${field.name} =[#if field.nullable] builder.${field.name}; [#else] checkNotNull(builder.${field.name}, "${field.name}");[/#if]
      [/#if]
   [/#list]
   }

[#-- Print accessors --]
[#list instanceFields![] as field]
   /**
    [#list field.javadocComment as line]
    * ${line}
    [/#list]
    */
   [#if field.nullable]
   @Nullable
   [/#if]
   public ${field.type} ${field.accessorName}() {
      [#if field.set]
      return Collections.unmodifiableSet(this.${field.name});
      [#elseif field.list]
      return Collections.unmodifiableList(this.${field.name});
      [#else]
      return this.${field.name};
      [/#if]
   }

[/#list]
[#if instanceFields?has_content]
   @Override
   public int hashCode() {
      return Objects.hashCode(${instanceFields[0].name}[#if instanceFields?size > 1][#list instanceFields[1..] as field], ${field.name}[/#list][/#if]);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      ${type} that = ${type}.class.cast(obj);
      return Objects.equal(this.${instanceFields[0].name}, that.${instanceFields[0].name})
      [#if instanceFields?size > 1]
      [#list instanceFields[1..] as field]
         && Objects.equal(this.${field.name}, that.${field.name})
      [/#list]
      [/#if]
         ;
   }
   
   protected ToStringHelper string() {
      return [#if subclass]super.string()[#else]Objects.toStringHelper("")[/#if]
[#list instanceFields as field]
         .add("${field.name}", ${field.name})
[/#list]         ;
   }
   
   [#if ! subclass]
   @Override
   public String toString() {
      return string().toString();
   }

   [/#if]
[/#if]
}