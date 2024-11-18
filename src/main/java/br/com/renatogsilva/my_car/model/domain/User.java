package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.dto.UserDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String cpf;
    private EnumSex sex;
    private LocalDate birthday;
    private LocalDate registrationDate;
    private EnumStatus status;

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.cpf = userDTO.getCpf();
        this.sex = userDTO.getSex();
        this.birthday = userDTO.getBirthday();
    }
}
