package com.kaishengit.mapper;

import com.kaishengit.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    List<Product> findAllWithTypeByQueryParam(Map<String, Object> queryParamMap);
}
