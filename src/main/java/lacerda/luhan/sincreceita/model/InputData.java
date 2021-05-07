package lacerda.luhan.sincreceita.model;

import lombok.*;
import org.apache.commons.csv.CSVRecord;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InputData {

    private String agencia;
    private String conta;
    private Double saldo;
    private String status;

    public InputData(CSVRecord csvRecord) {
        this.setAgencia(csvRecord.get("agencia"));
        this.setConta(csvRecord.get("conta"));
        this.setSaldo(Double.valueOf(csvRecord.get("saldo").replace(",", ".")));
        this.setStatus(csvRecord.get("status"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputData inputData = (InputData) o;

        if (!Objects.equals(agencia, inputData.agencia)) return false;
        if (!Objects.equals(conta, inputData.conta)) return false;
        if (!Objects.equals(saldo, inputData.saldo)) return false;
        return Objects.equals(status, inputData.status);
    }

    @Override
    public int hashCode() {
        int result = agencia != null ? agencia.hashCode() : 0;
        result = 31 * result + (conta != null ? conta.hashCode() : 0);
        result = 31 * result + (saldo != null ? saldo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
