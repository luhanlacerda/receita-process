package lacerda.luhan.sincreceita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputData {

    private String agencia;
    private String conta;
    private Double saldo;
    private String status;
    private String retorno;

    public OutputData(InputData input) {
        this.setAgencia(input.getAgencia());
        this.setConta(input.getConta());
        this.setSaldo(input.getSaldo());
        this.setStatus(input.getStatus());
    }

    public void preencheRetorno(Boolean retorno) {
        if (retorno)
            this.retorno = "Enviado";
        else
            this.retorno = "Não Enviado";
    }

}
