package usco.edu.co.parcial2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clinicaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinica - Gestion de consultas medicas")
                        .version("1.0")
                        .description("Servicios web para administrar medicos, consultorios y consultas con roles ADMINISTRADOR, MEDICO y PACIENTE."));
    }
}

