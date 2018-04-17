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
     *根据id查找权限
     * @author 马得草
     * @date 2018/4/16
     */
    Permission findPermissionById(Integer id);

    /*
     *编辑权限
     * @author 马得草
     * @date 2018/4/16
     */
    void updatePermission(Permission permission,Integer id);

    /*
     *查询所有的角色并加载角色拥有的权限列表
     * @author 马得草
     * @date 2018/4/16
     */
    List<Role> findAllRolesWithPermission();

    /*
     *查找所有权限
     * @author 马得草
     * @date 2018/4/16
     */
    List<Role> findAllRoles();


    /*
     *根据主键查找角色
     * @author 马得草
     * @date 2018/4/17
     */
   Role findById(Integer id);

    /**
     * 根据角色ID查询角色对象及其拥有的权限
     * @param id
     * @return
     */
    Role findRolesWithPermissionById(Integer id);

    /*
     *修改角色对象
     * @author 马得草
     * @date 2018/4/17
     */
    void updateRoles(Role roles, Integer[] permissionId);

    /*
     *根据ID删除角色
     * @author 马得草
     * @date 2018/4/17
     */
    void delRolesById(Integer id);

    /*
     *根据accountID查询对应的角色集合
     * @author 马得草
     * @date 2018/4/17
     */
    List<Role> findRolesByAccountId(Integer id);
}
