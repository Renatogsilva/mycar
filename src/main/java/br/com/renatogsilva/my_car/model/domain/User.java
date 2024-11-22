package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "is_primary_access", nullable = false)
    private boolean isPrimaryAccess;

    @Column(nullable = false)
    private Integer status;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    public User(UserRequestDTO userRequestDTO) {
        this.id = userRequestDTO.getId();
        this.username = userRequestDTO.getUsername();
    }
}
