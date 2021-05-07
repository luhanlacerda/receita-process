package lacerda.luhan.sincreceita;

import lacerda.luhan.sincreceita.config.AppConfig;
import lacerda.luhan.sincreceita.model.InputData;
import lacerda.luhan.sincreceita.service.ProcessCSVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/*
Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de
contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi
já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal,
antes as 10:00 da manhã na abertura das agências.

Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

Funcionalidade:
0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
1. Processa um arquivo CSV de entrada com o formato abaixo.
2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma
nova coluna.


Formato CSV:
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B
...

*/
@SpringBootApplication
public class SincReceitaApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SincReceitaApplication.class);

    @Autowired
    private AppConfig config;

    @Autowired
    private ProcessCSVService process;

    public static void main(String[] args) {
        SpringApplication.run(SincReceitaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String file = args[0].isEmpty() ? config.getFilePath().concat(config.getFilename()) : args[0];
        LOGGER.info("Iniciando a aplicacao");
        List<InputData> list = process.read(file);
        process.createCSVFile(list, file);
        LOGGER.info("Finalizando a aplicacao");
    }


}
