package util

import spock.lang.Specification
import util.FileUtils

class FileUtilsSpecification extends Specification {
    def "test readFile"() {
        given:
        String fileName = 'case1.txt'
        when:
        List<String> lines = FileUtils.readFile(fileName)

        then:
        lines.size() == 5
    }
}
