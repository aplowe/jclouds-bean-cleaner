package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

/**
 */
public class SimpleTypeTest extends AbstractTest {

   @Test
   public void testSimpleTypes() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/simpletest", "mixed", "test-data/simple/mixed");
      assertEquals(packageDir.listFiles().length, 5);
   }

   @Test
   public void testSimpleTypesAsJaxb() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/simpletest", "jaxb", "test-data/simple/jaxb");
      assertEquals(packageDir.listFiles().length, 5);
   }
   @Test
   public void testSimpleTypesAsJson() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/simpletest", "json", "test-data/simple/json");
      assertEquals(packageDir.listFiles().length, 5);
   }
   @Test
   public void testSimpleTypesAsMinimal() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/simpletest", "minimal", "test-data/simple/minimal");
      assertEquals(packageDir.listFiles().length, 5);
   }
}
