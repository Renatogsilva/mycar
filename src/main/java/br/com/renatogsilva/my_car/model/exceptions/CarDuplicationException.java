package br.com.renatogsilva.my_car.model.exceptions;

import lombok.Getter;

@Getter
public class CarDuplicationException extends ObjectDuplicationException {
    private int code;

    public CarDuplicationException(String message) {
        super(message);
    }

    public CarDuplicationException(int code, String message) {
        super(message, code);
    }
}
