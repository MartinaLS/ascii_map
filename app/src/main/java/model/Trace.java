package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trace {
   private final String[] unAllowedLetters = {" ", "x", "-", "|", "@", "+"};

   private final StringBuilder fullTraceBuilder = new StringBuilder();

   private final StringBuilder onlySpecialCharTraceBuilder = new StringBuilder();

   private final Set<Position> passedPositions = new HashSet<>();

   public String getFullTrace() {
      return fullTraceBuilder.toString();
   }

   public String getOnlySpecialCharTrace() {
      return onlySpecialCharTraceBuilder.toString();
   }

   public List<String> getUnAllowedCharacters() {
      return Arrays.asList(unAllowedLetters);
   }

   public void addNewCharacter(Position characterPosition, String character) {
      fullTraceBuilder.append(character);

      if (!passedPositions.contains(characterPosition) && !getUnAllowedCharacters().contains(character)) {
         passedPositions.add(characterPosition);
         onlySpecialCharTraceBuilder.append(character);
      }
   }
}
