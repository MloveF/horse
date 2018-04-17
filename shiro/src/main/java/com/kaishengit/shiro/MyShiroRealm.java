package com.kaishengit.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyShiroRealm implements Realm {
    public String getName() {
        return "myRealm";
    }

    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        if(!"tom".equals(userName)) {
            throw new UnknownAccountException("找不到该用户");
        }
        if(!"000000".equals(password)) {
            throw new IncorrectCredentialsException("密码错误");
        }
        if(1==1) {
            throw new LockedAccountException("账号被锁定");
        }

        return new SimpleAuthenticationInfo(userName,password,getName());
    }

}
