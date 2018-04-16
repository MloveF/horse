package com.kaishengit.tms.controller;


import com.kaishengit.tms.dto.ResponseBean;
import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
 *Ȩ�޿�����
 * @author ��ò�
 * @date 2018/4/13
 */
@Controller
@RequestMapping("/manage/permission")
public class PermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;


    /*
     *Ȩ�޵���ҳ
     * @author ��ò�  
     * @date 2018/4/14
     */
    @GetMapping
    public String home(Model model) {

        List<Permission> permissionList = rolePermissionService.findAllPermission();

        model.addAttribute("permissionList",permissionList);
        return "manage/permission/home";
    }

    /*   
     *����Ȩ�� ��ȥ���Ҳ˵�Ȩ�޵�list����
     * @author ��ò�  
     * @date 2018/4/13   
     */
    @GetMapping("/new")
    public  String newPermission(Model model) {
        //���Ҳ˵�Ȩ���б�
        List<Permission> premissionList = rolePermissionService.findPermissionByPermissionType(Permission.MENU_TYPE);

        model.addAttribute("premissionList",premissionList);
        return "manage/permission/new";
    }

    @PostMapping("/new")
    public String newPermission(Permission permission, RedirectAttributes redirectAttributes) {

        rolePermissionService.savePermission(permission);

        redirectAttributes.addFlashAttribute("message","�����ɹ�");
        return "redirect:/manage/permission";

    }

    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody ResponseBean deletePermission(@PathVariable Integer id) {

        try {
            rolePermissionService.delPermissionById(id);
            return ResponseBean.success();
        } catch (ServiceException ex) {
            return ResponseBean.error(ex.getMessage());
        }

    }

    @GetMapping("/{id:\\d+}/edit")
    public String editPermission(@PathVariable Integer id, Model model) {
        Permission permission = rolePermissionService.findPermissionById(id);
        List<Permission> premissionList = rolePermissionService.findPermissionByPermissionType(Permission.MENU_TYPE);
        model.addAttribute("permission",permission);
        model.addAttribute("premissionList",premissionList);
        return "manage/permission/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editPermission(@PathVariable Integer id,Permission permission, RedirectAttributes redirectAttributes) {
        rolePermissionService.updatePermission(permission,id);
        redirectAttributes.addFlashAttribute("message","�༭�ɹ�");
        return "redirect:/manage/permission";
    }




}
