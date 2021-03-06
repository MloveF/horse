package com.kaishengit.tms.mapper;

import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.entity.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Integer insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> findAllWithPermission();

    Role findById(Integer id);

    Role findByIdWithPermission(Integer id);

    List<Role> findRolesByAccountId(Integer id);
}