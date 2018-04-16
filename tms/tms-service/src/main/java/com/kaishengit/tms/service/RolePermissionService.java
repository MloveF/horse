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
     *��ѯ���н�ɫ
     * @author ��ò�
     * @date 2018/4/15
     */
    List<Role> findAllRole();


    Permission findPermissionById(Integer id);

    void updatePermission(Permission permission,Integer id);
}
