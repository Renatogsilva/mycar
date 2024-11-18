package br.com.renatogsilva.my_car.model.dto;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDTO implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private String mark;
    private Integer yearOfManufacture;
    private String color;
    private String bodyStyle;
    private EnumExchange exchange;
    private String enumExchangeDescription;
    private String engine;
    private String version;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.mark = car.getMark();
        this.yearOfManufacture = car.getYearOfManufacture();
        this.color = car.getColor();
        this.bodyStyle = car.getBodyStyle();
        this.exchange = car.getExchange();
        this.engine = car.getEngine();
        this.version = car.getVersion();
        this.enumExchangeDescription = car.getExchange().getDescription();
    }
}
