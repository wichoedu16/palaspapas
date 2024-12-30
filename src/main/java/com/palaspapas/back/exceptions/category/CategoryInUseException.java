package com.palaspapas.back.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryInUseException extends CategoryException {
    public CategoryInUseException(String message) {
        super(message);
    }
}