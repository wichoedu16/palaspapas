package com.palaspapas.back.infraestructure.abstract_services;

import com.palaspapas.back.api.models.request.CategoryRequest;
import com.palaspapas.back.api.models.response.CategoryResponse;

public interface ICategoryService extends  CrudService<CategoryRequest, CategoryResponse, Long>{
}
