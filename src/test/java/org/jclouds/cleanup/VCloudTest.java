package org.jclouds.cleanup;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

/**
 */
public class VCloudTest extends AbstractTest {

    @Test
    public void testJaxb() throws Exception {
        File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "Jaxb", "test-data/vcloud");
        assertEquals(packageDir.listFiles().length, 9);
    }

   @Test
   public void testJson() throws Exception {
      File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "json", "test-data/vcloud-json");
      assertEquals(packageDir.listFiles().length, 9);
   }

   @Test
   public void testJsonIdsNotNull() throws Exception {
      File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "json", "not_id", "test-data/vcloud-json-id");
      assertEquals(packageDir.listFiles().length, 9);
   }

   @Test
   public void testJsonKeysNotNull() throws Exception {
      File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "json", "not_key", "test-data/vcloud-json-key");
      assertEquals(packageDir.listFiles().length, 9);
   }
   
   @Test
   public void testJsonOnlyIdsNotNull() throws Exception {
      File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "json", "only_id", "test-data/vcloud-json-only-id");
      assertEquals(packageDir.listFiles().length, 9);
   }

   @Test
   public void testJsonOnlyKeysNotNull() throws Exception {
      File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "json", "only_key", "test-data/vcloud-json-only-key");
      assertEquals(packageDir.listFiles().length, 9);
   }

}
