package br.com.renatogsilva.my_car.model.exceptions.car;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectNotFoundException;
import lombok.Data;

@Data
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
