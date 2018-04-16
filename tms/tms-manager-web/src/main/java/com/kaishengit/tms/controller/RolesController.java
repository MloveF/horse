package com.kaishengit.tms.controller;


import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.entity.RolePremissionKey;
import com.kaishengit.tms.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/*
 *角色管理
 * @author 马得草
 * @date 2018/4/14
 */
@Controller
@RequestMapping("/manage/roles")
public class RolesController {

    @Autowired
    private RolePermissionService rolePermissionService;


    @GetMapping
    public String  home(Model model) {

        List<Role> roleList = rolePermissionService.findAllRole();
        model.addAttribute("roleList",roleList);

        for (Role role : roleList){
            System.out.println(role);
        }

        return "manage/roles/home";

    }

    @GetMapping("/new")
    public String newRoles(Model model) {
        model.addAttribute("permissionList",rolePermissionService.findAllPermission());
        return "manage/roles/new";
    }

    @PostMapping("/new")
    public String newRoles(Role role, Integer[] permissionId, RedirectAttributes redirectAttributes) {
        rolePermissionService.saveRoles(role,permissionId);

        redirectAttributes.addFlashAttribute("message","新增成功");
        return "redirect:/manage/roles";

    }



}
