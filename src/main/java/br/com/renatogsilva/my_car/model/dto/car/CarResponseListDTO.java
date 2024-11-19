package br.com.renatogsilva.my_car.model.dto.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class CarResponseListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String mark;
    private Integer yearOfManufacture;
    private String engine;
    private String version;

    public CarResponseListDTO(Car car) {
        this.id = car.getId();
        this.mark = car.getMark();
        this.yearOfManufacture = car.getYearOfManufacture();
        this.engine = car.getEngine();
        this.version = car.getVersion();
    }
}
