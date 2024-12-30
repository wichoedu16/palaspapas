package com.palaspapas.back.exceptions.category;

import com.palaspapas.back.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryException extends BaseException {
        public CategoryException(String message) {
            super(message, "CATEGORY_ERROR", HttpStatus.BAD_REQUEST);
        }
}
