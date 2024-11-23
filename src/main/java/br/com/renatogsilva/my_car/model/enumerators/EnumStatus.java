package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumStatus {
    ACTIVE(1, "Ativo"),
    INACTIVE(2, "Inativo"),
    DELETED(3, "Deletado");

    private Integer code;
    private String description;

    EnumStatus(Integer code, String description) {
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
}
