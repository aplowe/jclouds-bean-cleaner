Utility for re-writing Java domain objects
==========================================

Configured to output http://jclouds.org beans, usage is as follows::

java -jar cleaner-1.0.jar -format [format] [sourcepath] [classpath]

Where:

- [format] is one of json (default), jaxb, json_serialize (json plus @Named fields for serialization), mixed (json_serialize & jaxb) or minimal
- [sourcepath] points to the folder containing the source for your beans
- [classpath] is something to be added to the classpath of the run (e.g. target/classes or "target/classes:../somewhere").
   
Examples of each Format
-----------------------

- [JSON](https://github.com/aplowe/jclouds-bean-cleaner/blob/master/JSON.md)
- [JSON with serialization annotations](https://github.com/aplowe/jclouds-bean-cleaner/blob/master/JSON-S.md)
- [JAXB](https://github.com/aplowe/jclouds-bean-cleaner/blob/master/JAXB.md)
- [Mixed (JSON & JAXB)](https://github.com/aplowe/jclouds-bean-cleaner/blob/master/MIXED.md)

NOTES
-----

- regrettably, if classpath is incorrect the results will have Object references in place of the expected class.
- for mixed mode you need to provide the jaxb annotations in the input (the jclouds json annotations will be added)
- it is generally possible to cycle through the usual forms of beans, picking up @Named, @SerializedName,
@XmlElement, etc. to determine serialized names (for 'mixed' this is helpful)
- in the case of @ConstructorProperties on a constructor and no field annotations the cleaner will try to work things
out (assuming the parameters to the constructor match the field names!)

RUNNING THE CLEANER
-------------------

To regenerate the beans for nova on my laptop I would do as follows::

    Adam-Lowes-MacBook-Pro:nova aplowe$ java -jar ~/src/jclouds-bean-cleaner/target/cleaner-1.0.jar -format json_serialize src/main/java/org/jclouds/openstack/nova/domain target/classes/
    INFO: Loading source file /Users/aplowe/src/jclouds-aplowe2/apis/nova/src/main/java/org/jclouds/openstack/nova/domain/AbsoluteLimit.java...
    ...
    INFO: Processing Server writing to /Users/aplowe/src/jclouds-aplowe2/apis/nova/target/generated-sources/cleanbeans/org/jclouds/openstack/nova/domain/Server.java
    INFO: Javadoc returned successfully
    INFO: You passed the following arguments: -format json_serialize src/main/java/org/jclouds/openstack/nova/domain target/classes/
    Adam-Lowes-MacBook-Pro:nova aplowe$ 

License
-------
Copyright (C) 2012-2013 Cloudsoft Corporation.

Licensed under the Apache License, Version 2.0