package br.com.renatogsilva.my_car.model.dto.car;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class CarResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long carId;
    private String mark;
    private Integer yearOfManufacture;
    private String color;
    private String bodyStyle;
    private String enumExchangeDescription;
    private String engine;
    private String version;
}