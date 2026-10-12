package br.com.renatogsilva.my_car.api.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        description = "Perfil do usuário",
        schema = @Schema(
                type = "string",
                allowableValues = {
                        "ADMIN",
                        "USER",
                        "NOT_PERMISSION"
                }
        )
)
public @interface TypeUserParameter {
}
