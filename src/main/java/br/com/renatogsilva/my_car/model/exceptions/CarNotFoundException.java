package br.com.renatogsilva.my_car.model.exceptions;

import lombok.Getter;

@Getter
public class CarNotFoundException extends ObjectNotFoundException {
    private int code;

    public CarNotFoundException(String message) {
        super(message);
    }

    public CarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarNotFoundException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
