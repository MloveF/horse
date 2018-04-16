package com.kaishengit.tms.entity;

import java.io.Serializable;

/**
 * role_premission
 * @author 
 */
public class RolePremissionKey implements Serializable {
    private Integer roleId;

    private Integer premissionId;

    private static final long serialVersionUID = 1L;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPremissionId() {
        return premissionId;
    }

    public void setPremissionId(Integer premissionId) {
        this.premissionId = premissionId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RolePremissionKey other = (RolePremissionKey) that;
        return (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getPremissionId() == null ? other.getPremissionId() == null : this.getPremissionId().equals(other.getPremissionId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getPremissionId() == null) ? 0 : getPremissionId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", premissionId=").append(premissionId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}