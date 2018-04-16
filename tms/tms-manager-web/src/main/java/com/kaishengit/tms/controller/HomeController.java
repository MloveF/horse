package com.kaishengit.tms.controller;


import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
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
        return "index";
    }

    /*
     *ϵͳ��¼
     * @author ��ò�
     * @date 2018/4/12
     */
    @PostMapping("/")
    public String login(String accountMobile,
                        String password,
                        HttpSession session,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {

        //��ȡ��½��ip
        String requestIp = request.getRemoteAddr();

        try {
            Account account = accountService.login(accountMobile, password, requestIp);
            //����¼�ɹ��Ķ������session
            session.setAttribute("current_Account",account);
            //����homeҳ��
            return "redirect:/home";
        } catch (ServiceException ex) {
            redirectAttributes.addFlashAttribute("phone",accountMobile);
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/";
        }

    }

    /**
     * ��¼�����ҳ
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

}

