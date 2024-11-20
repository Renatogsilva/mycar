package br.com.renatogsilva.my_car.model.dto.phone;

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
public class PhoneRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long phoneId;
    private String number;
    private Integer typePhone;
    private boolean isMain;
    private PersonRequestDTO personRequestDTO;
}
