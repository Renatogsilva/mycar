package br.com.renatogsilva.my_car.model.dto.phone;

import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class PhoneRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long phoneId;

    @NotBlank(message = "Campo número é obrigatório")
    @NumberFormat(pattern = "(##)#####-####")
    private String number;

    @NotNull(message = "Campo tipo telefone é obrigatório")
    private EnumTypePhone typePhone;

    @NotNull(message = "Campo principal é obrigatório")
    private Boolean isMain;
}
