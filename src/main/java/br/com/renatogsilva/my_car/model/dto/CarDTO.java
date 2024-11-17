package br.com.renatogsilva.my_car.model.dto;

import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDTO implements Serializable {

    private Long id;
    private String mark;
    private Integer yearOfManufacture;
    private String color;
    private String bodyStyle;
    private EnumExchange exchange;
    private String engine;
    private String version;
}
