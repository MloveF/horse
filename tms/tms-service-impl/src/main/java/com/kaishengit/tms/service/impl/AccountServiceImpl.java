package com.kaishengit.tms.service.impl;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountExample;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.AccountLoginLogMapper;
import com.kaishengit.tms.mapper.AccountMapper;
import com.kaishengit.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class  AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;

    /**
     *
     * @param accountMobile �ֻ�����
     * @param password  ����
     * @param requestIp ��¼��IP��ַ
     * @return  �����¼�ɹ����򷵻�Account���������¼ʧ�ܷ���null
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
}
