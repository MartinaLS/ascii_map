package model

import spock.lang.Specification

class PositionSpecification extends Specification {

    def "test equality_positionsWithSameCoordinates"() {
        given:
        Position position = Position.of(0, 43);
        when:
        boolean isEqual = position == Position.of(0, 43);

        then:
        isEqual
    }

    def "test equality_positionsWithDifferentCoordinates"() {
        given:
        Position position = Position.of(0, 43);
        when:
        boolean isEqual = position == Position.of(0, 1);

        then:
        !isEqual
    }
}
