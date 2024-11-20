package br.com.renatogsilva.my_car.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StandartError implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private int code;
    private LocalDateTime timeStamp;
}