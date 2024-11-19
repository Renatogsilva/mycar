package br.com.renatogsilva.my_car.model.dto.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarRequestDTO implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String mark;

    @NonNull
    private Integer yearOfManufacture;
    private String color;

    @NonNull
    private String bodyStyle;

    @NonNull
    @Schema()
    private EnumExchange exchange;
    private String engine;
    private String version;

    public CarRequestDTO(Car car) {
        this.id = car.getId();
        this.mark = car.getMark();
        this.yearOfManufacture = car.getYearOfManufacture();
        this.color = car.getColor();
        this.bodyStyle = car.getBodyStyle();
        this.exchange = EnumExchange.get(car.getExchange());
        this.engine = car.getEngine();
        this.version = car.getVersion();
    }
}
