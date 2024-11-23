package br.com.renatogsilva.my_car.model.exceptions.user;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectDuplicationException;
import lombok.Data;

@Data
public class UserDuplicationException extends ObjectDuplicationException {
    private int code;

    public UserDuplicationException(String message) {
        super(message);
    }

    public UserDuplicationException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
