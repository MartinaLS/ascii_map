import model.Labyrinth
import model.Position
import spock.lang.Specification
import util.FileUtils

class LabyrinthSpecification extends Specification {
    def "test getCharOnCurrentPosition_providedEmptyList_charOnCurrentPositionIsNull"() {
        given:
        Labyrinth labyrinth = new Labyrinth(new ArrayList<String>());

        when:
        String charOnCurrentPosition = labyrinth.getCharOnCurrentPosition()
        then:
        !charOnCurrentPosition
    }

    def "test getCharOnCurrentPosition_providedNoNEmptyList_charOnCurrentPositionIs@"() {
        given:
        List<String> lines = FileUtils.readFile("ascii_path_1.txt")
        Labyrinth labyrinth = new Labyrinth(lines);

        when:
        String charOnCurrentPosition = labyrinth.getCharOnCurrentPosition()

        then:
        charOnCurrentPosition == '@'
    }

    def "test getCharOnPosition_providedNoNEmptyList_charOnCurrentPositionIs-"() {
        given:
        List<String> lines = FileUtils.readFile("ascii_path_1.txt")
        Labyrinth labyrinth = new Labyrinth(lines);

        when:
        String charOnCurrentPosition = labyrinth.getCharOnPosition(Position.of(0, 2))

        then:
        charOnCurrentPosition == '-'
    }

    def "test getCharOnPosition_providedNoNEmptyList_charOnCurrentPositionIsNull"() {
        given:
        List<String> lines = FileUtils.readFile("ascii_path_1.txt")
        Labyrinth labyrinth = new Labyrinth(lines);

        when:
        String charOnCurrentPosition = labyrinth.getCharOnPosition(Position.of(0, 5))

        then:
        charOnCurrentPosition == 'A'
    }
}
