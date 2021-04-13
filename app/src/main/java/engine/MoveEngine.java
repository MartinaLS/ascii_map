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
import java.util.function.UnaryOperator;

public class MoveEngine {
   private final MoveTrace moveTrace = new MoveTrace();

   private final Labyrinth labyrinth;

   private final Map<Direction, UnaryOperator<Labyrinth.State>> directionToChangeStateRule = new HashMap<>();

   private final Map<Direction, UnaryOperator<Labyrinth.State>> directionToChangePositionRule = new HashMap<>();

   public MoveEngine(Labyrinth labyrinth) {
      this.registerRules();
      this.labyrinth = labyrinth;
      labyrinth.getCurrentState().getPosition().ifPresentOrElse(position ->
                  moveTrace.addNewCharacter(position, labyrinth.getCharOnCurrentPosition()),
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
               .map(direction -> directionToChangePositionRule.get(direction)
                     .apply(labyrinth.getCurrentState())).orElse(null);
      }

      if ((state == null || state.getPosition().isEmpty()) && labyrinth.isCurrentPositionValidAndNotEqualTo("x")) {
         state = labyrinth.getCurrentState().getDirection()
               .map(direction -> directionToChangeStateRule.get(direction)
                     .apply(labyrinth.getCurrentState())).orElse(null);
      }

      String newCharacter = null;
      if (state != null && state.getPosition().isPresent() && state.getDirection().isPresent()) {
         labyrinth.setCurrentState(state);
         newCharacter = labyrinth.getCharOnCurrentPosition();
         moveTrace.addNewCharacter(state.getPosition().get(), newCharacter);
      }

      return !"x".equals(newCharacter);
   }

   private void registerRules() {
      directionToChangeStateRule.put(Direction.LEFT, this.buildChangeStateRuleForRightAndLeft());
      directionToChangeStateRule.put(Direction.RIGHT, this.buildChangeStateRuleForRightAndLeft());
      directionToChangeStateRule.put(Direction.UP, this.buildChangeStateRuleForUpAndDown());
      directionToChangeStateRule.put(Direction.DOWN, this.buildChangeStateRuleForUpAndDown());
      directionToChangeStateRule.put(Direction.UNDEFINED, this.buildInitialChangeStateRule());

      directionToChangePositionRule.put(Direction.LEFT, currentState -> {
         Position newPosition = null;
         if (labyrinth.isLeftPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getLeftPosition).orElse(null);
         }
         return Labyrinth.State.of(newPosition, currentState.getDirection().orElse(null));
      });

      directionToChangePositionRule.put(Direction.DOWN, currentState -> {
         Position newPosition = null;
         if (labyrinth.isDownPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getDownPosition).orElse(null);
         }
         return Labyrinth.State.of(newPosition, currentState.getDirection().orElse(null));
      });


      directionToChangePositionRule.put(Direction.RIGHT, currentState -> {
         Position newPosition = null;
         if (labyrinth.isRightPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getRightPosition).orElse(null);
         }
         return Labyrinth.State.of(newPosition, currentState.getDirection().orElse(null));
      });

      directionToChangePositionRule.put(Direction.UP, currentState -> {
         Position newPosition = null;
         if (labyrinth.isUpperPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getUpPosition).orElse(null);
         }
         return Labyrinth.State.of(newPosition, currentState.getDirection().orElse(null));
      });
   }


   private UnaryOperator<Labyrinth.State> buildChangeStateRuleForRightAndLeft() {
      return state -> {
         Position newPosition = null;
         Direction newDirection = null;
         if (labyrinth.isDownPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getDownPosition).orElse(null);
            newDirection = Direction.DOWN;
         }

         if (labyrinth.isUpperPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getUpPosition).orElse(null);
            newDirection = Direction.UP;
         }

         return Labyrinth.State.of(newPosition, newDirection);
      };
   }

   private UnaryOperator<Labyrinth.State> buildChangeStateRuleForUpAndDown() {
      return state -> {
         Position newPosition = null;
         Direction newDirection = null;
         if (labyrinth.isRightPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getRightPosition).orElse(null);
            newDirection = Direction.RIGHT;
         }

         if (labyrinth.isLeftPositionValidAndNotEqualTo(" ", "@")) {
            newPosition = labyrinth.getCurrentState().getPosition()
                  .map(Position::getLeftPosition).orElse(null);
            newDirection = Direction.LEFT;
         }

         return Labyrinth.State.of(newPosition, newDirection);
      };
   }

   private UnaryOperator<Labyrinth.State> buildInitialChangeStateRule() {
      return state -> {
         Labyrinth.State currentState = labyrinth.getCurrentState();
         Labyrinth.State newState = null;
         if (labyrinth.isLeftPositionValidAndNotEqualTo(" ")) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getLeftPosition).orElse(null),
                  Direction.LEFT);
         } else if (labyrinth.isRightPositionValidAndNotEqualTo(" ")) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getRightPosition).orElse(null),
                  Direction.RIGHT);
         } else if (labyrinth.isUpperPositionValidAndNotEqualTo(" ")) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getUpPosition).orElse(null),
                  Direction.UP);
         } else if (labyrinth.isDownPositionValidAndNotEqualTo(" ")) {
            newState = Labyrinth.State.of(currentState.getPosition().map(Position::getDownPosition).orElse(null),
                  Direction.DOWN);
         }
         return newState;
      };
   }

   public MoveTrace getMoveTrace() {
      return moveTrace;
   }

   public static class MoveTrace {
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
