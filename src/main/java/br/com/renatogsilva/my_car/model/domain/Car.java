package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity
@Table(name = "tb_car")
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "car_id")
    private Long carId;

    @Column(length = 100, nullable = false)
    private String mark;

    @Column(name = "year_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @Column(length = 50, nullable = false)
    private String color;

    @Column(length = 50, name = "body_style", nullable = false)
    private String bodyStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumExchange exchange;

    @Column(length = 20, nullable = false)
    private String engine;

    @Column(length = 20, nullable = false)
    private String version;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EnumStatus status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "exclusion_date")
    private LocalDate exclusionDate;

    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_creation_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_car_user_creation"))
    private User userCreation;

    @OneToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_exclusion_id", nullable = true, unique = true, foreignKey = @ForeignKey(name = "fk_car_user_create_exclusion"))
    private User userExclusion;
}
