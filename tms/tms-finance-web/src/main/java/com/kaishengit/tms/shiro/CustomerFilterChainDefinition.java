package com.kaishengit.tms.shiro;


import com.kaishengit.tms.entity.Permission;
import com.kaishengit.tms.service.RolePermissionService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/*
 *��̬����Ȩ�޺�url�Ĺ�ϵ
 * @author ��ò�
 * @date 2018/4/18
 */
public class CustomerFilterChainDefinition {

    private Logger logger = LoggerFactory.getLogger(CustomerFilterChainDefinition.class);

    @Autowired
    private RolePermissionService rolePermissionService;

    private String filterChainDefinitions;

    private AbstractShiroFilter shiroFilter;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public void setShiroFilter(AbstractShiroFilter shiroFilter) {
        this.shiroFilter = shiroFilter;
    }

    /*   
     *Spring��������ʱ����
     * @author ��ò�  
     * @date 2018/4/18   
     */
    @PostConstruct
    public synchronized void init() {
        getFilterChainManager().getFilterChains().clear();
        load();
    }
    
    /*   
     *���¼���URLȨ��
     * @author ��ò�  
     * @date 2018/4/18   
     */
    public synchronized void updateUrlPermission() {
        getFilterChainManager().getFilterChains().clear();
        load();
    }

    public synchronized void load() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);

        //�����ݿ��в������е�Ȩ�޶���
        List<Permission> permissionList = rolePermissionService.findAllPermission();
        Ini.Section section = ini.get(Ini.DEFAULT_SECTION_NAME);

        for(Permission permissions : permissionList) {
            section.put(permissions.getPermissionUrl(),"perms[" + permissions.getPermissionCode() + "]");
        }
        section.put("/**","user");

        DefaultFilterChainManager defaultFilterChainManager = getFilterChainManager();
        for(Map.Entry<String,String> entry : section.entrySet()) {
            defaultFilterChainManager.createChain(entry.getKey(),entry.getValue());
        }
    }

    private DefaultFilterChainManager getFilterChainManager() {
        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();
        return defaultFilterChainManager;

    }

}
