package org.jclouds.cleanup;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.PatternFilenameFilter;
import com.sun.javadoc.*;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jclouds.cleanup.data.Bean;
import org.jclouds.cleanup.doclet.ClassDocParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Interrogates the class structure using the doclet api and extracts the comments immediately preceding classes, fields
 * and methods as required.
 *
 * @see <a href=
 *      "http://docs.oracle.com/javase/6/docs/technotes/guides/javadoc/doclet/overview.html"
 *      />
 */
public class DomainObjectDocletCleaner extends Doclet {
   private static String outputPath = "target/generated-sources/cleanbeans";
   private static String format = "Gson";
   private static Configuration cfg = new Configuration();

   /**
    * Bootstrapping javadoc application to save users having to remember all the arguments.
    *
    * @param args the path to locate the source code and the path to the compiled classes
    * @throws IOException          if there are problems traversing the source code hierarchy
    * @throws InterruptedException if the spawned javadoc process is interrupted
    */
   public static void main(String[] args) throws IOException, InterruptedException {
      String sourcePath = args[0];
      String classPath = args[1];

      List<String> command = Lists.newArrayList("javadoc",
            "-classpath", System.getProperty("java.class.path") + ":" + classPath,
            "-docletpath", System.getProperty("java.class.path"),
            "-private",
            "-doclet", DomainObjectDocletCleaner.class.getCanonicalName()
      );

      if (args.length > 2) {
         Collections.addAll(command, Arrays.copyOfRange(args, 2, args.length));
      }

      command.addAll(listFileNames(new File(sourcePath)));

      Process process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));

      StreamGobbler in = new StreamGobbler(process.getInputStream(), "INFO");
      StreamGobbler err = new StreamGobbler(process.getErrorStream(), "ERROR");

      in.start();
      err.start();

      if (process.waitFor() == 0) {
         System.out.println("Javadoc returned successfully");
      } else {
         System.out.println("Javadoc returned an error code");
         System.out.println("You passed the following arguments: " + Joiner.on(" ").join(args));
      }
   }

   /**
    * @see com.sun.javadoc.Doclet#languageVersion()
    */
   @SuppressWarnings("unused")
   public static LanguageVersion languageVersion() {
      return LanguageVersion.JAVA_1_5;
   }

   /**
    * Adding -d option for output path and -format for output format
    *
    * @see com.sun.javadoc.Doclet#optionLength(String)
    */
   @SuppressWarnings("unused")
   public static int optionLength(String option) {
      if (Objects.equal(option, "-d")) return 2;
      if (Objects.equal(option, "-format")) return 2;
      return Doclet.optionLength(option);
   }

   /**
    * Adding -d option for output path and -format for output format
    *
    * @see com.sun.javadoc.Doclet#validOptions(String[][], com.sun.javadoc.DocErrorReporter)
    */
   @SuppressWarnings("unused")
   public static boolean validOptions(String[][] options, DocErrorReporter reporter) {
      return true;
   }

   /**
    * Route into doclet processing
    *
    * @see com.sun.javadoc.Doclet#start(com.sun.javadoc.RootDoc)
    */
   @SuppressWarnings("unused")
   public static boolean start(RootDoc root) {
      cfg.setTemplateLoader(new ClassTemplateLoader(DomainObjectDocletCleaner.class, "/"));

      try {
         readOptions(root.options());

         ClassDocParser parser = new ClassDocParser();
         Template template = cfg.getTemplate(format + "Bean.ftl");

         for (ClassDoc clazz : root.classes()) {
            Bean bean = parser.parseBean(clazz);

            String className = clazz.simpleTypeName();
            String packageName = clazz.containingPackage().name();
            File outputFile = new File(outputPath, packageName.replaceAll("\\.", File.separator) + File.separator + className + ".java");
            outputFile.getParentFile().mkdirs();
            System.out.println("Processing " + clazz.name() + " writing to " + outputFile.getAbsolutePath());
            if (clazz.containingClass() == null) {
               template.process(bean, new FileWriter(outputFile));
            }
         }
         return true;
      } catch (Exception e) {
         throw Throwables.propagate(e);
      }
   }

   private static void readOptions(String[][] options) {
      for (String[] opt : options) {
         if (opt[0].equals("-d")) {
            outputPath = opt[1];
         }
         if (opt[0].equals("-format")) {
            format = opt[1];
         }
      }
   }

   private static List<String> listFileNames(File file) throws IOException {
      List<String> result = Lists.newArrayList();
      for (File f : listFiles(file)) {
         result.add(f.getAbsolutePath());
      }
      return result;
   }

   private static List<File> listFiles(File file) throws IOException {
      ImmutableList.Builder<File> newOnes = ImmutableList.builder();
      if (file.isDirectory()) {
         newOnes.addAll(listFiles(file, new ArrayList<File>()));
      } else if (file.getName().endsWith(".java")) {
         newOnes.add(file);
      }
      return newOnes.build();
   }

   private static List<File> listFiles(File file, List<File> result) {
      if (file.isDirectory()) {
         for (File directory : file.listFiles(new FileFilter() {
            public boolean accept(File file) {
               return file.isDirectory();
            }
         })) {
            listFiles(directory, result);
         }
         result.addAll(Arrays.asList(file.listFiles(new PatternFilenameFilter(".*\\.java"))));
      }
      return result;
   }
}
