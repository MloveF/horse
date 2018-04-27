package com.kaishengit.tms.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.StoreAccount;
import com.kaishengit.tms.entity.StoreLoginLog;
import com.kaishengit.tms.entity.TicketStore;

import java.util.List;
import java.util.Map;


/**
 * 年票售票点业务类
 * @author fankay
 */
public interface TicketStoreService {

    /*
     *创建新的售票点
     * @author 马得草
     * @date 2018/4/19
     */
    void saveNewTicktStore(TicketStore ticketStore);

    /*
     *根据当前页面和查询参数查询销售点
     * @author 马得草
     * @date 2018/4/20
     */
    PageInfo<TicketStore> findAllTicketStoreByPage(Integer pageNo, Map<String, Object> queryParam);

    /*
     *根据ID查找对应的售票点
     * @author 马得草
     * @date 2018/4/20
     */
    TicketStore findTicketStoreById(Integer id);

    /*
     *根据售票点的StoreAccountId主键查找售票点账号对象
     * @author 马得草
     * @date 2018/4/20
     */
    StoreAccount findStoreAccountById(Integer id);

    /*   
     *修改售票点
     * @author 马得草  
     * @date 2018/4/20   
     */
    void updateTicketStore(TicketStore ticketStore);

    /*
     *把StroeAccount的状态修改为禁用（disable）
     * @author 马得草
     * @date 2018/4/22
     */
    void editStroeAccountState(Integer id);

    /*
     *根据TicketStore的ID查找对应的StoreAccount对象
     * @date 2018/4/23
     * @param
     * @return
     */
    StoreAccount findStoreAccountByTicketStoreId(Integer id);

    /*
     *把StroeAccount的状态修改为正常（normal）
     * @author 马得草
     * @date 2018/4/23
     */
    void normalAccountState(Integer id);


    /*
     *根据售票点的ID删除售票点和售票账户
     * @author 马得草
     * @date 2018/4/23
     */
    void delTicketStoreWithAccountById(Integer id);

    /**
     * 查询所有的售票点
     * @return
     */
    List<TicketStore> findAllTicketStore();

    /**
     * 根据账号（手机号码）查找售票点登录账号对象
     * @param
     * @return
     */
    StoreAccount findStoreAccountByName(String name);


    /**
     * 保存售票点的登录日志
     * @param
     */
    void saveStoreAccountLoginLog(StoreLoginLog storeLoginLog);
}
