package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumMessageCarExceptions {
    CAR_NOT_FOUND(404, "Veículo não encontrado"),
    INVALID_PARAMETER(400, "Parâmetro informado é inválido"),
    CAR_DUPLICATE(409, "Já existe um veículo cadastrado na base com essas características");

    private Integer code;
    private String message;

    private EnumMessageCarExceptions(Integer code, String message) {
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

    public static EnumMessageCarExceptions get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumMessageCarExceptions e : EnumMessageCarExceptions.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
