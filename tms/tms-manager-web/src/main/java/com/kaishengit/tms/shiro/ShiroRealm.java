package com.kaishengit.tms.shiro;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private AccountService accountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /*
     *判断登录
     * @author 马得草
     * @date 2018/4/17
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
       String userMobile = usernamePasswordToken.getUsername();
       if(userMobile != null) {
           Account account = accountService.findByMobile(userMobile);
           if(account == null) {
               throw new UnknownAccountException("找不到该账号:" + userMobile);
           }else {
               if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    logger.info("{} 登录成功: {}",account,usernamePasswordToken.getHost());
                    //保存登录的日志到数据库
                   AccountLoginLog accountLoginLog = new AccountLoginLog();
                   accountLoginLog.setLoginTime(new Date());
                   accountLoginLog.setLoginIp(usernamePasswordToken.getHost());
                   accountLoginLog.setAccountId(account.getId());
                   accountService.saveAccountLoginLog(accountLoginLog);

                   return new SimpleAuthenticationInfo(account,account.getAccountPassword(),getName());
               }else {
                   throw new LockedAccountException("账号被禁用或锁定:" + account.getAccountState());
               }
           }

       }


        return null;
    }
}
