package lacerda.luhan.sincreceita.service;

import lacerda.luhan.sincreceita.model.InputData;
import lacerda.luhan.sincreceita.model.InputHeaders;
import lacerda.luhan.sincreceita.model.OutputData;
import lacerda.luhan.sincreceita.model.OutputHeaders;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessCSVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCSVService.class);
    public static final char DELIMITER = ';';

    @Autowired
    private ReceitaService receitaService;

    private String errorMessage;

    public ProcessCSVService(){
        this.receitaService = new ReceitaService();
    }

    public List<InputData> read(String file) throws IOException {
        LOGGER.info("Iniciando a leitura do arquivo");
        Reader in = new FileReader(file);

        Iterable<CSVRecord> records = CSVFormat.newFormat(';')
                .withHeader(InputHeaders.class)
                .withFirstRecordAsHeader()
                .parse(in);

        List<InputData> inputs = new ArrayList<>();
        records.forEach(record -> {
            InputData data = new InputData(record);
            inputs.add(data);
        });

        LOGGER.info("Finalizando a leitura do arquivo");

        return inputs;
    }

    public void createCSVFile(List<InputData> inputsRecords, String file) {
        LOGGER.info("Iniciando a escrita do arquivo");
        FileWriter out = null;
        try {
            out = new FileWriter(file);
        } catch (IOException e) {
            errorMessage = "Erro ao tentar acessar o path do arquivo: " + e.getMessage();
            LOGGER.info(errorMessage);
        }
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withQuote(null).withDelimiter(DELIMITER)
                .withHeader(OutputHeaders.class))) {

            for (InputData input : inputsRecords) {
                OutputData output = new OutputData(input);
                output.preencheRetorno(preencheRetornoReceita(output));
                String value = decimalFormat(output.getSaldo());
                printer.printRecord(output.getAgencia(), output.getConta(), value, output.getStatus(), output.getRetorno());
            }
            LOGGER.info("Finalizando a escrita do arquivo");
        } catch (Exception e) {
            errorMessage = "Erro ao criar o arquivo .csv: " + e.getMessage();
            LOGGER.error(errorMessage);
            e.printStackTrace();
        }
    }

    private boolean preencheRetornoReceita(OutputData output) {
        try {
            LOGGER.info("Realizando chamada ao servico da Receita para agencia: ".concat(output.getAgencia()).concat(" e conta: ").concat(output.getConta()));
            return receitaService.atualizarConta(output.getAgencia(), output.getConta().replace("-", ""), output.getSaldo(), output.getStatus());
        } catch (InterruptedException e) {
            errorMessage = "Erro ao consumir servico da receita: " + e.getMessage();
            LOGGER.error(errorMessage);
            return false;
        }
    }

    private String decimalFormat(double number) {
        DecimalFormat df = new DecimalFormat("0.##");
        df.setMinimumFractionDigits(2);
        return df.format(number);
    }

}
