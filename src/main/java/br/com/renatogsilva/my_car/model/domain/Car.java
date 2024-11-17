package br.com.renatogsilva.my_car.model.domain;

import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
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
@Table(name = "tb_car")
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mark;
    private Integer yearOfManufacture;
    private String color;
    private String bodyStyle;
    private EnumExchange exchange;
    private String engine;
    private String version;
    private LocalDate creationDate;
    private LocalDate exclusionDate;
}
