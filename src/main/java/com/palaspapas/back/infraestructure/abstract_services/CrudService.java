package com.palaspapas.back.infraestructure.abstract_services;

public interface CrudService<REQUEST, RESPONSE, Long> {
    RESPONSE create(REQUEST request);

    RESPONSE read(Long id);

    RESPONSE update(REQUEST request, Long id);

    void delete(Long id);
}
