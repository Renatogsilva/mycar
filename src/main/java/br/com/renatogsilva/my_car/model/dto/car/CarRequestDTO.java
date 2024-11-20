package br.com.renatogsilva.my_car.model.dto.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Campo marca do veículo é obrigatório")
    @Max(value = 100, message = "O valor do campo cor excedeu o limite de 100 caracteres")
    private String mark;

    @Min(value = 2000, message = "Ano de fabricação deve ser maior ou igual á 2000")
    @Positive(message = "Valor do campo ano de fabricação deve ser positivo")
    private Integer yearOfManufacture;

    @NotBlank(message = "Campo cor do veículo é obrigatório")
    @Min(value = 3, message = "Campo cor deve conter no mínimo 3 caracteres")
    @Max(value = 50, message = "O valor do campo cor excedeu o limite de 50 caracteres")

    private String color;
    @NotBlank(message = "Campo estilo do carro é obrigatório")
    @Min(value = 4, message = "Campo estilo do carro deve conter no mínimo 3 caracteres")
    @Max(value = 50, message = "O valor do campo cor excedeu o limite de 50 caracteres")

    private String bodyStyle;
    @NotNull(message = "Campo tipo de câmbio é obrigatório")

    private EnumExchange exchange;
    @NotBlank(message = "Campo motor é obrigatório")
    @Min(value = 3, message = "Campo motor deve conter no mínimo 3 caracteres")
    @Max(value = 50, message = "O valor do campo cor excedeu o limite de 50 caracteres")

    private String engine;
    @NotBlank(message = "Campo versão do carro é obrigatório")
    @Min(value = 3, message = "Campo versão do carro deve conter no mínimo 3 caracteres")
    @Max(value = 50, message = "O valor do campo cor excedeu o limite de 50 caracteres")
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
