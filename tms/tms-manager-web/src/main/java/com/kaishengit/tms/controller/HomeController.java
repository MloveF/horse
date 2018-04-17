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
 *首页及登录、登出的控制器
 * @author 马得草
 * @date 2018/4/12   
 */
@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    /*
     *系统登录页面
     * @author 马得草
     * @date 2018/4/12
     */
    @GetMapping("/")
    public String index() {

        Subject subject = SecurityUtils.getSubject();

        //判断当前账户是否被认证 如果是 则退出该账号
        if(subject.isAuthenticated()) {
            subject.logout();
        }
        //判断是否被记住 如果被记住 则跳转到登录成功页面
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

        //根据账户和密码进行登录
        String requestIp = request.getRemoteAddr();
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(accountMobile,DigestUtils.md5Hex(password),rememberMe!=null,requestIp);

        try{
            subject.login(usernamePasswordToken);

            //登录后跳转到目标的判断
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = "/home";
            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }
            return "redirect:"+url;

        }catch (UnknownAccountException | IncorrectCredentialsException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号被锁定");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
        }

        return "redirect:/";

    }




    /**
     * 登录后的首页
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

