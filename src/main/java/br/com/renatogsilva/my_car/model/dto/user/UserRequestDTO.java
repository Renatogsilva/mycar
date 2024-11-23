package br.com.renatogsilva.my_car.model.dto.user;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long userId;

    @NotBlank(message = "Campo login é obrigatório")
    @Size(min = 5, max = 255, message = "O valor do campo login deve conter no mínimo 5 e no máximo 255 caractere")
    private String username;

    @Valid
    private PersonRequestDTO personRequestDTO;
}
