package com.kaishengit.tms.controller;


import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.dto.ResponseBean;
import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.AccountService;
import com.kaishengit.tms.service.TicketService;
import com.kaishengit.tms.service.TicketStoreService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TicketStoreService ticketStoreService;
    /*
     *年票入库首页
     * @author 马得草
     * @date 2018/4/20
     */
    @GetMapping("/storage")
    public String ticketIn(Model model) {
        Account account = (Account) SecurityUtils.getSubject().getPrincipal();
        List<TicketInRecord> ticketInRecordList = ticketService.findAllTicketInRecordByAccountId(account.getId());
        model.addAttribute("ticketInRecordList",ticketInRecordList);
        return "ticket/storage/home";
    }

    /*
     *新增入库
     * @author 马得草
     * @date 2018/4/20
     */
    @GetMapping("/storage/new")
    public String newTicketStorage(Model model) {
        String today = DateTime.now().toString("YYYY-MM-dd");

        model.addAttribute("today",today);
        return "ticket/storage/new";
    }

    @PostMapping("/storage/new")
    public String newTicketStorage(TicketInRecord ticketInRecord, RedirectAttributes redirectAttributes) {
        String begin = ticketInRecord.getBeginTicketNum();
        String end = ticketInRecord.getEndTicketNum();
        if(begin == "" || end == "") {
            redirectAttributes.addFlashAttribute("message","Cannot be empty");
            return "redirect:/ticket/storage/new";
        }

        List<Ticket> ticketList = ticketService.findAllTicket();

        int beginNum =  Integer.parseInt(ticketInRecord.getBeginTicketNum());
        int endNum = Integer.parseInt(ticketInRecord.getEndTicketNum());
        if(endNum <= beginNum ) {
            redirectAttributes.addFlashAttribute("message","Incorrect input format");
            return "redirect:/ticket/storage/new";
        }

        for(Ticket ticket : ticketList) {
            if(ticket.getId()<= endNum && ticket.getId()>=beginNum ) {
                redirectAttributes.addFlashAttribute("message","The ticket number is not repeatable");
                return "redirect:/ticket/storage/new";
            }
        }

        ticketService.saveTicketInRecord(ticketInRecord);

        redirectAttributes.addFlashAttribute("message","New success");
        return "redirect:/ticket/storage";
    }



    /*
     *根据id删除对应的入库列表和票库
     * @author 马得草
     * @date 2018/4/21
     */
    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody ResponseBean delTicketInRecord(@PathVariable Integer id) {

        try {
            ticketService.delTicketInRecordById(id);
            return ResponseBean.success();
        } catch (ServiceException ex) {
            return ResponseBean.error(ex.getMessage());
        }

    }


    /*   
     *年票下发首页
     * @author 马得草  
     * @date 2018/4/23   
     */
    @GetMapping("/out")
    public String ticketOutHome(Model model,
                                @RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo) {

        PageInfo<TicketOutRecord> pageInfo = ticketService.findTicketOutRecordByPageNo(pageNo);
        model.addAttribute("page",pageInfo);
        return "ticket/out/home";
        
    }


    /*
     *新增下发
     * @author 马得草
     * @date 2018/4/23
     */
    @GetMapping("/out/new")
    public String newTicketOut(Model model) {

        String today = DateTime.now().toString("YYYY-MM-dd");
        //查看所有的售票点
        List<TicketStore> ticketStoreList = ticketStoreService.findAllTicketStore();

        model.addAttribute("today",today);
        model.addAttribute("ticketStoreList",ticketStoreList);
        return "ticket/out/new";
    }


    @PostMapping("/out/new")
    public String newTicketOut(TicketOutRecord ticketOutRecord,RedirectAttributes redirectAttributes) {

        try {
            ticketService.saveTicketOutRecord(ticketOutRecord);
        } catch (ServiceException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/ticket/out";

    }


    @GetMapping("/chart")
    public String chartHome(Model model) {
        Map<String,Long> resultMap = ticketService.countTicketByState();
        model.addAttribute("resultMap",resultMap);
        return "ticket/chart/home";
    }


    @GetMapping("/out/{id:\\d+}/del")
    @ResponseBody
    public ResponseBean  delTicketOut(@PathVariable Integer id) {

        ticketService.delOutRecordById(id);
        return ResponseBean.success();
    }

}
