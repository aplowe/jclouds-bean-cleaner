package org.jclouds.cleanup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;

import com.google.common.base.Splitter;

/**
 */
public abstract class AbstractTest {
   /**
    * Hiding away some hookiness that locates target based upon class.getResource().getFile()!
    */
   protected File generateBeans(String packagePath) throws Exception {
      // Our classes are in the classpath!
      File f = new File(AbstractTest.class.getResource(packagePath).getFile());
      File classesDir = f.getParentFile();
      assertTrue(classesDir.exists(), "Lost the compiled files at path " + packagePath + " - expected something in target/test-classes or similar!");
      File targetDir = f;
      for (String x : Splitter.on("/").split(packagePath)) {
         targetDir = targetDir.getParentFile();
      }
      assertTrue(targetDir.exists());
      assertEquals(targetDir.getName(), "target");
      File generatedDir = new File(targetDir, "generated-sources/cleanbeans");
      File simpleTestPackage = new File(generatedDir, packagePath);
      
      // Be careful to clean up first!
      if (simpleTestPackage.exists()) {
         for (File file : simpleTestPackage.listFiles()) {
            assertTrue(file.delete());
         }
         assertEquals(simpleTestPackage.listFiles().length, 0);
      }

      DomainObjectDocletCleaner.main(new String[]{new File(targetDir.getParent(), "src/test/java" + packagePath).getAbsolutePath(), classesDir.getAbsolutePath(), "-d", generatedDir.getAbsolutePath(), "-jaxb"});

      return simpleTestPackage;
   }
}
