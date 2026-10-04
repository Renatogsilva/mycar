package br.com.renatogsilva.my_car.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum EnumStatus {
    ACTIVE(1, "Ativo"),
    INACTIVE(2, "Inativo"),
    DELETED(3, "Deletado");

    private final Integer code;
    @Getter
    private final String description;

    EnumStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public static EnumStatus get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumStatus e : EnumStatus.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }

    public static EnumStatus getByDescription(String description) {
        if (description == null) {
            return null;
        }

        for (EnumStatus status : EnumStatus.values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Descrição inválida: " + description
        );
    }
}
