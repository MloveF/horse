package com.kaishengit.tms.service.impl;

import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.AccountLoginLogMapper;
import com.kaishengit.tms.mapper.AccountMapper;
import com.kaishengit.tms.mapper.AccountRoleMapper;
import com.kaishengit.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 *ϵͳ�˻�ҵ����
 * @author ��ò�
 * @date 2018/4/16
 */
@Service
public class  AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;

    @Autowired
    private AccountRoleMapper accountRoleMapper;

    /**
     *
     * @param accountMobile �ֻ�����
     * @param password  ����
     * @param requestIp ��¼��IP��ַ
     * @return  ��¼�ɹ�����account����
     * @throws ServiceException �����¼ʧ�ܣ���ͨ���쳣�׳�����Ĵ���ԭ��
     */
    @Override
    public Account login(String accountMobile, String password, String requestIp) throws ServiceException {
        //�����ֻ�������Ҷ�Ӧ���˺�
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(accountMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);

        Account account = null;
        if(accountList != null && !accountList.isEmpty()) {

            account = accountList.get(0);
            if(account.getAccountPassword().equals(DigestUtils.md5Hex(password))) {
                //�ж�״̬
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    //��ӵ�¼����־
                    AccountLoginLog loginLog = new AccountLoginLog();
                    loginLog.setAccountId(account.getId());
                    loginLog.setLoginIp(requestIp);
                    loginLog.setLoginTime(new Date());
                    accountLoginLogMapper.insertSelective(loginLog);

                    logger.info("{} ��¼ϵͳ",account);
                    return account;
                }else if ( Account.STATE_DISABLE.equals(account.getAccountState())) {
                    throw new ServiceException("�˺ű�����");
                }else {
                    throw new ServiceException("�˺ű�����");
                }
            }else {
                throw new ServiceException("�˺Ż��������");
            }

        }else {
            throw new ServiceException("�˺Ż��������");
        }
    }


    /*
     *����uri�����Ĳ�����ѯ�����˻������ض�Ӧ�Ľ�ɫ�б�
     * @author ��ò�
     * @date 2018/4/16
     */
    @Override
    public List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam) {
        return accountMapper.findAllWithRolesByQueryParam(requestParam);
    }

    /**
     * �����˺�
     *
     * @param account  account����
     * @param rolesIds ��Ӧ�Ľ�ɫID
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAccount(Account account, Integer[] rolesIds) {
        account.setCreateTime(new Date());
        //�˺�Ĭ������Ϊ�ֻ�����ĺ�6λ
        String password;
        if(account.getAccountMobile().length() <= 6) {
            password = account.getAccountMobile();
        } else {
            password = account.getAccountMobile().substring(account.getAccountMobile().length()-6,account.getAccountMobile().length());
        }
        //���������MD5����
        password = DigestUtils.md5Hex(password);

        account.setAccountPassword(password);

        //�˺�Ĭ��״̬
        account.setAccountState(Account.STATE_NORMAL);
       int id =  accountMapper.insertSelective(account);

        //����˺źͽ�ɫ�Ķ�Ӧ��ϵ��
        for(Integer roleId : rolesIds) {
            AccountRoleKey accountRolesKey = new AccountRoleKey();
            accountRolesKey.setAccountId(account.getId());
            accountRolesKey.setRoleId(roleId);

            accountRoleMapper.insert(accountRolesKey);
        }

    }

    /*
     *����ID��ѯaccount����
     * @author ��ò�
     * @date 2018/4/17
     */
    @Override
    public Account findById(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    /*
     *�޸��˻�
     * @author ��ò�
     * @date 2018/4/17
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateAccount(Account account, Integer[] rolesIds) {

        account.setUpdateTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);

        //ɾ��ԭ�����˻� ��ɫ��ϵ

        AccountRoleExample accountRoleExample = new AccountRoleExample();
        accountRoleExample.createCriteria().andAccountIdEqualTo(account.getId());
        accountRoleMapper.deleteByExample(accountRoleExample);

        //�����˺�-��ɫ��ϵ
        if(rolesIds != null) {
            for(Integer roles : rolesIds) {
                AccountRoleKey accountRoleKey = new AccountRoleKey();
                accountRoleKey.setRoleId(roles);
                accountRoleKey.setAccountId(account.getId());

                accountRoleMapper.insertSelective(accountRoleKey);
            }
        }
        logger.info("�޸��˺� {}",account);
    }

    /*
     *���ݵ绰�������account����
     * @author ��ò�
     * @date 2018/4/17
     */
    @Override
    public Account findByMobile(String userMobile) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(userMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);

        if(accountList != null && !accountList.isEmpty()) {
            return accountList.get(0);
        }
        return null;
    }

    /*
     *�����˻���¼��Ϣ
     * @author ��ò�
     * @date 2018/4/17
     */
    @Override
    public void saveAccountLoginLog(AccountLoginLog accountLoginLog) {
        accountLoginLogMapper.insertSelective(accountLoginLog);
    }

    /*
     *����accountIDɾ��account����
     * @author ��ò�
     * @date 2018/4/18
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delAccountById(Integer id) throws ServiceException {

        //ɾ���˻��ͽ�ɫ�Ĺ�ϵ��¼
        AccountRoleExample accountRoleExample = new AccountRoleExample();
        accountRoleExample.createCriteria().andAccountIdEqualTo(id);
        accountRoleMapper.deleteByExample(accountRoleExample);

        //ɾ���˻�
        Account account = accountMapper.selectByPrimaryKey(id);
        accountMapper.deleteByPrimaryKey(id);

        logger.info("ɾ����ɫ {}",account);


    }

}

