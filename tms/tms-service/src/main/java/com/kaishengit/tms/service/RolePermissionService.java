package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.exception.ServiceException;

import java.util.List;

/*
 *角色和权限的业务类
 * @author 马得草  
 * @date 2018/4/13   
 */
public interface RolePermissionService {

    /*
     *添加权限
     * @author 马得草
     * @date 2018/4/13
     */
    void savePermission(Permission permission);


    /*   
     *查询所有权限
     * @author 马得草  
     * @date 2018/4/13   
     */
    List<Permission> findAllPermission();


    /*   
     *根据权限类型查找对应的列表
     * @author 马得草  
     * @date 2018/4/13   
     */
    List<Permission> findPermissionByPermissionType(String menuType);

    void saveRoles(Role role, Integer[] permissionId);

    /*
     *根据id删除对应的权限
     * @author 马得草
     * @date 2018/4/15
     */
    void delPermissionById(Integer id)throws ServiceException;

    /*
     *查询所有角色
     * @author 马得草
     * @date 2018/4/15
     */
    List<Role> findAllRole();


    Permission findPermissionById(Integer id);

    void updatePermission(Permission permission,Integer id);
}
