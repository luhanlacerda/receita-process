package lacerda.luhan.sincreceita.service

import lacerda.luhan.sincreceita.model.InputData
import spock.lang.Specification

class ProcessCSVServiceSpec extends Specification {

    static final String FILENAME_INPUT = "src\\test\\java\\resources\\receita.csv"
    static final String FILENAME_OUTPUT = "src\\test\\java\\resources\\receita-out.csv"

    ProcessCSVService process

    def expected
    def data

    def setup() {
        process = new ProcessCSVService(receitaService: Stub(ReceitaService));
    }

    def "when read a valid .csv file"() {
        given:
        expected = Arrays.asList(new InputData("0101", "12225-6", 100.0, "A"),
                new InputData("0101", "12226-8", 3200.5, "A"),
                new InputData("3202", "40011-1", -35.12, "I"),
                new InputData("3202", "54001-2", 0.0, "P"),
                new InputData("3202", "00321-2", 34500.0, "B"))

        when:
        data = process.read(FILENAME_INPUT)

        then:
        data.size() == expected.size()
        data == expected
    }

    def "when read invalid .csv file"() {
        when:
        data = process.read("FILENAME_INPUT")

        then:
        data == null
        thrown(IOException.class)
    }


    def "when create a valid .csv file"() {
        given:
        File file = new File(FILENAME_OUTPUT)
        expected = Arrays.asList(new InputData("0101", "12225-6", 100.0, "A"),
                new InputData("0101", "12226-8", 3200.5, "A"),
                new InputData("3202", "40011-1", -35.12, "I"),
                new InputData("3202", "54001-2", 0.0, "P"),
                new InputData("3202", "00321-2", 34500.0, "B"))

        when:
        process.createCSVFile(expected, FILENAME_OUTPUT)

        then:
        file.exists() == true
        notThrown(Exception.class)
    }

    def "when create a invalid .csv file"() {
        given:
        expected = Arrays.asList(new InputData("0101", "12225-6", 100.0, "A"),
                new InputData("0101", "12226-8", 3200.5, "A"),
                new InputData("3202", "40011-1", -35.12, "I"),
                new InputData("3202", "54001-2", 0.0, "P"),
                new InputData("3202", "00321-2", 34500.0, "B"))

        when:
        data = process.createCSVFile(expected, null)

        then:
        data == null
        thrown(Exception.class)
    }


}
