package com.kaishengit.tms.controller;


import com.kaishengit.tms.entity.TicketStore;
import com.kaishengit.tms.fileStore.QiniuStore;
import com.kaishengit.tms.service.TicketStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ticketstore")
public class TicketStoreController {

    @Autowired
    private TicketStoreService ticketStoreService;

    @Autowired
    private QiniuStore qiniuStore;

    @GetMapping
    public String home() {
        return "store/home";
    }

    @GetMapping("/new")
    public String newStore() {
        return "store/new";

    }

    @PostMapping("/new")
    public String newStore(TicketStore ticketStore, RedirectAttributes redirectAttributes) {

        ticketStoreService.saveNewTicktStore(ticketStore);
        redirectAttributes.addFlashAttribute("message","�����ɹ�");
        return "redirect:/ticketstore";
    }


}
