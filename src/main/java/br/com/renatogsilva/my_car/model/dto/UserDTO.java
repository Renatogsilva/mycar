package br.com.renatogsilva.my_car.model.dto;

import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String cpf;
    private EnumSex sex;
    private LocalDate birthday;
}
