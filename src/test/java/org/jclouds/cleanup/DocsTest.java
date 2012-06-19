package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

/**
 */
public class DocsTest extends AbstractTest {

   @Test
   public void testGenerateDoc() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/docs", "mixed", "docs");
      assertEquals(packageDir.listFiles().length, 1);
   }
   @Test
   public void testAsJaxb() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/docs", "jaxb", "test-data/docs/jaxb");
      assertEquals(packageDir.listFiles().length, 1);
   }

   @Test
   public void testAsMinimal() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/docs", "minimal", "test-data/docs/minimal");
      assertEquals(packageDir.listFiles().length, 1);
   }
   
   @Test
   public void testAsJson() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/docs", "json", "test-data/docs/gson");
      assertEquals(packageDir.listFiles().length, 1);
   }

}
