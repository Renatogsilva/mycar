package br.com.renatogsilva.my_car.model.dto.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long expiresIn;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
