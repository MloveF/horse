package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.exception.ServiceException;

import java.util.List;
import java.util.Map;

/*   
 *系统账号的业务类
 * @author 马得草  
 * @date 2018/4/12   
 */
public interface AccountService {


    /**
     * 系统登录
     * @param accountMobile
     * @param password
     * @param requestIp
     * @return
     * @throws ServiceException
     */
    Account login(String accountMobile, String password, String requestIp)throws ServiceException;

    /*
     *根据uri传来的参数查询所有账户并加载对应的角色列表
     * @author 马得草
     * @date 2018/4/16
     */
    List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam);

    /**
     * 新增账号
     * @param account account对象
     * @param rolesIds 对应的角色ID
     */
    void saveAccount(Account account, Integer[] rolesIds);

    /*
     *根据ID查询account对象
     * @author 马得草
     * @date 2018/4/17
     */
    Account findById( Integer id);

    /*
     *修改账户
     * @author 马得草
     * @date 2018/4/17
     */
    void updateAccount(Account account, Integer[] rolesIds);

    /*
     *根据电话查找account对象
     * @author 马得草
     * @date 2018/4/17
     */
    Account findByMobile(String userMobile);

    /*
     *保存账户登录信息
     * @author 马得草
     * @date 2018/4/17
     */
    void saveAccountLoginLog(AccountLoginLog accountLoginLog);
}

