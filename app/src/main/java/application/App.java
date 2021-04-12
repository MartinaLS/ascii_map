/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package application;

import model.Labyrinth;
import engine.MoveEngine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

   public static void main(String[] args) {
      List<String> lines = readFile("ascii_path_3.txt");
      MoveEngine moveEngine = new MoveEngine(new Labyrinth(lines));
      while (moveEngine.nextMove()) {
         System.out.println(moveEngine.getMoveTrace().getFullTrace());
      }
   }

   private static List<String> readFile(String filePath) {
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
