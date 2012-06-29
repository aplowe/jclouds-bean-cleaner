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
import org.jclouds.cleanup.data.ParseOptions;
import org.jclouds.cleanup.doclet.ClassDocParser;
import org.jclouds.logging.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.LogManager;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Interrogates the class structure using the doclet api, extracts the javadocs of classes, fields
 * and methods as required, and generates replacement source code.
 *
 * @see <a href=
 *      "http://docs.oracle.com/javase/6/docs/technotes/guides/javadoc/doclet/overview.html"
 *      />
 */
public class DomainObjectDocletCleaner extends Doclet {
   private static final Logger LOG = Logger.CONSOLE;
   private static final ParseOptions parseOptions = new ParseOptions(ParseOptions.Format.JSON, ParseOptions.NullableHandling.DEFAULT);
   private static final Configuration cfg = new Configuration();

   private static String outputPath = "target/generated-sources/cleanbeans";

   /**
    * Bootstrapping javadoc application to save users having to remember all the arguments.
    *
    * @param args the path to locate the source code and the path to the compiled classes
    * @throws IOException          if there are problems traversing the source code hierarchy
    * @throws InterruptedException if the spawned javadoc process is interrupted
    */
   public static void main(String[] args) throws IOException, InterruptedException {
      checkArgument(args.length > 1, "You need to supply a sourcePath and a classPath");

      String sourcePath = args[args.length - 2];
      String classPath = args[args.length - 1];

      List<String> command = Lists.newArrayList("javadoc",
            "-classpath", System.getProperty("java.class.path") + ":" + classPath,
            "-docletpath", System.getProperty("java.class.path"),
            "-private",
            "-doclet", DomainObjectDocletCleaner.class.getCanonicalName()
      );

      if (args.length > 2) {
         Collections.addAll(command, Arrays.copyOfRange(args, 0, args.length - 2));
      }

      command.addAll(listFileNames(new File(sourcePath)));

      Process process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));

      StreamGobbler in = new StreamGobbler(process.getInputStream(), "INFO");
      StreamGobbler err = new StreamGobbler(process.getErrorStream(), "ERROR");

      in.start();
      err.start();

      int returnCode = process.waitFor();
      if (returnCode == 0) {
         LOG.info("Javadoc returned successfully");
         LOG.info("You passed the following arguments: " + Joiner.on(" ").join(args));
      } else {
         LOG.error("Javadoc returned error code " + returnCode);
         LOG.info("You passed the following arguments: " + Joiner.on(" ").join(args));
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
      if (Objects.equal(option, "-verbose")) return 1;
      if (Objects.equal(option, "-format")) return 2;
      if (Objects.equal(option, "-nullable")) return 2;
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
         Template template = cfg.getTemplate(parseOptions.getFormat() == ParseOptions.Format.MINIMAL ? "MinimalBean.ftl" : "Bean.ftl");
         List<Bean> beans = Lists.newArrayList();

         for (ClassDoc clazz : root.classes()) {
            if (clazz.containingClass() == null) {
               beans.add(parser.parseBean(clazz, parseOptions, false));
            }
         }
         
         for (Bean bean : beans) {
            if (bean.getAllFields().size() > 0) {
               String className = bean.getType();
               String packageName = bean.getPackageName();
               File outputFile = new File(outputPath, packageName.replaceAll("\\.", File.separator) + File.separator + className + ".java");
               outputFile.getParentFile().mkdirs();
               LOG.info("Processing " + bean.getType() + " writing to " + outputFile.getAbsolutePath());
               template.process(bean, new FileWriter(outputFile));
            }
         }

         return true;
      } catch (Exception e) {
         throw Throwables.propagate(e);
      }
   }

   private static void readOptions(String[][] options) throws IOException {
      for (String[] opt : options) {
         if (Objects.equal(opt[0], "-verbose")) {
            // Quick-fix for verbose is to use JDK logging
            LogManager.getLogManager().readConfiguration(
                  DomainObjectDocletCleaner.class.getResourceAsStream("/verbose-logging.properties"));
         }else if (Objects.equal(opt[0], "-d")) {
            outputPath = opt[1];
         } else if (Objects.equal(opt[0], "-format")) {
            parseOptions.setFormat(ParseOptions.Format.fromValue(opt[1]));
         } else if (Objects.equal(opt[0], "-nullable")) {
            parseOptions.setNullableHandling(ParseOptions.NullableHandling.fromValue(opt[1]));
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
