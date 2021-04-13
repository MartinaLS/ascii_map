package engine

import model.Labyrinth
import spock.lang.Specification
import util.FileUtils

class MoveEngineSpecification extends Specification {

    def "test nexMove"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        boolean hasNext = moveEngine.nextMove()

        then:
        hasNext
        labyrinth.getCharOnCurrentPosition() == '-'
    }

    def "test moveToTheEnd_case1:Map 1 - a basic example"() {
        given:
        List<String> lines = FileUtils.readFile("case1.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@---A---+|C|+---+|+-B-x'
        moveEngine.getTrace().onlySpecialCharTrace == 'ACB'
    }

    def "test moveToTheEnd_case2:Map 2 - go straight through intersections"() {
        given:
        List<String> lines = FileUtils.readFile("case2.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@|A+---B--+|+--C-+|-||+---D--+|x'
        moveEngine.getTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case3:Map 3 - letters may be found on turns"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@---A---+|||C---+|+-B-x'
        moveEngine.getTrace().onlySpecialCharTrace == 'ACB'
    }

    def "test moveToTheEnd_case4:Map 4 - do not collect letters twice"() {
        given:
        List<String> lines = FileUtils.readFile("case4.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@--A-+|+-+|A|+--B--+C|+-+|+-C-+|D|x'
        moveEngine.getTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case5:Map 5 - keep direction, even in a compact space"() {
        given:
        List<String> lines = FileUtils.readFile("case5.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@A+++A|+-B-+C+++C-+Dx'
        moveEngine.getTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case6:Map 6 - basic example"() {
        given:
        List<String> lines = FileUtils.readFile("case6.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getTrace().fullTrace == '@--+||+---A---x'
        moveEngine.getTrace().onlySpecialCharTrace == 'A'
    }

    def "test moveToTheEnd_case11:Map 11 - too many options"() {
        given:
        List<String> lines = FileUtils.readFile("case11.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        thrown(RuntimeException)
    }
}
