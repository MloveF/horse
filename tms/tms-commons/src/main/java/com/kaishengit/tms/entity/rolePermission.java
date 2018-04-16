package com.kaishengit.tms.entity;

public class rolePermission {

    private Integer roleId;
    private Integer permissionId;

    @Override
    public String toString() {
        return "rolePermission{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                '}';
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
