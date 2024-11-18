package br.com.renatogsilva.my_car.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de cadastro de veículos").version("1.0.0")
                        .version("1.0")
                        .description("API para cadastro de veículos sob 4 rodas")
                        .contact(new Contact()
                                .name("renatogsilva")
                                .email("silvarenato180@gmail.com")
                                .url("https://renatogsilva.com"))
                        .license(new License()));
    }
}
