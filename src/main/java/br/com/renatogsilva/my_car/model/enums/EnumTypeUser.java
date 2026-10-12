package br.com.renatogsilva.my_car.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum EnumTypeUser {
    ADMIN(1, "Admin"),
    USER(2, "User"),
    NOT_PERMISSION(3, "Not Permission");

    private final Integer code;
    @Getter
    private final String description;

    EnumTypeUser(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public Integer getCode() {
        return code;
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
