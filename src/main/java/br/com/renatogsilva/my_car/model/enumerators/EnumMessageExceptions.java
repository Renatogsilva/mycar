package br.com.renatogsilva.my_car.model.enumerators;

public enum EnumMessageExceptions {
    CAR_NOT_FOUND(404, "Veículo não encontrado"),
    INVALID_PARAMETER(400, "Parâmetro inválido"),
    CAR_DUPLICATE(409, "Já existe um veículo cadastrado na base com essas caracteristicas");

    private Integer code;
    private String message;

    private EnumMessageExceptions(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static EnumMessageExceptions get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumMessageExceptions e : EnumMessageExceptions.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
