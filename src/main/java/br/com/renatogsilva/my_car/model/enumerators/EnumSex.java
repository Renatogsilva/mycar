package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumSex {
    MALE(1, "Masculino"),
    FEMALE(2, "Feminino");

    private Integer code;
    private String description;

    EnumSex(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static EnumSex get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumSex e : EnumSex.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
