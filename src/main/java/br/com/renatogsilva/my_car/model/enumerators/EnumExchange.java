package br.com.renatogsilva.my_car.model.enumerators;

import lombok.Getter;

@Getter
public enum EnumExchange {
    MANUAL(1, "Manual"),
    AUTOMATIC(2, "Automático");

    private Integer cod;
    private String description;

    EnumExchange(Integer cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public static EnumExchange get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumExchange e : EnumExchange.values()) {
            if (e.cod.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
