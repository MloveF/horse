package com.kaishengit.tms.controller;

import com.google.common.collect.Maps;
import com.kaishengit.tms.controller.exception.NotFoundException;
import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.service.AccountService;
import com.kaishengit.tms.service.RolePermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/*
     *账号管理
     * @author 马得草
     * @date 2018/4/16
     */
    @Controller
    @RequestMapping("/manage/account")
    public class AccountController {

        @Autowired
        private RolePermissionService rolePermissionService;

        @Autowired
        private AccountService accountService;

        /*
         *跳转到home.jsp页
         * @author 马得草
         * @date 2018/4/16
         */
        @GetMapping
        public String home(Model model,
                           @RequestParam(required = false) Integer rolesId,
                           @RequestParam(required = false) String nameMobile) {

            Map<String,Object> requestParam = Maps.newHashMap();
            requestParam.put("nameMobile",nameMobile);
            requestParam.put("rolesId",rolesId);

            model.addAttribute("accountList",accountService.findAllAccountWithRolesByQueryParam(requestParam));
            model.addAttribute("rolesList",rolePermissionService.findAllRoles());
            return "manage/account/home";
        }

        /*
         *新增角色 重定向到new.jsp页
         * @author 马得草
         * @date 2018/4/16
         */
        @GetMapping("/new")
        @RequiresPermissions("account:add")
        public String newAccount(Model model) {

            List<Role> roleList = rolePermissionService.findAllRoles();
            model.addAttribute("rolesList",roleList);
            return "manage/account/new";

        }

        @PostMapping("/new")
        @RequiresPermissions("account:add")
        public String newAccount(Account account,Integer[] rolesIds) {
            accountService.saveAccount(account,rolesIds);
            return "redirect:/manage/account";
        }


        @GetMapping("/{id:\\d+}/edit")
        public String updateAccount(@PathVariable Integer id,Model model) {

            Account account = accountService.findById(id);
            if(account == null) {
                throw new NotFoundException();
            }
            //查询所有角色
            List<Role> rolesList = rolePermissionService.findAllRoles();

            List<Role> accountRolesList = rolePermissionService.findRolesByAccountId(id);

            model.addAttribute("accountRolesList",accountRolesList);
            model.addAttribute("rolesList",rolesList);
            model.addAttribute("account",account);
            return "manage/account/edit";
        }


    @PostMapping("/{id:\\d+}/edit")
    public String updateAccount(Account account, Integer[] rolesIds, RedirectAttributes redirectAttributes) {
        accountService.updateAccount(account,rolesIds);
        redirectAttributes.addFlashAttribute("message","修改账号成功");
        return "redirect:/manage/account";
    }






    }
