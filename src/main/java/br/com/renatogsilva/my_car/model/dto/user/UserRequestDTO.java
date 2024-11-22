package br.com.renatogsilva.my_car.model.dto.user;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class UserRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private PersonRequestDTO personRequestDTO;
}