package br.com.renatogsilva.my_car.model.exceptions.basic;

import lombok.Getter;

@Getter
public class ObjectDuplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    public ObjectDuplicationException() {}

    public ObjectDuplicationException(String message) {
        super(message);
    }
    public ObjectDuplicationException(int code) {}

    public ObjectDuplicationException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ObjectDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
