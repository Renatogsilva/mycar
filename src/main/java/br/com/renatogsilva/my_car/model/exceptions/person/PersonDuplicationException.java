package br.com.renatogsilva.my_car.model.exceptions.person;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectDuplicationException;
import lombok.Data;

@Data
public class PersonDuplicationException extends ObjectDuplicationException {
    private int code;

    public PersonDuplicationException(String message) {
        super(message);
    }

    public PersonDuplicationException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
