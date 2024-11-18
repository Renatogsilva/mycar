package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.dto.CarDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import jakarta.persistence.*;
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

    @Column(length = 100, nullable = false, unique = true)
    @NonNull
    private String mark;

    @Column(name = "year_manufacture", nullable = false)
    private Integer yearOfManufacture;
    private String color;

    @Column(name = "body_style")
    private String bodyStyle;
    @Column(length = 1, nullable = false)
    private EnumExchange exchange;
    private String engine;
    private String version;
    private EnumStatus status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "exclusion_date")
    private LocalDate exclusionDate;

    public Car(CarDTO carDTO) {
        this.id = carDTO.getId();
        this.mark = carDTO.getMark();
        this.yearOfManufacture = carDTO.getYearOfManufacture();
        this.color = carDTO.getColor();
        this.bodyStyle = carDTO.getBodyStyle();
        this.exchange = carDTO.getExchange();
        this.engine = carDTO.getEngine();
        this.version = carDTO.getVersion();
    }
}
