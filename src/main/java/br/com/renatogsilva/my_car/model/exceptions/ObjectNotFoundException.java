package br.com.renatogsilva.my_car.model.exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    public ObjectNotFoundException() {}

    public ObjectNotFoundException(String message) {
        super(message);
    }
    public ObjectNotFoundException(int code) {}

    public ObjectNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
