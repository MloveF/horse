package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.exception.ServiceException;

import java.util.List;

/*
 *��ɫ��Ȩ�޵�ҵ����
 * @author ��ò�  
 * @date 2018/4/13   
 */
public interface RolePermissionService {

    /*
     *���Ȩ��
     * @author ��ò�
     * @date 2018/4/13
     */
    void savePermission(Permission permission);


    /*   
     *��ѯ����Ȩ��
     * @author ��ò�  
     * @date 2018/4/13   
     */
    List<Permission> findAllPermission();


    /*   
     *����Ȩ�����Ͳ��Ҷ�Ӧ���б�
     * @author ��ò�  
     * @date 2018/4/13   
     */
    List<Permission> findPermissionByPermissionType(String menuType);

    void saveRoles(Role role, Integer[] permissionId);

    /*
     *����idɾ����Ӧ��Ȩ��
     * @author ��ò�
     * @date 2018/4/15
     */
    void delPermissionById(Integer id)throws ServiceException;

    /*
     *����id����Ȩ��
     * @author ��ò�
     * @date 2018/4/16
     */
    Permission findPermissionById(Integer id);

    /*
     *�༭Ȩ��
     * @author ��ò�
     * @date 2018/4/16
     */
    void updatePermission(Permission permission,Integer id);

    /*
     *��ѯ���еĽ�ɫ�����ؽ�ɫӵ�е�Ȩ���б�
     * @author ��ò�
     * @date 2018/4/16
     */
    List<Role> findAllRolesWithPermission();

    /*
     *��������Ȩ��
     * @author ��ò�
     * @date 2018/4/16
     */
    List<Role> findAllRoles();


    /*
     *�����������ҽ�ɫ
     * @author ��ò�
     * @date 2018/4/17
     */
   Role findById(Integer id);

    /**
     * ���ݽ�ɫID��ѯ��ɫ������ӵ�е�Ȩ��
     * @param id
     * @return
     */
    Role findRolesWithPermissionById(Integer id);

    /*
     *�޸Ľ�ɫ����
     * @author ��ò�
     * @date 2018/4/17
     */
    void updateRoles(Role roles, Integer[] permissionId);

    /*
     *����IDɾ����ɫ
     * @author ��ò�
     * @date 2018/4/17
     */
    void delRolesById(Integer id);

    /*
     *����accountID��ѯ��Ӧ�Ľ�ɫ����
     * @author ��ò�
     * @date 2018/4/17
     */
    List<Role> findRolesByAccountId(Integer id);
}
