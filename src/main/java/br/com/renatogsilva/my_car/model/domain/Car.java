package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
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
    private Long id;

    @Column(length = 100, nullable = false)
    private String mark;

    @Column(name = "year_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @Column(length = 50, nullable = false)
    private String color;

    @Column(length = 50, name = "body_style", nullable = false)
    private String bodyStyle;

    @Column(nullable = false)
    private Integer exchange;

    @Column(length = 20, nullable = false)
    private String engine;

    @Column(length = 20, nullable = false)
    private String version;

    @NotNull
    private Integer status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "exclusion_date")
    private LocalDate exclusionDate;

    public Car(CarRequestDTO carRequestDTO) {
        this.id = carRequestDTO.getId();
        this.mark = carRequestDTO.getMark();
        this.yearOfManufacture = carRequestDTO.getYearOfManufacture();
        this.color = carRequestDTO.getColor();
        this.bodyStyle = carRequestDTO.getBodyStyle();
        this.exchange = carRequestDTO.getExchange().getCode();
        this.engine = carRequestDTO.getEngine();
        this.version = carRequestDTO.getVersion();
    }
}
