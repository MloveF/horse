package com.kaishengit.tms.shiro;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.service.AccountService;
import com.kaishengit.tms.service.RolePermissionService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /*
     *�жϽ�ɫ��Ȩ��
     * @author ��ò�
     * @date 2018/4/18
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //��ȡ��ǰ��½�Ķ���
        Account account = (Account) principalCollection.getPrimaryPrincipal();
        //��ȡ��ǰ��½�Ķ���ӵ�еĽ�ɫ
        List<Role> roleList = rolePermissionService.findRolesByAccountId(account.getId());
        //��ȡ��ǰ��½�Ķ���ӵ�е�Ȩ��
        List<Permission> permissionList = new ArrayList<>();
        for(Role roles : roleList) {
            List<Permission> permissions = rolePermissionService.findAllPermissionByRolesId(roles.getId());
            permissionList.addAll(permissions);
        }

        Set<String> rolesNameSet = new HashSet<>();
        for(Role roles : roleList) {
            rolesNameSet.add(roles.getRolesCode());
        }

        Set<String> permissionNameSet = new HashSet<>();
        for(Permission permissions : permissionList) {
            permissionNameSet.add(permissions.getPermissionCode());
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //��ǰ�û�ӵ�еĽ�ɫcodeֵ
        simpleAuthorizationInfo.setRoles(rolesNameSet);

        //��ǰ�û�ӵ�е�Ȩ��codeֵ
        simpleAuthorizationInfo.setStringPermissions(permissionNameSet);
        return simpleAuthorizationInfo;
    }

    /*
     *�жϵ�¼
     * @author ��ò�
     * @date 2018/4/17
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
       String userMobile = usernamePasswordToken.getUsername();
       if(userMobile != null) {
           Account account = accountService.findByMobile(userMobile);
           if(account == null) {
               throw new UnknownAccountException("�Ҳ������˺�:" + userMobile);
           }else {
               if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    logger.info("{} ��¼�ɹ�: {}",account,usernamePasswordToken.getHost());
                    //�����¼����־�����ݿ�
                   AccountLoginLog accountLoginLog = new AccountLoginLog();
                   accountLoginLog.setLoginTime(new Date());
                   accountLoginLog.setLoginIp(usernamePasswordToken.getHost());
                   accountLoginLog.setAccountId(account.getId());
                   accountService.saveAccountLoginLog(accountLoginLog);

                   return new SimpleAuthenticationInfo(account,account.getAccountPassword(),getName());
               }else {
                   throw new LockedAccountException("�˺ű����û�����:" + account.getAccountState());
               }
           }

       }


        return null;
    }
}
