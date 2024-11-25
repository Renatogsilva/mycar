package br.com.renatogsilva.my_car.model.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class LoginRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Campo login é obrigatório")
    private String username;

    @NotBlank(message = "Campo senha é obrigatório")
    private String password;
}
