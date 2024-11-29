package br.com.renatogsilva.my_car.model.dto.user;

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
public class UserProfileRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotBlank(message = "Campo login é obrigatório")
    private String username;

    @NotBlank(message = "Campo e-mail é obrigatório")
    private String email;

    @NotBlank(message = "Campo senha atual é obrigatório")
    private String currentPassword;

    @NotBlank(message = "Campo nova senha é obrigatório")
    private String newPassword;
}
