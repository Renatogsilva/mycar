package br.com.renatogsilva.my_car.model.dto;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDTO implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    private String mark;

    @NonNull
    private Integer yearOfManufacture;
    private String color;

    @NonNull
    private String bodyStyle;

    @NonNull
    @Schema(description = "Tipo de câmbio", example = "1", allowableValues = "1 - MANUAL, 2 - AUTOMATIC")
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
