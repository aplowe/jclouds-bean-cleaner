MIXED FORMAT EXAMPLE
--------------------

To generate 'mixed' format beans (with both JAXB and JSON annotations), it is best to start with classes with JAXB annotations.
These can be written short-hand as follows::

    package org.jclouds.cleanup.docs;
    
    import javax.xml.bind.annotation.XmlAttribute;
    
    /**
    * Class comment for Docs example
    * A few lines....
    * Long
    */
    public class Example {
     /** @return name is required */
     @XmlAttribute(required=true)
     String name;
    
     /** @return attribute is not null */
     @XmlAttribute(required=true)
     String id;
    
     /** Description of the example object */
     @XmlAttribute
     String description;
    }

This can be converted into a full bean with accessors, to which the field comments will be added, a Builder, etc. The 'mixed'
output for the above is as follows::

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
    package org.jclouds.cleanup.docs;
    
    import static com.google.common.base.Preconditions.checkNotNull;
    
    import java.beans.ConstructorProperties;
    
    import javax.xml.bind.annotation.XmlAttribute;
    import javax.inject.Named;
    
    import org.jclouds.javax.annotation.Nullable;
    
    import com.google.common.collect.ImmutableMultimap;
    import com.google.common.collect.ImmutableList;
    import com.google.common.collect.ImmutableMap;
    import com.google.common.collect.Sets;
    import com.google.common.collect.ImmutableSet;
    import com.google.common.base.Objects;
    import com.google.common.base.Objects.ToStringHelper;
    
    /**
    * Class comment for Docs example
    * A few lines....
    * Long
    */
    public class Example {
    
      public static Builder<?> builder() { 
         return new ConcreteBuilder();
      }
      
      public Builder<?> toBuilder() { 
         return new ConcreteBuilder().fromExample(this);
      }
    
      public static abstract class Builder<T extends Builder<T>>  {
         protected abstract T self();
    
         protected String name;
         protected String id;
         protected String description;
      
         /** 
          * @see Example#getName()
          */
         public T name(String name) {
            this.name = checkNotNull(name, "name");
            return self();
         }
    
         /** 
          * @see Example#getId()
          */
         public T id(String id) {
            this.id = checkNotNull(id, "id");
            return self();
         }
    
         /** 
          * @see Example#getDescription()
          */
         public T description(String description) {
            this.description = description; 
            return self();
         }
    
         public Example build() {
            return new Example(name, id, description);
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
    
      @ConstructorProperties({
         "name", "id", "description"
      })
      protected Example(String name, String id, @Nullable String description) {
         this.name = checkNotNull(name, "name");
         this.id = checkNotNull(id, "id");
         this.description = description;
      }
    
      protected Example() {
         // for JAXB
      }
    
      /**
       * @return name is required
       */
      public String getName() {
         return this.name;
      }
    
      /**
       * @return attribute is not null
       */
      public String getId() {
         return this.id;
      }
    
      /**
       * Description of the example object
       */
      @Nullable
      public String getDescription() {
         return this.description;
      }
    
      @Override
      public int hashCode() {
         return Objects.hashCode(name, id, description);
      }
    
      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null || getClass() != obj.getClass()) return false;
         Example that = Example.class.cast(obj);
         return Objects.equal(this.name, that.name)
                  && Objects.equal(this.id, that.id)
                  && Objects.equal(this.description, that.description);
      }
      
      protected ToStringHelper string() {
         return Objects.toStringHelper("")
               .add("name", name).add("id", id).add("description", description);
      }
      
      @Override
      public String toString() {
         return string().toString();
      }
    
    }