[#ftl]
[#macro namedanno field][#if field.nullable]@Nullable [/#if][/#macro]
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
public [#if abstract]abstract [/#if]class ${type} [#if subclass]extends ${superClassName} [/#if]{

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
   public static abstract class Builder<T extends Builder<T>> [#if subclass]extends ${superClassName}.Builder<T> [/#if] {
   [#if ! subclass]
      protected abstract T self();

   [/#if]
   [#-- Print builder fields --]
   [#list instanceFields![] as field]
      [#if field.set]
      protected ${field.type} ${field.name} = ImmutableSet.of();
      [#elseif field.list]
      protected ${field.type} ${field.name} = ImmutableList.of();
      [#elseif field.map]
      protected ${field.type} ${field.name} = ImmutableMap.of();
      [#elseif field.multimap]
      protected ${field.type} ${field.name} = ImmutableMultimap.of();
      [#else]
      protected ${field.type} ${field.name};
      [/#if]
   [/#list]
   
   [#-- Print setters --]
   [#list instanceFields![] as field]
      /** 
       * @see ${type}#${field.accessorName}()
       */
      public T ${field.name}(${field.type} ${field.name}) {
         [#if field.set]
         this.${field.name} = ImmutableSet.copyOf(checkNotNull(${field.name}, "${field.name}"));      
         [#elseif field.list]
         this.${field.name} = ImmutableList.copyOf(checkNotNull(${field.name}, "${field.name}"));     
         [#elseif field.map]
         this.${field.name} = ImmutableMap.copyOf(checkNotNull(${field.name}, "${field.name}"));     
         [#elseif field.multimap]
         this.${field.name} = ImmutableMultimap.copyOf(checkNotNull(${field.name}, "${field.name}"));     
         [#elseif field.nullable]
         this.${field.name} = checkNotNull(${field.name}, "${field.name}");
         [#else]
         this.${field.name} = ${field.name};
         [/#if]
         return self();
      }

      [#if field.set]
      public T ${field.name}(${field.parameterType}... in) {
         return ${field.name}(ImmutableSet.copyOf(in));
      }

      [#elseif field.list]
      public T ${field.name}(${field.parameterType}... in) {
         return ${field.name}(ImmutableList.copyOf(in));
      }

      [/#if]
[/#list]
      public ${type} build() {
         return new ${type}(${allFields[0].name}[#if allFields?size > 1][#list allFields[1..] as field], ${field.name}[/#list][/#if]);
      }
      
      public T from${type}(${type} in) {
         return [#if subclass]super.from${superClassName}(in)[#else]this[/#if][#list instanceFields![] as field]
                  .${field.name}(in.${field.accessorName}())[/#list];
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
[#if jaxb]
   [#list field.annotations![] as anno]
   ${anno}
   [/#list]
   [#if field.set]
   private ${field.type} ${field.name} = Sets.newLinkedHashSet(); // maintaining order
   [#else]
   private ${field.type} ${field.name};
   [/#if]
   [#else]
   [#if gson && field.name != field.serializedName]
   @Named("${field.serializedName}")
   [/#if]
   private final ${field.type} ${field.name};
   [/#if]
[/#list]

   [#-- Print constructors --]   
   [#if gson]
   @ConstructorProperties({
      "${allFields[0].serializedName}"[#if allFields?size > 1][#list allFields[1..] as field], "${field.serializedName}"[/#list][/#if]
   })
   [#else]
   [/#if]
   protected ${type}([@namedanno field=allFields[0] /]${allFields[0].type} ${allFields[0].name}[#if allFields?size > 1][#list allFields[1..] as field], [@namedanno field=field /]${field.type} ${field.name}[/#list][/#if]) {
   [#if subclass]
      super(${superFields[0].name}[#if superFields?size > 1][#list superFields[1..] as field], ${field.name}[/#list][/#if]);
   [/#if]
   [#list instanceFields![] as field]
      [#if field.nullable]
      [#if field.set]
      this.${field.name} = ${field.name} == null ? ImmutableSet.<${field.parameterType}>of() : ImmutableSet.copyOf(${field.name});      
      [#elseif field.list]
      this.${field.name} = ${field.name} == null ? ImmutableList.<${field.parameterType}>of() : ImmutableList.copyOf(${field.name});      
      [#elseif field.map]
      this.${field.name} = ${field.name} == null ? ImmutableMap.<${field.parameterType}>of() : ImmutableMap.copyOf(${field.name});      
      [#elseif field.multimap]
      this.${field.name} = ${field.name} == null ? ImmutableMultimap.<${field.parameterType}>of() : ImmutableMultimap.copyOf(${field.name});      
      [#else]
      this.${field.name} = ${field.name};
      [/#if]      
      [#else]
      [#if field.primative]
      this.${field.name} = ${field.name};
      [#elseif field.set]
      this.${field.name} = ImmutableSet.copyOf(checkNotNull(${field.name}, "${field.name}"));      
      [#elseif field.list]
      this.${field.name} = ImmutableList.copyOf(checkNotNull(${field.name}, "${field.name}"));     
      [#elseif field.map]
      this.${field.name} = ImmutableMap.copyOf(checkNotNull(${field.name}, "${field.name}"));     
      [#elseif field.multimap]
      this.${field.name} = ImmutableMultimap.copyOf(checkNotNull(${field.name}, "${field.name}"));     
      [#else]
      this.${field.name} = checkNotNull(${field.name}, "${field.name}");
      [/#if]
      [/#if]
   [/#list]
   }

   [#if jaxb]
   protected ${type}() {
      // for JAXB
      [#list instanceFields![] as field]
      [#if field.optional]
      ${field.name} = Optional.absent(); // work-around
      [/#if]
      [/#list]
   }

   [/#if]
[#-- Print accessors --]
[#list instanceFields![] as field]
   [#if field.javadocComment?size > 0 ]
   /**
    [#list field.javadocComment as line]
    * ${line}
    [/#list]
    */
   [/#if]
   [#if field.nullable && !field.set && !field.list && !field.map && !field.multimap]
   @Nullable
   [/#if]
   public ${field.type} ${field.accessorName}() {
      return this.${field.name};
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
      return [#if subclass]super.equals(that) && [/#if]Objects.equal(this.${instanceFields[0].name}, that.${instanceFields[0].name})[#if instanceFields?size > 1][#list instanceFields[1..] as field]
               && Objects.equal(this.${field.name}, that.${field.name})[/#list][/#if];
   }
   
   protected ToStringHelper string() {
      return [#if subclass]super.string()[#else]Objects.toStringHelper("")[/#if]
            [#list instanceFields as field].add("${field.name}", ${field.name})[/#list];
   }
   
   [#if ! subclass]
   @Override
   public String toString() {
      return string().toString();
   }

   [/#if]
[/#if]
}
