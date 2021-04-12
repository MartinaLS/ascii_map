package util;

import application.App;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {
   private FileUtils() {
   }

   public static List<String> readFile(String filePath) {
      Scanner input = null;
      try {
         input = new Scanner(Path.of(App.class.getClassLoader().getResource(filePath).toURI()))
               .useDelimiter("\n");
      } catch (Exception e) {
         //ignore ex
      }
      List<String> lines = new ArrayList<>();
      while (input != null && input.hasNext()) {
         lines.add(input.next());
      }
      return lines;
   }
}
