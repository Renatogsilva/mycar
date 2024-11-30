package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
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
    private Long userId;

    @Column(nullable = false, length = 255)
    private String username;

    @Column(nullable = false, length = 255, name = "user_password")
    private String password;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "is_primary_access", nullable = false)
    private boolean isPrimaryAccess;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatus status;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EnumTypeUser typeUser;

    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_person_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_person"))
    @ToString.Exclude
    private Person person;
}
