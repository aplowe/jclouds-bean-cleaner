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
package org.jclouds.cleanup.data;

/**
 * Tuple for a bean and the name of its superclass
 *
 * @author Adam Lowe
 */
public class BeanAndSuperClassName {
   private Bean bean;
   private String superClassName;

   public BeanAndSuperClassName(Bean bean, String superClassName) {
      this.bean = bean;
      this.superClassName = superClassName;
   }

   public Bean getBean() {
      return bean;
   }

   public String getSuperClassName() {
      return superClassName;
   }
}