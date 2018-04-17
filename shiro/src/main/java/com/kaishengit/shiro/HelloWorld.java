package com.kaishengit.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import java.util.Arrays;

public class HelloWorld {

    public static void main(String[] args) {

        //1. ����SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        //2. ��ȡSubject����
        Subject subject = SecurityUtils.getSubject();

        //3. �ж�subject�Ƿ���֤����¼��
        System.out.println("isAuthenticated? " + subject.isAuthenticated());

        if(!subject.isAuthenticated()) {
            //4. �����˺ź�������е�¼
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("zhangsan","123123");

            try {
                //5. ͨ��login�������е�¼,�����¼ʧ�ܣ����׳�AuthenticationException�������쳣
                subject.login(usernamePasswordToken);
                // getPrincipal() ���ص��ǵ�ǰ��¼���˺�����
                System.out.println(subject.getPrincipal() + "��¼�ɹ�");

                //6. ͨ��hasRole()�����ж�subject�Ƿ�ӵ��ĳ����ɫ
                System.out.println("has Admin? " + subject.hasRole("admin"));
                System.out.println("has superadmin? " + subject.hasRole("superadmin"));
                //subject.checkRole("user");ͬ�����ж�subject�Ƿ�ӵ��ĳ����ɫ�����û�У����׳��쳣
                //hasRoles��������ͬʱ�ж�subject�����ɫ������Ϊboolean���͵�����
                boolean[] results = subject.hasRoles(Arrays.asList("admin","superadmin","user"));
                for(boolean result : results) {
                    System.out.println(result);
                }
                //hasAllRoles()�ж�subject�Ƿ�ͬʱӵ����Щ��ɫ�����һ��û�У��ͷ���false
                System.out.println(subject.hasAllRoles(Arrays.asList("admin","superadmin")));

                //Ȩ���ж�
                System.out.println("-----------------------------------");
                //isPermitted() �ж�subject�Ƿ�ӵ��ָ��Ȩ��
                System.out.println("" + subject.isPermitted("user:add"));
                System.out.println("" + subject.isPermitted("customer:add"));

                boolean[] isPermittedArray = subject.isPermitted("user:add","customer:add","customer:find");
                for(boolean isPermitted : isPermittedArray) {
                    System.out.println(isPermitted);
                }

                //isPermittedAll()�ж�subject�Ƿ�ͬʱӵ����ЩȨ��
                System.out.println(subject.isPermittedAll("customer:add","customer:find"));
                //checkPermission()�ж�subject�Ƿ��и�Ȩ�ޣ����û�����׳��쳣
                //subject.checkPermission("user:add");


                //session
                Session session = subject.getSession();
                session.setAttribute("uName","jackson");

                System.out.println(session.getAttribute("uName"));


                //��ȫ�˳�
                subject.logout();



            } catch (UnknownAccountException ex) {
                System.out.println("UnknownAccountException");
            } catch (IncorrectCredentialsException ex) {
                System.out.println("IncorrectCredentialsException");
            } catch (LockedAccountException ex) {
                System.out.println("LockedAccountException");
            } catch (AuthenticationException ex) {
                System.out.println("AuthenticationException");
            }
        }


    }
}
