package org.jclouds.cleanup;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

/**
 */
public class VCloudTest extends AbstractTest {

    @Test
    public void testSimpleTypes() throws Exception {
        File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "Jaxb");
        assertEquals(packageDir.listFiles().length, 9);
    }

}
