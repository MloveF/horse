package com.kaishengit.dao;


import com.kaishengit.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    //NamedParameterJdbcTemplate和JdbcTemplate方法差不多  它支持引用占位符
    // 占位符要求必须使用:开头 后面的属性为put到Map集合时的键值
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(Product product) {
        String sql = "insert into Product(name,inventory) values(?,?)";
        jdbcTemplate.update(sql,product.getName(),product.getInventory());
    }

    public void batchSave(List<Product> products) {
        String sql = "insert into product(name,inventory) values(?,?)";

        List<Object[]> params = new ArrayList<Object[]>();
        for(Product product : products) {
            Object[] objects = new Object[2];
            objects[0] = product.getName();
            objects[1] = product.getInventory();
            params.add(objects);
        }

        jdbcTemplate.batchUpdate(sql,params);
    }
    public Product findById(Long id) {
        String sql = "select * from Product where id = ?";
        //queryForObject 查询单挑记录
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Product>(Product.class),id);
    }

    public List<Product> findAll() {
        String sql = "select * from Product";
        //BeanPropertyRowMapper将数据库一条记录映射为实体类对象 query查询多条记录
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Product>(Product.class));
    }

    //这是用jdbcTemplate类创建的更新
 /*   public void update(Product product) {
        String sql = "update Product set name = ?,inventory=? where id = ?";
        jdbcTemplate.update(sql,product.getName(),product.getInventory(),product.getId());
    }*/



 //这是使用NamedparameterjabcTmplate创建的更新
    public void update(Product product) {

        String sql = "update Product set name = :name,inventory = :inventory where id = :id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name",product.getName());
        params.put("inventory",product.getInventory());
        params.put("id",product.getId());

        namedParameterJdbcTemplate.update(sql,params);
    }

    public Long countAll() {
        String sql = "select count(*) from Product";
        //SingleColumnRowMapper获取数据库中单行单列值
        return jdbcTemplate.queryForObject(sql,new SingleColumnRowMapper<Long>(Long.class));
    }


    public List<Map<String,Object>> findAllToMap() {
        String sql = "select * from product";
//ColumnMapRowMapper 将数据库中的一条记录了映射为一个Map集合 数据库的列名作为Map的key数据库的值为Map的value值
        return jdbcTemplate.query(sql,new ColumnMapRowMapper());
    }

}
