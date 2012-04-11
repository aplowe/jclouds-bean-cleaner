package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

/**
 */
public class DocsTest extends AbstractTest {

   @Test
   public void testSimpleTypes() throws Exception {
      File packageDir = generateBeans("/org/jclouds/cleanup/docs", "Minimal");
      assertEquals(packageDir.listFiles().length, 1);
   }

}
