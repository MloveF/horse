package com.kaishengit.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class MyRealmHelloWorld {

    public static void main(String[] args) {
        //1. ����SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        //2. ��ȡSubject����
        Subject subject = SecurityUtils.getSubject();

        //3. ��¼
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("tom","000000");
        try {
            subject.login(usernamePasswordToken);
            System.out.println("-=----> ��¼�ɹ�");
        } catch (UnknownAccountException ex) {
            ex.printStackTrace();
        } catch (IncorrectCredentialsException ex) {
            ex.printStackTrace();
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
        }
    }
}
