package com.kaishengit.tms.controller;


import com.kaishengit.tms.dto.ResponseBean;
import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountExample;
import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketInRecord;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.AccountService;
import com.kaishengit.tms.service.TicketService;
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

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AccountService accountService;
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



}
