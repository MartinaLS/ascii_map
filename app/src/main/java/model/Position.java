package model;

public class Position {
   private final int rowIndex;

   private final int columnIndex;

   private Position(int rowIndex, int columnIndex) {
      this.rowIndex = rowIndex;
      this.columnIndex = columnIndex;
   }

   public int getRowIndex() {
      return rowIndex;
   }

   public int getColumnIndex() {
      return columnIndex;
   }

   public Position getLeftPosition() {
      return Position.of(rowIndex, columnIndex - 1);
   }

   public Position getRightPosition() {
      return Position.of(rowIndex, columnIndex + 1);
   }

   public Position getUpPosition() {
      return Position.of(rowIndex - 1, columnIndex);
   }

   public Position getDownPosition() {
      return Position.of(rowIndex + 1, columnIndex);
   }

   public static Position of(int rowIndex, int columnIndex) {
      return new Position(rowIndex, columnIndex);
   }
}
