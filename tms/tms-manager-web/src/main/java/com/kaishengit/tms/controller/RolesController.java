package com.kaishengit.tms.controller;


import com.google.common.collect.Maps;
import com.kaishengit.tms.dto.ResponseBean;
import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.entity.Role;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.service.RolePermissionService;
import com.kaishengit.tms.controller.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


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
        model.addAttribute("rolesList",rolePermissionService.findAllRolesWithPermission());
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

    /*
     *根据id查找对应的角色和权限  用于编辑
     * @author 马得草
     * @date 2018/4/16
     */
    @GetMapping("/{id:\\d+}/edit")
    public String updateRoles(@PathVariable Integer id,
                              Model model) {

        Role roles = rolePermissionService.findRolesWithPermissionById(id);

        if(roles == null) {
            throw new NotFoundException();
        }
        //查询所有的权限
        List<Permission> permissionList = rolePermissionService.findAllPermission();

        //判断权限列表是否该被checked
        Map<Permission,Boolean> map = checkdPermissionList(roles.getPermissionList(),permissionList);

        model.addAttribute("roles",roles);
        model.addAttribute("permissionMap",map);
        return "manage/roles/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editRoles(Role roles,Integer[] permissionId,
                            RedirectAttributes redirectAttributes) {
        rolePermissionService.updateRoles(roles,permissionId);

        redirectAttributes.addFlashAttribute("message","角色修改成功");
        return "redirect:/manage/roles";
    }

    /**
     * 在编辑页面判断当前权限的复选框是否被checked
     * @param rolesPermissionList 当前角色拥有的权限
     * @param permissionList 所有的权限列表
     * @return 有顺序的map集合，如果被选择则value为true
     */
    private Map<Permission, Boolean> checkdPermissionList(List<Permission> rolesPermissionList, List<Permission> permissionList) {
        Map<Permission,Boolean> resultMap = Maps.newLinkedHashMap();

        for(Permission permission : permissionList) {
            boolean flag = false;
            for(Permission rolesPermission : rolesPermissionList) {
                if(permission.getId().equals(rolesPermission.getId())) {
                    flag = true;
                    break;
                }
            }
            resultMap.put(permission,flag);
        }
        return resultMap;
    }

    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody  ResponseBean deleteRoles(@PathVariable Integer id) {

        try {
            rolePermissionService.delRolesById(id);
            return ResponseBean.success();
        }catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }



}
