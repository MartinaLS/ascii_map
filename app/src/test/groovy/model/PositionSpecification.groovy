package model

import spock.lang.Specification

class PositionSpecification extends Specification {

    def "test equality_positionsWithSameCoordinates"() {
        given:
        Position position = Position.of(0, 43)
        when:
        boolean isEqual = position == Position.of(0, 43)

        then:
        isEqual
    }

    def "test equality_positionsWithDifferentCoordinates"() {
        given:
        Position position = Position.of(0, 43)
        when:
        boolean isEqual = position == Position.of(0, 1)

        then:
        !isEqual
    }

    def "test getRightPosition"() {
        given:
        Position position = Position.of(0, 43);
        when:
        Position rightPosition = position.getRightPosition()

        then:
        rightPosition == Position.of(0, 44);
    }

    def "test getLeftPosition"() {
        given:
        Position position = Position.of(1, 43);
        when:
        Position leftPosition = position.getLeftPosition()

        then:
        leftPosition == Position.of(1, 42);
    }

    def "test getUpPosition"() {
        given:
        Position position = Position.of(1, 43);
        when:
        Position upPosition = position.getUpPosition()

        then:
        upPosition == Position.of(0, 43);
    }

    def "test getDownPosition"() {
        given:
        Position position = Position.of(1, 43);
        when:
        Position leftPosition = position.getDownPosition()

        then:
        leftPosition == Position.of(2, 43);
    }
}
