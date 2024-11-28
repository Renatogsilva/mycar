package br.com.renatogsilva.my_car.model.enumerators;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumMessageUserExceptions {
    USER_NOT_FOUND(404, "Usuário não encontrado"),
    USER_DUPLICATE(409, "Usuário de acesso informado já existe"),
    CREDENTIALS_INVALID(401, "Usuário ou senha inválidos"),
    CURRENT_PASSWORD_INVALID(400, "Senha atual informada é inválida"),
    USERNAME_INVALID(400, "Login informado é inválido"),;

    private Integer code;
    private String message;

    private EnumMessageUserExceptions(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static EnumMessageUserExceptions get(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (EnumMessageUserExceptions e : EnumMessageUserExceptions.values()) {
            if (e.code.equals(cod)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
