package br.com.renatogsilva.my_car.model.dto.person;

import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class PersonRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private Integer sex;
    private LocalDate birthDate;
    private List<PhoneRequestDTO> phonesRequestDTOs;
}
