package br.com.renatogsilva.my_car.model.dto.person;

import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
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
public class PersonResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long personId;
    private String fullName;
    private String email;
    private String cpf;
    private EnumSex sex;
    private LocalDate birthDate;
    private List<PhoneResponseDTO> phonesResponseDTOs;
}
