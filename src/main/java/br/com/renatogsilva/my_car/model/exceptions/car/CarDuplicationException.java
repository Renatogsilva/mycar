package br.com.renatogsilva.my_car.model.exceptions.car;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectDuplicationException;
import lombok.Data;

@Data
public class CarDuplicationException extends ObjectDuplicationException {
    private int code;

    public CarDuplicationException(String message) {
        super(message);
    }

    public CarDuplicationException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}