package model


import spock.lang.Specification
import spock.lang.Unroll
import util.FileUtils

class LabyrinthSpecification extends Specification {
    def "test getCurrentPositionCoordinates_providedNoNEmptyList_charOnCurrentPositionIs@"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        String coordinates = labyrinth.getCurrentPositionCoordinates()

        then:
        coordinates == '(0, 1)'
    }

    def "test getCharOnCurrentPosition_providedNoNEmptyList_charOnCurrentPositionIs@"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        String charOnCurrentPosition = labyrinth.getCharOnCurrentPosition()

        then:
        charOnCurrentPosition == '@'
    }

    @Unroll
    def "test getCharOnPosition_providedNoNEmptyList_charOnCurrentPositionIs#expectedChar"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        String charOnCurrentPosition = labyrinth.getCharOnPosition(Position.of(sourceRowIndex, sourceColumnIndex))

        then:
        charOnCurrentPosition == expectedChar

        where:
        sourceRowIndex | sourceColumnIndex || expectedChar
        0              | 2                 || '-'
        0              | 5                 || 'A'
        0              | 9                 || '+'
        0              | 0                 || ' '
        0              | 1                 || '@'
        2              | 1                 || 'x'
    }

    @Unroll
    def "test isPositionValid_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isPositionValid(Position.of(sourceRowIndex, sourceColumnIndex))

        then:
        isPositionValid == expectedPositionValid

        where:
        sourceRowIndex | sourceColumnIndex || expectedPositionValid
        -1             | 5                 || false
        0              | 5                 || true
        5              | 100               || false
        3              | -1                || false
    }

    @Unroll
    def "test isPositionValidAndIsNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isPositionValidAndIsNotEqualTo(Position.of(sourceRowIndex, sourceColumnIndex), unallowdChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        sourceRowIndex | sourceColumnIndex | unallowdChars          || expectedPositionValid
        -1             | 5                 | new String[]{}         || false
        0              | 5                 | new String[]{}         || true
        5              | 100               | new String[]{}         || false
        3              | -1                | new String[]{}         || false
        0              | 2                 | new String[]{" ", "-"} || false
        0              | 2                 | new String[]{" ", "+"} || true
    }

    @Unroll
    def "test isDownPositionValidAndNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isNextPositionValidAndNotEqualTo(Direction.DOWN, unallowedChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        unallowedChars    || expectedPositionValid
        new String[]{}    || true
        new String[]{" "} || false
    }

    @Unroll
    def "test isUpperPositionValidAndNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isNextPositionValidAndNotEqualTo(Direction.UP, unallowedChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        unallowedChars    || expectedPositionValid
        new String[]{}    || false
        new String[]{" "} || false
    }

    @Unroll
    def "test isLeftPositionValidAndNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isNextPositionValidAndNotEqualTo(Direction.LEFT, unallowedChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        unallowedChars    || expectedPositionValid
        new String[]{}    || true
        new String[]{" "} || false
    }

    @Unroll
    def "test isRightPositionValidAndNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isNextPositionValidAndNotEqualTo(Direction.RIGHT, unallowedChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        unallowedChars    || expectedPositionValid
        new String[]{}    || true
        new String[]{" "} || true
        new String[]{"-"} || false
    }

    @Unroll
    def "test getNextPosition-inDirection:#sourceDirection"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        Position position = labyrinth.getNextPosition(sourceDirection)

        then:
        position == targetPosition

        where:
        sourceDirection || targetPosition
        Direction.LEFT  || Position.of(0, 0)
        Direction.RIGHT || Position.of(0, 2)
        Direction.UP    || Position.of(-1, 1)
        Direction.DOWN  || Position.of(1, 1)
    }

    @Unroll
    def "test isCurrentPositionValidAndNotEqualTo_labyrinthWithProvidedNoNEmptyList_positionIsValid=#expectedPositionValid"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        when:
        boolean isPositionValid = labyrinth.isCurrentPositionValidAndNotEqualTo(unallowedChars)

        then:
        isPositionValid == expectedPositionValid

        where:
        unallowedChars    || expectedPositionValid
        new String[]{}    || true
        new String[]{" "} || true
        new String[]{"-"} || true
        new String[]{"@"} || false
    }

    def "test labyrinth construction Map 7 - no start (labyrinth not valid)"() {
        when:
        List<String> lines = FileUtils.readFile("case7.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        then:
        thrown(RuntimeException)
    }

    def "test labyrinth construction Map 8 - no end (labyrinth not valid)"() {
        when:
        List<String> lines = FileUtils.readFile("case8.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        then:
        thrown(RuntimeException)
    }

    def "test labyrinth construction Map 9 - multiple ends (labyrinth not valid)"() {
        when:
        List<String> lines = FileUtils.readFile("case9.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        then:
        thrown(RuntimeException)
    }

    def "test labyrinth construction Map 10 - multiple ends (labyrinth not valid)"() {
        when:
        List<String> lines = FileUtils.readFile("case10.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        then:
        thrown(RuntimeException)
    }
}
