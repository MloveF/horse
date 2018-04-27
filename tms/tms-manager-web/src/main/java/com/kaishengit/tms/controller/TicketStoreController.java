package com.kaishengit.tms.controller;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.kaishengit.tms.controller.exception.NotFoundException;
import com.kaishengit.tms.dto.ResponseBean;
import com.kaishengit.tms.entity.StoreAccount;
import com.kaishengit.tms.entity.TicketStore;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.fileStore.QiniuStore;
import com.kaishengit.tms.service.TicketStoreService;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/ticketstore")
public class    TicketStoreController {

    @Autowired
    private TicketStoreService ticketStoreService;

    @Autowired
    private QiniuStore qiniuStore;


    @GetMapping
    public String home(Model model,
                       @RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo,
                       @RequestParam(required = false,defaultValue = "") String storeName,
                       @RequestParam(required = false,defaultValue = "") String storeManager,
                       @RequestParam(required = false,defaultValue = "") String storeTel){

        Map<String,Object> queryParam = Maps.newHashMap();
        queryParam.put("storeName",storeName);
        queryParam.put("storeManager",storeManager);
        queryParam.put("storeTel",storeTel);
        PageInfo<TicketStore> pageInfo = ticketStoreService.findAllTicketStoreByPage(pageNo,queryParam);
        model.addAttribute("pageInfo",pageInfo);

        return "store/home";
    }


    /*
     *������Ʊ��
     * @author ��ò�
     * @date 2018/4/20
     */
    @GetMapping("/new")
    public String newStore(Model model) {

        //��ȡ��ţ�ϴ���token
        String upToken  = qiniuStore.getUploadToken();
        model.addAttribute("upToken",upToken );
        return "store/new";
    }


    @PostMapping("/new")
    public String newStore(TicketStore ticketStore, RedirectAttributes redirectAttributes) {

        ticketStoreService.saveNewTicktStore(ticketStore);
        redirectAttributes.addFlashAttribute("message","�����ɹ�");
        return "redirect:/ticketstore";
    }


    /*
     *�鿴��Ʊ������
     * @author ��ò�
     * @date 2018/4/20
     */
    @GetMapping("/{id:\\d+}")
    public String viewTicketStore(@PathVariable Integer id,Model model) {
        TicketStore ticketStore = ticketStoreService.findTicketStoreById(id);

        if(ticketStore == null) {
            throw new NotFoundException();
        }

        //���ҹ�������Ʊ���˺�
        StoreAccount storeAccount = ticketStoreService.findStoreAccountById(ticketStore.getStoreAccountId());

        model.addAttribute("storeAccount",storeAccount);
        model.addAttribute("ticketStore",ticketStore);
        return "store/info";

    }

    /*
     *�༭��Ʊ��
     * @author ��ò�
     * @date 2018/4/20
     */
    @GetMapping("/{id:\\d+}/edit")
    public String editTicketStore(@PathVariable Integer id,Model model) {
        TicketStore ticketStore = ticketStoreService.findTicketStoreById(id);

        if(ticketStore == null) {
            throw new NotFoundException();
        }

        //��ȡ��ţ���ϴ�Token����
        String uploadToken = qiniuStore.getUploadToken();

        model.addAttribute("uploadToken",uploadToken );
        model.addAttribute("ticketStore",ticketStore);
        return "store/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editTicketStore(TicketStore ticketStore,RedirectAttributes redirectAttributes) {

        ticketStoreService.updateTicketStore(ticketStore);
        redirectAttributes.addFlashAttribute("message","�޸ĳɹ�");
        return "redirect:/ticketstore";
    }


    /*
     *��StroeAccount��״̬�޸�Ϊ���ã�disable��
     * @author ��ò�
     * @date 2018/4/22
     */
    @GetMapping("/{id:\\d+}/disable")
    public String disableAccount(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

        ticketStoreService.editStroeAccountState(id);
        StoreAccount storeAccount = ticketStoreService.findStoreAccountByTicketStoreId(id);

        model.addAttribute("storeAccount",storeAccount);
        return "redirect:/ticketstore";
    }

    @GetMapping("/{id:\\d+}/normal")
    public String normalAccount(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

        ticketStoreService.normalAccountState(id);
        StoreAccount storeAccount = ticketStoreService.findStoreAccountByTicketStoreId(id);

        model.addAttribute("storeAccount",storeAccount);
        return "redirect:/ticketstore";
    }


    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody
    ResponseBean deleteRoles(@PathVariable Integer id) {

        try {
            ticketStoreService.delTicketStoreWithAccountById(id);
            return ResponseBean.success();
        }catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }



}
