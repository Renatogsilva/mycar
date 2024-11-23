package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumMessagePersonExceptions {
    CAR_DUPLICATE(409, "Já existe um registro cadastrado na base com essas informações");

    private Integer code;
    private String message;

    private EnumMessagePersonExceptions(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static EnumMessagePersonExceptions get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumMessagePersonExceptions e : EnumMessagePersonExceptions.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
