package lacerda.luhan.sincreceita.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
@Setter
public class AppConfig {

    @Value("${file.path}")
    private String filePath;

    @Value("${file.filename}")
    private String filename;

}
