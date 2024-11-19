package br.com.renatogsilva.my_car.model.enumerators;

import lombok.Getter;

@Getter
public enum EnumStatus {
    ACTIVE(1, "Ativo"),
    INACTIVE(2, "Inativo");

    private Integer code;
    private String description;

    EnumStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
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
