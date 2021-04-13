package engine;

import model.Direction;
import model.Labyrinth;
import model.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static model.Direction.DOWN;
import static model.Direction.LEFT;
import static model.Direction.RIGHT;
import static model.Direction.UP;

public class MoveEngine {
   private final Trace trace = new Trace();

   private final Labyrinth labyrinth;

   private final Map<Direction, BiFunction<Direction, Direction, Labyrinth.State>> directionToChangeStateRule = new HashMap<>();

   private Function<Direction, Labyrinth.State> changePositionRule;

   private final Map<Direction, List<Direction>> directionToPerpendicularDirections = new HashMap<>();

   public MoveEngine(Labyrinth labyrinth) {
      this.registerRules();
      this.labyrinth = labyrinth;
      labyrinth.getCurrentState().getPosition().ifPresentOrElse(position ->
                  trace.addNewCharacter(position, labyrinth.getCharOnCurrentPosition()),
            () -> {
               throw new RuntimeException("Unknown starting point.");
            });
   }

   public void moveToTheEnd() {
      System.out.println("Start moving...");
      System.out.println("Start coordinate: " + labyrinth.getCurrentPositionCoordinates());
      while (nextMove()) {
         System.out.println("-> Current position coordinate: " + labyrinth.getCurrentPositionCoordinates());
      }

      System.out.println("End coordinate: " + labyrinth.getCurrentPositionCoordinates());
   }

   public boolean nextMove() {
      Labyrinth.State state = null;
      if (labyrinth.isCurrentPositionValidAndNotEqualTo(" ", "x", "+", "@")) {
         state = labyrinth.getCurrentState().getDirection()
               .map(direction -> changePositionRule.apply(direction)).orElse(null);
      }

      if ((state == null || state.getPosition().isEmpty()) && labyrinth.isCurrentPositionValidAndNotEqualTo("x")) {
         state = labyrinth.getCurrentState().getDirection()
               .map(direction -> {
                  List<Direction> directions = directionToPerpendicularDirections
                        .getOrDefault(direction, Arrays.asList(UP, DOWN, LEFT, RIGHT));
                  return directionToChangeStateRule.get(direction)
                        .apply(directions.get(0), directions.get(1));
               }).orElse(null);
      }

      String newCharacter = null;
      if (state != null && state.getPosition().isPresent() && state.getDirection().isPresent()) {
         labyrinth.setCurrentState(state);

         newCharacter = labyrinth.getCharOnCurrentPosition();
         trace.addNewCharacter(state.getPosition().get(), newCharacter);
      }

      if (state == null || state.getPosition().isEmpty()) {
         throw new RuntimeException("Cannot determine next move");
      }

      return !"x".equals(newCharacter);
   }

   private void registerRules() {
      directionToChangeStateRule.put(LEFT, this.buildChangeStateRule());
      directionToChangeStateRule.put(Direction.RIGHT, this.buildChangeStateRule());
      directionToChangeStateRule.put(Direction.UP, this.buildChangeStateRule());
      directionToChangeStateRule.put(Direction.DOWN, this.buildChangeStateRule());
      directionToChangeStateRule.put(Direction.UNDEFINED, this.buildInitialChangeStateRule());

      changePositionRule = this.buildChangePositionRule();

      directionToPerpendicularDirections.put(LEFT, Arrays.asList(UP, DOWN));
      directionToPerpendicularDirections.put(RIGHT, Arrays.asList(UP, DOWN));
      directionToPerpendicularDirections.put(UP, Arrays.asList(RIGHT, LEFT));
      directionToPerpendicularDirections.put(DOWN, Arrays.asList(RIGHT, LEFT));
   }

   private BiFunction<Direction, Direction, Labyrinth.State> buildChangeStateRule() {
      return (firstPossibleDirection, secondPossibleDirection) -> {
         Position newPosition = null;
         Direction newDirection = null;
         int countOfPossibleDirections = 0;
         if (labyrinth.isNextPositionValidAndNotEqualTo(firstPossibleDirection, Labyrinth.EMPTY_POINT, Labyrinth.START_POINT)) {
            newPosition = labyrinth.getNextPosition(firstPossibleDirection);
            newDirection = firstPossibleDirection;
            countOfPossibleDirections++;
         }

         if (labyrinth.isNextPositionValidAndNotEqualTo(secondPossibleDirection, Labyrinth.EMPTY_POINT, Labyrinth.START_POINT)) {
            newPosition = labyrinth.getNextPosition(secondPossibleDirection);
            newDirection = secondPossibleDirection;
            countOfPossibleDirections++;
         }

         return countOfPossibleDirections == 1 ? Labyrinth.State.of(newPosition, newDirection) : null;
      };
   }

   private BiFunction<Direction, Direction, Labyrinth.State> buildInitialChangeStateRule() {
      return (firstPossibleDirection, secondPossibleDirection) -> {
         Labyrinth.State currentState = labyrinth.getCurrentState();
         Labyrinth.State newState = null;
         if (labyrinth.isNextPositionValidAndNotEqualTo(LEFT, Labyrinth.EMPTY_POINT)) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getLeftPosition).orElse(null), LEFT);
         } else if (labyrinth.isNextPositionValidAndNotEqualTo(RIGHT, Labyrinth.EMPTY_POINT)) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getRightPosition).orElse(null), RIGHT);
         } else if (labyrinth.isNextPositionValidAndNotEqualTo(UP, Labyrinth.EMPTY_POINT)) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getUpPosition).orElse(null), UP);
         } else if (labyrinth.isNextPositionValidAndNotEqualTo(DOWN, Labyrinth.EMPTY_POINT)) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getDownPosition).orElse(null), DOWN);
         }
         return newState;
      };
   }

   private Function<Direction, Labyrinth.State> buildChangePositionRule() {
      return direction -> {
         Position newPosition = null;
         if (labyrinth.isNextPositionValidAndNotEqualTo(direction, Labyrinth.EMPTY_POINT, Labyrinth.START_POINT)) {
            newPosition = labyrinth.getNextPosition(direction);
         }
         return Labyrinth.State.of(newPosition, labyrinth.getCurrentState().getDirection().orElse(null));
      };
   }

   public Trace getTrace() {
      return trace;
   }

   public static class Trace {
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
}
