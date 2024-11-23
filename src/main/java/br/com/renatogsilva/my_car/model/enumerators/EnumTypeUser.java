package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumTypeUser {
    ADMIN(1, "Admin"),
    USER(2, "User"),
    NOT_PERMISION(3, "Not Permission");

    private Integer code;
    private String description;

    EnumTypeUser(Integer code, String description) {
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

    public static EnumTypeUser get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumTypeUser e : EnumTypeUser.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
