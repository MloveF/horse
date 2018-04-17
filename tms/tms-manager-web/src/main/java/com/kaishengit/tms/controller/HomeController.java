package com.kaishengit.tms.controller;


import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*   
 *��ҳ����¼���ǳ��Ŀ�����
 * @author ��ò�
 * @date 2018/4/12   
 */
@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    /*
     *ϵͳ��¼ҳ��
     * @author ��ò�
     * @date 2018/4/12
     */
    @GetMapping("/")
    public String index() {

        Subject subject = SecurityUtils.getSubject();

        //�жϵ�ǰ�˻��Ƿ���֤ ����� ���˳����˺�
        if(subject.isAuthenticated()) {
            subject.logout();
        }
        //�ж��Ƿ񱻼�ס �������ס ����ת����¼�ɹ�ҳ��
        if(subject.isRemembered()) {
            return "redirect:/home";
        }

        return "index";
    }


    @PostMapping("/")
    public String login(String accountMobile,
                        String password,
                        String rememberMe,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();

        //�����˻���������е�¼
        String requestIp = request.getRemoteAddr();
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(accountMobile,DigestUtils.md5Hex(password),rememberMe!=null,requestIp);

        try{
            subject.login(usernamePasswordToken);

            //��¼����ת��Ŀ����ж�
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = "/home";
            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }
            return "redirect:"+url;

        }catch (UnknownAccountException | IncorrectCredentialsException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","�˺Ż��������");
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","�˺ű�����");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","�˺Ż��������");
        }

        return "redirect:/";

    }




    /**
     * ��¼�����ҳ
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/401")
    public String unauthorizedUrl() {
        return "error/401";
    }

}

