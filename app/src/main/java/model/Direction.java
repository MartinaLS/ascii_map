package model;

public enum Direction {
   UNDEFINED,
   RIGHT,
   LEFT,
   UP,
   DOWN;

   public boolean isLeft() {
      return this == LEFT;
   }

   public boolean isRight() {
      return this == RIGHT;
   }

   public boolean isUp() {
      return this == UP;
   }

   public boolean isDown() {
      return this == DOWN;
   }
}
