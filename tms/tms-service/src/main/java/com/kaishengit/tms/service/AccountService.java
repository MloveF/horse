package com.kaishengit.tms.service;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.exception.ServiceException;

import java.util.List;
import java.util.Map;

/*   
 *ϵͳ�˺ŵ�ҵ����
 * @author ��ò�  
 * @date 2018/4/12   
 */
public interface AccountService {


    /**
     * ϵͳ��¼
     * @param accountMobile
     * @param password
     * @param requestIp
     * @return
     * @throws ServiceException
     */
    Account login(String accountMobile, String password, String requestIp)throws ServiceException;

    /*
     *����uri�����Ĳ�����ѯ�����˻������ض�Ӧ�Ľ�ɫ�б�
     * @author ��ò�
     * @date 2018/4/16
     */
    List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam);

    /**
     * �����˺�
     * @param account account����
     * @param rolesIds ��Ӧ�Ľ�ɫID
     */
    void saveAccount(Account account, Integer[] rolesIds);

    /*
     *����ID��ѯaccount����
     * @author ��ò�
     * @date 2018/4/17
     */
    Account findById( Integer id);

    /*
     *�޸��˻�
     * @author ��ò�
     * @date 2018/4/17
     */
    void updateAccount(Account account, Integer[] rolesIds);

    /*
     *���ݵ绰����account����
     * @author ��ò�
     * @date 2018/4/17
     */
    Account findByMobile(String userMobile);

    /*
     *�����˻���¼��Ϣ
     * @author ��ò�
     * @date 2018/4/17
     */
    void saveAccountLoginLog(AccountLoginLog accountLoginLog);
}

