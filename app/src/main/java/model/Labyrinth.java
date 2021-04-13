package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Labyrinth {
   public static final String START_POINT = "@";

   public static final String END_POINT = "x";

   public static final String EMPTY_POINT = " ";

   private String[][] matrix = new String[100][100];

   private int maxRows = 100;

   private int maxColumns = 100;

   private final Map<Direction, Supplier<Position>> directionRule = new HashMap<>();

   private final List<Position> startPositions = new ArrayList<>();

   private final List<Position> endPositions = new ArrayList<>();

   private State currentState = new State();

   public Labyrinth(List<String> inputLines) {
      load(inputLines);
      registerDirectionRules();
   }

   private void load(List<String> lines) {
      maxRows = lines.size();
      maxColumns = lines.stream()
            .max(Comparator.comparingInt(String::length))
            .map(String::length).orElse(0);
      matrix = new String[maxRows][maxColumns];

      final AtomicInteger rowCounter = new AtomicInteger(0);
      lines.forEach((line) -> {
         int rowIndex = rowCounter.getAndIncrement();
         for (int columnIndex = 0; columnIndex < maxColumns; columnIndex++) {
            String character = columnIndex < line.length() ? line.substring(columnIndex, columnIndex + 1) : EMPTY_POINT;
            matrix[rowIndex][columnIndex] = character;

            if (START_POINT.equals(character)) {
               startPositions.add(Position.of(rowIndex, columnIndex));
            }

            if (END_POINT.equals(character)) {
               endPositions.add(Position.of(rowIndex, columnIndex));
            }
         }
      });

      if (startPositions.size() != 1) {
         throw new RuntimeException("Cannot determine starting point.");
      }
      currentState.position = startPositions.get(0);

      if (endPositions.size() != 1) {
         throw new RuntimeException("Cannot determine end point.");
      }
   }

   public boolean isPositionValid(Position position) {
      return position != null && position.getColumnIndex() >= 0 && position.getColumnIndex() < maxColumns
            && position.getRowIndex() >= 0 && position.getRowIndex() < maxRows;
   }

   public boolean isPositionValidAndIsNotEqualTo(Position p, String... elements) {
      return isPositionValid(p) && Arrays.stream(elements).noneMatch(el -> el.equals(getCharOnPosition(p)));
   }

   public String getCharOnCurrentPosition() {
      if (currentState.getPosition().isPresent()) {
         return getCharOnPosition(currentState.getPosition().get());
      }

      return null;
   }

   public String getCharOnPosition(Position position) {
      if (isPositionValid(position)) {
         return matrix[position.getRowIndex()][position.getColumnIndex()];
      }
      return null;
   }

   public String getCurrentPositionCoordinates() {
      Optional<Position> currentPosition = currentState.getPosition();
      if (currentPosition.isPresent() && isPositionValid(currentPosition.get())) {
         return String.format("(%s, %s)", currentPosition.get().getRowIndex(), currentPosition.get().getColumnIndex());
      }
      return null;
   }

   public Position getNextPosition(Direction direction) {
      return directionRule.get(direction).get();
   }

   public boolean isNextPositionValidAndNotEqualTo(Direction direction, String... elements) {
      return isPositionValidAndIsNotEqualTo(directionRule.get(direction).get(), elements);
   }

   public boolean isCurrentPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position, elements);
   }

   private void registerDirectionRules() {
      directionRule.put(Direction.UP, () -> currentState.getPosition().map(Position::getUpPosition).orElse(null));
      directionRule.put(Direction.RIGHT, () -> currentState.getPosition().map(Position::getRightPosition).orElse(null));
      directionRule.put(Direction.LEFT, () -> currentState.getPosition().map(Position::getLeftPosition).orElse(null));
      directionRule.put(Direction.DOWN, () -> currentState.getPosition().map(Position::getDownPosition).orElse(null));
   }

   public void setCurrentState(State state) {
      this.currentState = state;
   }

   public State getCurrentState() {
      return currentState;
   }

   public static class State {
      private Position position;

      private Direction direction = Direction.UNDEFINED;

      private State() {
      }

      private State(Position position, Direction direction) {
         this.position = position;
         this.direction = direction;
      }

      public static State of(Position position, Direction direction) {
         return new State(position, direction);
      }

      public static State ofUndefined() {
         return new State();
      }

      public Optional<Position> getPosition() {
         return Optional.ofNullable(position);
      }

      public Optional<Direction> getDirection() {
         return Optional.ofNullable(direction);
      }
   }
}
