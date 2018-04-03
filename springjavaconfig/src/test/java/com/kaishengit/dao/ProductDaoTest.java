package com.kaishengit.dao;

import com.kaishengit.BaseTestCase;
import com.kaishengit.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductDaoTest extends BaseTestCase {
    @Autowired
    private ProductDao productDao;
    @Test
    public void save() {
        Product product = new Product("电脑显卡",100L);
        productDao.save(product);
    }

    @Test
    public void findById() {
        Product product = productDao.findById(1L);
        System.out.println(product);
    }

    @Test
    public void findAll() {

        List<Product> products = productDao.findAll();
        for(Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void batchSave() {
        List<Product> productList = Arrays.asList(
                new Product("苹果",600L),
                new Product("华为",100L)
        );
        productDao.batchSave(productList);
    }


    @Test
    public void update() {
        Product product = productDao.findById(1L);
        product.setName("机械硬盘");
        product.setInventory(500L);

        productDao.update(product);
    }

    @Test
    public void count() {
        Long products = productDao.countAll();
        System.out.println(products);

    }


    @Test
    //一个List集合里面包含了多个Map集合；一个Map集合里面有多个键值对，共同组成一个对象；
    public void findAllMap() {
        List<Map<String,Object>> list = productDao.findAllToMap();

        for(Map<String,Object> map : list) {
            for(Map.Entry entry : map.entrySet()) {
                System.out.println(entry.getKey() +"--->"+ entry.getValue());

            }
            System.out.println("---------------------------");
        }


    }


}
