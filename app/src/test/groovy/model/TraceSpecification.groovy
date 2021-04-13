package model

import spock.lang.Specification

class TraceSpecification extends Specification {
    def "test addNewCharacter_characterCIsAllowed"() {
        given:
        Trace trace = new Trace();

        when:
        trace.addNewCharacter(Position.of(0, 0), 'C')

        then:
        trace.fullTrace == 'C'
        trace.onlySpecialCharTrace == 'C'
    }

    def "test addNewCharacter_character+IsNotAllowed"() {
        given:
        Trace trace = new Trace();

        when:
        trace.addNewCharacter(Position.of(0, 0), '+')

        then:
        trace.fullTrace == '+'
        trace.onlySpecialCharTrace == ''
    }
}
