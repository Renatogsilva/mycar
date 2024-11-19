package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumExchange {
    MANUAL(1, "Manual"),
    AUTOMATIC(2, "Automático");

    private Integer code;
    private String description;

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    EnumExchange(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EnumExchange get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumExchange e : EnumExchange.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
