package br.com.renatogsilva.my_car.model.enumerators;

import lombok.Getter;

@Getter
public enum EnumTypePhone {
    FIXED(1, "Fixo"),
    RESIDENTIAL(2, "Residencial"),
    CELL_PHONE(3, "Celular"),
    COMMERCIAL(4, "Comercial"),;

    private Integer code;
    private String description;

    EnumTypePhone(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EnumTypePhone get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumTypePhone e : EnumTypePhone.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
