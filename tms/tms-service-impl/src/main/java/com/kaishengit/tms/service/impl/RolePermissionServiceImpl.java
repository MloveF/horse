package com.kaishengit.tms.service.impl;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.PermissionMapper;
import com.kaishengit.tms.mapper.RoleMapper;
import com.kaishengit.tms.mapper.RolePremissionMapper;
import com.kaishengit.tms.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色和权限的业务类
 * @author fankay
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePremissionMapper rolePremissionMapper;

    /*
     *新增权限
     * @author 马得草
     * @date 2018/4/16
     */
    @Override
    public void savePermission(Permission permission) {

        permission.setCreateTime(new Date());
        permissionMapper.insertSelective(permission);
        logger.info("添加权限 {}", permission);

    }

    /*
     *查询所有权限
     * @author 马得草
     * @date 2018/4/13
     */
    @Override
    public List<Permission> findAllPermission() {
        PermissionExample permissionExample = new PermissionExample();
        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        List<Permission> resultList = new ArrayList<>();
        treeList(permissionList, resultList, 0);
        return resultList;
    }

    private void treeList(List<Permission> sourceList, List<Permission> endList, int parentId) {

        List<Permission> tempList = Lists.newArrayList(Collections2.filter(sourceList,permission -> permission.getParentId().equals(parentId)));
        for (Permission permission : tempList) {
            endList.add(permission);
            treeList(sourceList, endList, permission.getId());
        }

    }

    /*
     *根据权限类型查找对应的列表
     * @author 马得草
     * @date 2018/4/13
     */
    @Override
    public List<Permission> findPermissionByPermissionType(String menuType) {

        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(menuType);

        return permissionMapper.selectByExample(permissionExample);
    }


    /*
     *新增角色
     * @author 马得草
     * @date 2018/4/14
     * role 角色对象
     * permissionId 角色对应的权限Id
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveRoles(Role role, Integer[] permissionId) {


        role.setCreateTime(new Date());
        roleMapper.insertSelective(role);

        for(Integer perId : permissionId) {

            RolePremissionKey rolePremissionKey = new RolePremissionKey();
            rolePremissionKey.setPremissionId(perId);
            rolePremissionKey.setRoleId(role.getId());

            rolePremissionMapper.insert(rolePremissionKey);
        }
        logger.info("保存角色 {}",role);
    }

    /*
     *根据id删除对应的权限
     * @author 马得草
     * @date 2018/4/15
     * @ServiceException  删除失败 抛出异常
     */
    @Override
    public void delPermissionById(Integer id)throws ServiceException {

        //查询该权限是否有子节点
       PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andParentIdEqualTo(id);

        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);

        if(permissionList != null && !permissionList.isEmpty()) {

            throw new ServiceException("have Child nodes Delete failed");
        }

        //查询判断该权限是否被角色占用
        RolePremissionExample rolePremissionExample = new RolePremissionExample();
        rolePremissionExample.createCriteria().andPremissionIdEqualTo(id);

        List<RolePremissionKey> rolePremissionKeyList = rolePremissionMapper.selectByExample(rolePremissionExample);

        if(rolePremissionKeyList != null && !rolePremissionKeyList.isEmpty()) {

            throw new ServiceException("permissions Role references Delete failed");

        }

        Permission permission = permissionMapper.selectByPrimaryKey(id);

        permissionMapper.deleteByPrimaryKey(id);

        logger.info("Remove Permissions {}",permission);

    }

    /*
     *查询所有角色
     * @author 马得草
     * @date 2018/4/15
     */
    @Override
    public List<Role> findAllRole() {
        return roleMapper.findAllRole();
    }


    /*
     *根据Id查找对应的permission对象 是用来编辑的
     * @author 马得草
     * @date 2018/4/16
     */
    @Override
    public Permission findPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }


    /*
     *编辑权限
     * @author 马得草
     * @date 2018/4/16
     */
    @Override
    public void updatePermission(Permission permission, Integer id) {
        permission.setCreateTime(new Date());

        permissionMapper.updateByPrimaryKeySelective(permission);
        logger.info("编辑权限 {}", permission);

    }


}

