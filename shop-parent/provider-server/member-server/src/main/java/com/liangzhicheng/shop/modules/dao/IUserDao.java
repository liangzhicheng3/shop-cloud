package com.liangzhicheng.shop.modules.dao;

import com.liangzhicheng.shop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IUserDao {

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User getById(Long id);

}
