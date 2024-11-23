package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumMessageGenericExceptions {
    INVALID_PARAMETER(400, "Parâmetro inválido"),;

    private Integer code;
    private String message;

    private EnumMessageGenericExceptions(Integer code, String message) {
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

    public static EnumMessageGenericExceptions get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumMessageGenericExceptions e : EnumMessageGenericExceptions.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
