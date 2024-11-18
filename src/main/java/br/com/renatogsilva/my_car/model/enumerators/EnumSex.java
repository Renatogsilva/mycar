package br.com.renatogsilva.my_car.model.enumerators;

import lombok.Getter;

@Getter
public enum EnumSex {
    MALE(1, "Masculino"),
    FEMALE(2, "Feminino");

    private Integer cod;
    private String description;

    EnumSex(Integer cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public static EnumSex get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumSex e : EnumSex.values()) {
            if (e.cod.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
