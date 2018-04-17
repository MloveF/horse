package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.ProductType;

import java.util.List;
import java.util.Map;

public interface ProductService {


    PageInfo findAllProductByPageNoAndQueryParam(Integer pageNo, Map<String, Object> queryParamMap);

    List<ProductType> findAllProductType();
}
