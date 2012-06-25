[#ftl]
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
[#-- ^^ ABOVE COPY-AND-PASTED FROM Bean.ftl ^^ --]

[#-- Print fields --]
[#list instanceFields![] as field]
   /**
    [#list field.javadocComment as line]
    * ${line}
    [/#list]
    */
   [#list field.annotations![] as anno]
   ${anno}
   [/#list]
   [#if field.nullable]
   @Nullable
   [/#if]
   private ${field.type} ${field.name};
[/#list]

}