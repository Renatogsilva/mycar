package br.com.renatogsilva.my_car.model.dto.person;

import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long personId;

    @NotBlank(message = "Campo primeiro nome é obrigatório")
    @Size(min = 5, max = 255, message = "O valor do campo primeiro nome deve conter no mínimo 5 e no máximo 255 caractere")
    private String firstName;
    private String lastName;

    @NotBlank(message = "Campo e-mail é obrigatório")
    @Email(message = "E-mail informado é inválido")
    private String email;

    @NotBlank(message = "Campo cpf é obrigatório")
    @CPF(message = "Cpf informado é inválido")
    private String cpf;

    @NotNull(message = "Campo sexo é obrigatório")
    private EnumSex sex;
    private LocalDate birthDate;

    @Valid
    private List<PhoneRequestDTO> phonesRequestDTOs;
}
