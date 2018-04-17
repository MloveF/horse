package com.kaishengit.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Product;
import com.kaishengit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1",name = "p",required = false) Integer pageNo,
                       @RequestParam(required = false) String productName,
                       @RequestParam(required = false) String place,
                       @RequestParam(required = false) Float minPrice,
                       @RequestParam(required = false) Float maxPrice,
                       @RequestParam(required = false) Integer typeId,
                       Model model) {
        //将搜索添加封装到Map集合中
        Map<String,Object> queryParamMap = new HashMap<>();
        queryParamMap.put("productName",productName);
        queryParamMap.put("place",place);
        queryParamMap.put("minPrice",minPrice);
        queryParamMap.put("maxPrice",maxPrice);
        queryParamMap.put("typeId",typeId);

        PageInfo<Product> pageInfo = productService.findAllProductByPageNoAndQueryParam(pageNo,queryParamMap);

        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("typeList",productService.findAllProductType());
        return "product/list";
    }


}
