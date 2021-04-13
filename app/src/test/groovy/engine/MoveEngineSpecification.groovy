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
    }

    def "test moveToTheEnd_case1:Map 1 - a basic example"() {
        given:
        List<String> lines = FileUtils.readFile("case1.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@---A---+|C|+---+|+-B-x'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'ACB'
    }

    def "test moveToTheEnd_case2:Map 2 - go straight through intersections"() {
        given:
        List<String> lines = FileUtils.readFile("case2.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@|A+---B--+|+--C-+|-||+---D--+|x'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case3:Map 3 - letters may be found on turns"() {
        given:
        List<String> lines = FileUtils.readFile("case3.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@---A---+|||C---+|+-B-x'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'ACB'
    }

    def "test moveToTheEnd_case4:Map 4 - do not collect letters twice"() {
        given:
        List<String> lines = FileUtils.readFile("case4.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@--A-+|+-+|A|+--B--+C|+-+|+-C-+|D|x'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case5:Map 5 - keep direction, even in a compact space"() {
        given:
        List<String> lines = FileUtils.readFile("case5.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@A+++A|+-B-+C+++C-+Dx'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'ABCD'
    }

    def "test moveToTheEnd_case5:Map 6 - basic example"() {
        given:
        List<String> lines = FileUtils.readFile("case6.txt")
        Labyrinth labyrinth = new Labyrinth(lines)

        MoveEngine moveEngine = new MoveEngine(labyrinth)
        when:
        moveEngine.moveToTheEnd()

        then:
        moveEngine.getMoveTrace().fullTrace == '@--+||+---A---x'
        moveEngine.getMoveTrace().onlySpecialCharTrace == 'A'
    }
}
