package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

/**
 */
public class VCloudTest extends AbstractTest {

    @Test
    public void testSimpleTypes() throws Exception {
        File packageDir = generateBeans("/org/jclouds/vcloud/director/v1_5/domain", "Jaxb");
        assertEquals(packageDir.listFiles().length, 10);
    }

}
