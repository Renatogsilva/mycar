package br.com.renatogsilva.my_car.model.dto.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class CarResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String mark;
    private Integer yearOfManufacture;
    private String color;
    private String bodyStyle;
    private String enumExchangeDescription;
    private String engine;
    private String version;

    public CarResponseDTO(Car car) {
        this.id = car.getId();
        this.mark = car.getMark();
        this.yearOfManufacture = car.getYearOfManufacture();
        this.color = car.getColor();
        this.bodyStyle = car.getBodyStyle();
        this.engine = car.getEngine();
        this.version = car.getVersion();
        this.enumExchangeDescription = EnumExchange.get(car.getExchange()).getDescription();
    }
}