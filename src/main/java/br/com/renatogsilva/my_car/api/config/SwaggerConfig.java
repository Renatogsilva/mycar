package br.com.renatogsilva.my_car.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String serverPort;

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
                        .license(new License()))
                .addServersItem(new Server().url("https://mycar-prod.up.railway.app").description("Servidor de Produção"))
                .addServersItem(new Server().url("http://localhost:" + this.serverPort).description("Servidor de Desenvolvimento"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("jwt")));
    }
}
