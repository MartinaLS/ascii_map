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

   @Override
   public boolean equals(Object other) {
      if (other == this) {
         return true;
      }
      if (!(other instanceof Position)) {
         return false;
      }
      return rowIndex == ((Position) other).rowIndex && columnIndex == ((Position) other).columnIndex;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + rowIndex;
      result = prime * result + columnIndex;
      return result;
   }

   @Override
   public String toString() {
      return  String.format("(%s, %s)", rowIndex, columnIndex);
   }
}
