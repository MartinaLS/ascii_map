package engine;

import model.Direction;
import model.Labyrinth;
import model.Position;
import model.Trace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
   }

   public void moveToTheEnd() {
      System.out.println("Start moving...");

      labyrinth.getCurrentState().getPosition().ifPresentOrElse(position ->
                  trace.addNewCharacter(position, labyrinth.getCharOnCurrentPosition()),
            () -> {
               throw new RuntimeException("Unknown starting point.");
            });

      System.out.println("Start coordinate: " + labyrinth.getCurrentPosition());
      while (nextMove()) {
         System.out.println("-> Current position coordinate: " + labyrinth.getCurrentPosition());
      }

      System.out.println("End coordinate: " + labyrinth.getCurrentPosition());
   }

   public boolean nextMove() {
      Labyrinth.State newState = null;
      if (labyrinth.isCurrentPositionValidAndNotEqualTo(" ", "x", "+", "@")) {
         newState = labyrinth.getCurrentState().getDirection()
               .map(direction -> changePositionRule.apply(direction)).orElse(null);
      }

      if ((newState == null || newState.getPosition().isEmpty()) && labyrinth.isCurrentPositionValidAndNotEqualTo("x")) {
         newState = labyrinth.getCurrentState().getDirection().map(direction -> {
            List<Direction> directions = directionToPerpendicularDirections
                  .getOrDefault(direction, Arrays.asList(UP, DOWN, LEFT, RIGHT));
            return directionToChangeStateRule.get(direction)
                  .apply(directions.get(0), directions.get(1));
         }).orElse(null);
      }

      Optional.ofNullable(newState).ifPresent(state -> {
         labyrinth.setCurrentState(state);
         trace.addNewCharacter(state.getPosition()
               .orElseThrow(() -> {
                  throw new RuntimeException("Cannot determine next move");
               }), labyrinth.getCharOnCurrentPosition());
      });

      return labyrinth.isEndReached();
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

         return countOfPossibleDirections == 1 ? Labyrinth.State.of(newPosition, newDirection) : Labyrinth.State.ofUndefined();
      };
   }

   private BiFunction<Direction, Direction, Labyrinth.State> buildInitialChangeStateRule() {
      return (firstPossibleDirection, secondPossibleDirection) -> {
         final Labyrinth.State newState = Labyrinth.State.ofUndefined();
         directionToPerpendicularDirections.keySet().forEach(direction -> {
            if (labyrinth.isNextPositionValidAndNotEqualTo(direction, Labyrinth.EMPTY_POINT)) {
               newState.copy(Labyrinth.State.of(labyrinth.getNextPosition(direction), direction));
            }
         });
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

   public Trace getTrace() {
      return trace;
   }
}
