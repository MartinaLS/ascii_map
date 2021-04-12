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

   private Position currentPosition;

   private Position previousPosition;

   private Direction currentDirection;

   private final StringBuilder fullPathBuilder = new StringBuilder();

   public Labyrinth(List<String> inputLines) {
      buildMatrix(inputLines);
      initPositions();
   }

   private void buildMatrix(List<String> lines) {
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
               currentPosition = startPosition;
               fullPathBuilder.append(c);
            }
         }
         rowIndex.incrementAndGet();
      });
   }

   public boolean isPositionValid(Position position) {
      return position.getColumnIndex() >= 0 && position.getColumnIndex() < maxColumns
            && position.getRowIndex() >= 0 && position.getRowIndex() < maxRows;
   }

   public boolean isPositionValidAndEqualTo(Position p, String... elements) {
      return isPositionValid(p) && Arrays.asList(elements).stream().anyMatch(el -> el.equals(getCharOnPosition(p)));
   }

   public boolean isPositionValidAndIsNotEqualTo(Position p, String... elements) {
      return isPositionValid(p) && Arrays.asList(elements).stream().noneMatch(el -> el.equals(getCharOnPosition(p)));
   }

   public String getCharOnPosition(Position position) {
      if (isPositionValid(position)) {
         return matrix[position.getRowIndex()][position.getColumnIndex()];
      }

      return "n";
   }

   public boolean nextMove() {
      Pair pair = null;
      if (isPositionValidAndIsNotEqualTo(currentPosition, " ", "x", "+", "@")) {
         pair = changePosition();
      }

      previousPosition = currentPosition;
      if ((pair == null || pair.getPosition().isEmpty()) && isPositionValidAndIsNotEqualTo(currentPosition, "x")) {
         pair = changeDirectionAndPosition();
      }

      if (pair != null && pair.getPosition().isPresent()) {
         currentPosition = pair.getPosition().get();
      }
      if (pair != null && pair.getDirection().isPresent()) {
         currentDirection = pair.getDirection().get();
      }

      fullPathBuilder.append(getCharOnPosition(currentPosition));

      return !getCharOnPosition(currentPosition).equals("x");
   }


   public Pair changeDirectionAndPosition() {
      Position newPosition = null;
      Direction newDirection = null;
      if (currentDirection.isLeft() || currentDirection.isRight()) {
         if (isPositionValidAndIsNotEqualTo(currentPosition.getDownPosition(), " ", "@")) {
            newPosition = currentPosition.getDownPosition();
            newDirection = Direction.DOWN;
         }

         if (isPositionValidAndIsNotEqualTo(currentPosition.getUpPosition(), " ", "@")) {
            newPosition = currentPosition.getUpPosition();
            newDirection = Direction.UP;
         }
      } else if (currentDirection.isUp() || currentDirection.isDown()) {
         if (isPositionValidAndIsNotEqualTo(currentPosition.getRightPosition(), " ", "@")) {
            newPosition = currentPosition.getRightPosition();
            newDirection = Direction.RIGHT;
         }

         if (isPositionValidAndIsNotEqualTo(currentPosition.getLeftPosition(), " ", "@")) {
            newPosition = currentPosition.getLeftPosition();
            newDirection = Direction.LEFT;
         }
      }

      return Pair.of(newPosition, newDirection);
   }

   public Pair changePosition() {
      Position newPosition = null;
      Direction newDirection = currentDirection;
      if (currentDirection.isLeft() && isPositionValidAndIsNotEqualTo(currentPosition.getLeftPosition(), " ", "@")) {
         newPosition = currentPosition.getLeftPosition();
      }
      if (currentDirection.isRight() && isPositionValidAndIsNotEqualTo(currentPosition.getRightPosition(), " ", "@")) {
         newPosition = currentPosition.getRightPosition();
      }
      if (currentDirection.isDown() && isPositionValidAndIsNotEqualTo(currentPosition.getDownPosition(), " ", "@")) {
         newPosition = currentPosition.getDownPosition();
      }
      if (currentDirection.isUp() && isPositionValidAndIsNotEqualTo(currentPosition.getUpPosition(), " ", "@")) {
         newPosition = currentPosition.getUpPosition();
      }

      return Pair.of(newPosition, newDirection);
   }

   private void initPositions() {
      if (startPosition == currentPosition) {
         Position newPosition = currentPosition.getLeftPosition();
         if (isPositionValidAndIsNotEqualTo(newPosition, " ")) {
            currentPosition = newPosition;
            currentDirection = Direction.LEFT;
         }
         newPosition = currentPosition.getRightPosition();
         if (isPositionValidAndIsNotEqualTo(newPosition, " ")) {
            currentPosition = newPosition;
            currentDirection = Direction.RIGHT;
         }

         newPosition = currentPosition.getUpPosition();
         if (isPositionValidAndIsNotEqualTo(newPosition, " ")) {
            currentPosition = newPosition;
            currentDirection = Direction.UP;
         }

         newPosition = currentPosition.getDownPosition();
         if (isPositionValidAndIsNotEqualTo(newPosition, " ")) {
            currentPosition = newPosition;
            currentDirection = Direction.DOWN;
         }

         fullPathBuilder.append(getCharOnPosition(currentPosition));
         previousPosition = startPosition;
      }
   }

   public String getFullPath() {
      return fullPathBuilder.toString();
   }


   public static class Pair {
      private Position position;

      private Direction direction;

      private Pair(Position position, Direction direction) {
         this.position = position;
         this.direction = direction;
      }

      public static Pair of(Position position, Direction direction) {
         return new Pair(position, direction);
      }

      public Optional<Position> getPosition() {
         return Optional.ofNullable(position);
      }

      public Optional<Direction> getDirection() {
         return Optional.ofNullable(direction);
      }
   }
}
