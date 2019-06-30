package com.itheima.ssm.dao;


import com.itheima.ssm.domain.Product;
import com.itheima.ssm.domain.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductDao {

    //查询所有的产品信息
    @Select("select * from product")
    List<Product> findAll() throws Exception;

    @Insert("insert into product (productNum,productName,cityName,departureTime,productPrice,productDesc,productStatus) values(#{productNum},#{productName},#{cityName},#{departureTime},#{productPrice},#{productDesc},#{productStatus})")
    void save(Product product) throws Exception;

    @Select("select * from product where id=#{id}")
    Product findById(String id);
}
