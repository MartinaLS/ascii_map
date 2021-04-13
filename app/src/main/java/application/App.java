/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package application;

import engine.MoveEngine;
import model.Labyrinth;

import java.util.List;

import static util.FileUtils.readFile;

public class App {

   public static void main(String[] args) {
      if (args.length == 1) {
         List<String> lines = readFile(args[0]);
         MoveEngine moveEngine = new MoveEngine(new Labyrinth(lines));
         moveEngine.moveToTheEnd();

         System.out.println("FULL PATH: " + moveEngine.getTrace().getFullTrace());
         System.out.println("ONLY SPECIAL CHAR: " + moveEngine.getTrace().getOnlySpecialCharTrace());
      }
   }
}
