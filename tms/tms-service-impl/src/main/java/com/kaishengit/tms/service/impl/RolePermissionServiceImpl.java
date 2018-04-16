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
 * ��ɫ��Ȩ�޵�ҵ����
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
     *����Ȩ��
     * @author ��ò�
     * @date 2018/4/16
     */
    @Override
    public void savePermission(Permission permission) {

        permission.setCreateTime(new Date());
        permissionMapper.insertSelective(permission);
        logger.info("���Ȩ�� {}", permission);

    }

    /*
     *��ѯ����Ȩ��
     * @author ��ò�
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
     *����Ȩ�����Ͳ��Ҷ�Ӧ���б�
     * @author ��ò�
     * @date 2018/4/13
     */
    @Override
    public List<Permission> findPermissionByPermissionType(String menuType) {

        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(menuType);

        return permissionMapper.selectByExample(permissionExample);
    }


    /*
     *������ɫ
     * @author ��ò�
     * @date 2018/4/14
     * role ��ɫ����
     * permissionId ��ɫ��Ӧ��Ȩ��Id
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
        logger.info("�����ɫ {}",role);
    }

    /*
     *����idɾ����Ӧ��Ȩ��
     * @author ��ò�
     * @date 2018/4/15
     * @ServiceException  ɾ��ʧ�� �׳��쳣
     */
    @Override
    public void delPermissionById(Integer id)throws ServiceException {

        //��ѯ��Ȩ���Ƿ����ӽڵ�
       PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andParentIdEqualTo(id);

        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);

        if(permissionList != null && !permissionList.isEmpty()) {

            throw new ServiceException("have Child nodes Delete failed");
        }

        //��ѯ�жϸ�Ȩ���Ƿ񱻽�ɫռ��
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
     *��ѯ���н�ɫ
     * @author ��ò�
     * @date 2018/4/15
     */
    @Override
    public List<Role> findAllRole() {
        return roleMapper.findAllRole();
    }


    /*
     *����Id���Ҷ�Ӧ��permission���� �������༭��
     * @author ��ò�
     * @date 2018/4/16
     */
    @Override
    public Permission findPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }


    /*
     *�༭Ȩ��
     * @author ��ò�
     * @date 2018/4/16
     */
    @Override
    public void updatePermission(Permission permission, Integer id) {
        permission.setCreateTime(new Date());

        permissionMapper.updateByPrimaryKeySelective(permission);
        logger.info("�༭Ȩ�� {}", permission);

    }


}

