Utility for re-writing Java domain objects.

Configured to output http://jclouds.org beans, usage is as follows::

java -jar cleaner-1.0.jar <sourcepath> <classpath> -format <format>

Where:

- <format> is one of Gson, Jaxb or Minimal
- <sourcepath> points to the folder containing the source for your beans
- <classpath> is something to be added to the classpath of the run (e.g. target/classes or "target/classes:../somewhere").
   
NOTE: regrettably, if classpath is incorrect the results will have Object references in place of the expected class.

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
    
    import com.google.common.collect.ImmutableList;
    import com.google.common.collect.ImmutableMap;
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
    
          private String name;
          private String id;
          private String description;
       
          /** 
           * @see Example#getName()
           */
          public T name(String name) {
             this.name = name;
             return self();
          }
    
          /** 
           * @see Example#getId()
           */
          public T id(String id) {
             this.id = id;
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
             return new Example(this);
          }
          
          public T fromExample(Example in) {
             return this
                .name(in.getName())
                .id(in.getId())
                .description(in.getDescription())
                ;
          }
       }
    
       private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
          @Override
          protected ConcreteBuilder self() {
             return this;
          }
       }
    
       @XmlAttribute(required=true)
       private final String name;
       @XmlAttribute(required=true)
       private final String id;
       @XmlAttribute
       private final String description;
    
       protected Example(Builder<?> builder) {
          this.name = checkNotNull(builder.name, "name");
          this.id = checkNotNull(builder.id, "id");
          this.description = builder.description; 
       }
    
       protected Example() {
          // for GSON
          this.name = null;
          this.id = null;
          this.description = null;
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
             && Objects.equal(this.description, that.description)
             ;
       }
       
       protected ToStringHelper string() {
          return Objects.toStringHelper("")
             .add("name", name)
             .add("id", id)
             .add("description", description)
             ;
       }
       
       @Override
       public String toString() {
          return string().toString();
       }
    
    }

