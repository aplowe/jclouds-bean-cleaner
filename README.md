Utility for re-writing Java domain objects.

Configured to output http://jclouds.org beans.

For example, a JAXB bean that has been written short-hand as follows:

    package org.jclouds.cleanup.docs;
    
    import javax.xml.bind.annotation.XmlAttribute;
    
    /**
     * Class comment for Docs example
     * A few lines....
     * Long
     */
    public class Example<T extends Example<T>> {
        /** @return name is required */
        @XmlAttribute(required=true)
        protected String name;
    
        /** @return attribute is not null */
        @XmlAttribute(required=true)
        protected String id;
    
        /** Description of the example object */
        @XmlAttribute
        protected String description;
    }

Can be converted into a full bean with accessors, to which the field comments will be added, and a Builder:

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
    package org.jclouds.cleanup.docs;
    
    import static com.google.common.base.Preconditions.checkNotNull;
    
    import java.util.Collections;
    
    import javax.xml.bind.annotation.XmlAttribute;
    
    import org.jclouds.javax.annotation.Nullable;
    
    import com.google.common.base.Objects.ToStringHelper;
    import com.google.common.base.Objects;
    import com.google.common.collect.ImmutableCollection;
    import com.google.common.collect.ImmutableList;
    import com.google.common.collect.ImmutableSet;
    import com.google.common.collect.Lists;
    import com.google.common.collect.Sets;
    
    /**
     * Class comment for Docs example
     *  A few lines....
     *  Long
     */
    public class Example {
       public static Builder<?> builder() {
          return new ConcreteBuilder();
       }
       
       public Builder<?> toBuilder() {
          return new ConcreteBuilder().fromExample(this);
       }
       
       public static abstract class Builder<T extends Builder<T>> {
          private String name;
          private String id;
          private String description;
          
          protected abstract T self();
          
          /**
           * @see Example#getName()
           **/
          public T name(String name) {
             this.name = name;
             return self();
          }
          
          /**
           * @see Example#getId()
           **/
          public T id(String id) {
             this.id = id;
             return self();
          }
          
          /**
           * @see Example#getDescription()
           **/
          public T description(String description) {
             this.description = description;
             return self();
          }
          
          public Example build() {
             return new Example(this);
          }
          
          public T fromExample(Example in) {
             return this
                   .name(in.getName())
                   .id(in.getId())
                   .description(in.getDescription());
          }
       }
       
       private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
          @Override
          protected ConcreteBuilder self() {
             return this;
          }
       }
       
       @XmlAttribute(required=true)
       private String name;
       @XmlAttribute(required=true)
       private String id;
       @XmlAttribute
       private String description;
       
       protected Example(Builder<?> b) {
          this.name = b.name;
          this.id = b.id;
          this.description = b.description;
       }
       
       protected Example() {
          // for JAXB
       }
       
       /**
        * @return name is required
        */
       public String getName() {
          return name;
       }
       
       /**
        * @return attribute is not null
        */
       public String getId() {
          return id;
       }
       
       /**
        * Description of the example object
        *
        * @return the description property or null if not set.
        */
       @Nullable
       public String getDescription() {
          return description;
       }
       
       @Override
       public int hashCode() {
          return Objects.hashCode(name, id, description);
       }
       
       @Override
       public boolean equals(Object obj) {
          if (this == obj) return true;
          if (obj == null) return false;
          if (getClass() != obj.getClass()) return false;
          Example other = (Example) obj;
          return Objects.equal(name, other.name)
                && Objects.equal(id, other.id)
                && Objects.equal(description, other.description);
       }
       
       @Override
       public String toString() {
          return string().toString();
       }
       
       protected ToStringHelper string() {
          return Objects.toStringHelper("")
                .add("name", name)
                .add("id", id)
                .add("description", description);
       }
    }

