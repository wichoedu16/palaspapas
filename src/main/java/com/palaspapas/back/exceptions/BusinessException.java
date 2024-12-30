package com.palaspapas.back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando se viola una regla de negocio en la aplicación.
 * Se utiliza para indicar errores que están relacionados con la lógica del negocio
 * y no con errores técnicos o de sistema.
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, "BUS-400", HttpStatus.BAD_REQUEST);
    }
}
