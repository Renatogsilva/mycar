package br.com.renatogsilva.my_car.model.enumerators;

import lombok.Getter;

@Getter
public enum EnumStatus {
    ACTIVE(1, "Ativo"),
    INACTIVE(2, "Inativo");

    private Integer cod;
    private String description;

    EnumStatus(Integer cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public static EnumStatus get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumStatus e : EnumStatus.values()) {
            if (e.cod.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
