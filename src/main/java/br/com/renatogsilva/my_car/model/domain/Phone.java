package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_phone")
public class Phone implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "phone_id")
    private Long phoneId;

    @Column(length = 14, nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_phone", nullable = false, length = 20)
    private EnumTypePhone typePhone;

    @Column(name = "is_main", nullable = true)
    private Boolean isMain;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_phone_person"))
    @ToString.Exclude
    private Person person;
}