package model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Labyrinth {
   private String[][] matrix = new String[100][100];

   private int maxRows = 100;

   private int maxColumns = 100;

   private Position startPosition;

   private State currentState = new State();

   public Labyrinth(List<String> inputLines) {
      load(inputLines);
   }

   private void load(List<String> lines) {
      maxRows = lines.size();
      maxColumns = lines.stream()
            .max((f, s) -> s.length() < f.length() ? 1 : -1)
            .map(String::length).orElse(0);

      matrix = new String[maxRows][maxColumns];
      final AtomicInteger rowIndex = new AtomicInteger(0);
      lines.forEach((line) -> {
         for (int columnIndex = 0; columnIndex < maxColumns; columnIndex++) {
            char c = columnIndex < line.length() ? line.charAt(columnIndex) : " ".charAt(0);
            matrix[rowIndex.get()][columnIndex] = String.valueOf(c);
            if (c == '@') {
               startPosition = Position.of(rowIndex.get(), columnIndex);
               currentState.position = startPosition;
            }
         }
         rowIndex.incrementAndGet();
      });
   }

   public boolean isPositionValid(Position position) {
      return position.getColumnIndex() >= 0 && position.getColumnIndex() < maxColumns
            && position.getRowIndex() >= 0 && position.getRowIndex() < maxRows;
   }

   public boolean isPositionValidAndIsNotEqualTo(Position p, String... elements) {
      return isPositionValid(p) && Arrays.stream(elements).noneMatch(el -> el.equals(getCharOnPosition(p)));
   }

   public State getCurrentState() {
      return currentState;
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

   public boolean isDownPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position.getDownPosition(), elements);
   }

   public boolean isUpperPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position.getUpPosition(), elements);
   }

   public boolean isLeftPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position.getLeftPosition(), elements);
   }

   public boolean isRightPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position.getRightPosition(), elements);
   }

   public boolean isCurrentPositionValidAndNotEqualTo(String... elements) {
      return isPositionValidAndIsNotEqualTo(currentState.position, elements);
   }

   public void setCurrentState(State state) {
      this.currentState = state;
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

      public Optional<Position> getPosition() {
         return Optional.ofNullable(position);
      }

      public Optional<Direction> getDirection() {
         return Optional.ofNullable(direction);
      }
   }
}
