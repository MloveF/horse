package com.kaishengit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Product;
import com.kaishengit.entity.ProductType;
import com.kaishengit.mapper.ProductMapper;
import com.kaishengit.mapper.ProductTypeMapper;
import com.kaishengit.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;


    @Override
    public PageInfo findAllProductByPageNoAndQueryParam(Integer pageNo, Map<String, Object> queryParamMap) {
        PageHelper.startPage(pageNo,10);
        List<Product> productList = productMapper.findAllWithTypeByQueryParam(queryParamMap);

        return  new PageInfo<>(productList);
    }

    @Override
    public List<ProductType> findAllProductType() {
        return productTypeMapper.findAll();
    }
}
