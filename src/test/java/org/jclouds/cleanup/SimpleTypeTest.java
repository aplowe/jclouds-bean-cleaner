package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

/**
 */
public class SimpleTypeTest extends AbstractTest {

   @Test
   public void testSimpleTypes() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/simpletest");
      assertEquals(packageDir.listFiles().length, 4);
   }

}
