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
 *系统账户业务类
 * @author 马得草
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
     * @param accountMobile 手机号码
     * @param password  密码
     * @param requestIp 登录的IP地址
     * @return  登录成功返回account对象
     * @throws ServiceException 如果登录失败，则通过异常抛出具体的错误原因
     */
    @Override
    public Account login(String accountMobile, String password, String requestIp) throws ServiceException {
        //根据手机号码查找对应的账号
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(accountMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);

        Account account = null;
        if(accountList != null && !accountList.isEmpty()) {

            account = accountList.get(0);
            if(account.getAccountPassword().equals(DigestUtils.md5Hex(password))) {
                //判断状态
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    //添加登录的日志
                    AccountLoginLog loginLog = new AccountLoginLog();
                    loginLog.setAccountId(account.getId());
                    loginLog.setLoginIp(requestIp);
                    loginLog.setLoginTime(new Date());
                    accountLoginLogMapper.insertSelective(loginLog);

                    logger.info("{} 登录系统",account);
                    return account;
                }else if ( Account.STATE_DISABLE.equals(account.getAccountState())) {
                    throw new ServiceException("账号被锁定");
                }else {
                    throw new ServiceException("账号被禁用");
                }
            }else {
                throw new ServiceException("账号或密码错误");
            }

        }else {
            throw new ServiceException("账号或密码错误");
        }
    }


    /*
     *根据uri传来的参数查询所有账户并加载对应的角色列表
     * @author 马得草
     * @date 2018/4/16
     */
    @Override
    public List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam) {
        return accountMapper.findAllWithRolesByQueryParam(requestParam);
    }

    /**
     * 新增账号
     *
     * @param account  account对象
     * @param rolesIds 对应的角色ID
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAccount(Account account, Integer[] rolesIds) {
        account.setCreateTime(new Date());
        //账号默认密码为手机号码的后6位
        String password;
        if(account.getAccountMobile().length() <= 6) {
            password = account.getAccountMobile();
        } else {
            password = account.getAccountMobile().substring(account.getAccountMobile().length()-6,account.getAccountMobile().length());
        }
        //对密码进行MD5加密
        password = DigestUtils.md5Hex(password);

        account.setAccountPassword(password);

        //账号默认状态
        account.setAccountState(Account.STATE_NORMAL);
       int id =  accountMapper.insertSelective(account);

        //添加账号和角色的对应关系表
        for(Integer roleId : rolesIds) {
            AccountRoleKey accountRolesKey = new AccountRoleKey();
            accountRolesKey.setAccountId(account.getId());
            accountRolesKey.setRoleId(roleId);

            accountRoleMapper.insert(accountRolesKey);
        }

    }

    /*
     *根据ID查询account对象
     * @author 马得草
     * @date 2018/4/17
     */
    @Override
    public Account findById(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    /*
     *修改账户
     * @author 马得草
     * @date 2018/4/17
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateAccount(Account account, Integer[] rolesIds) {

        account.setUpdateTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);

        //删除原来的账户 角色关系

        AccountRoleExample accountRoleExample = new AccountRoleExample();
        accountRoleExample.createCriteria().andAccountIdEqualTo(account.getId());
        accountRoleMapper.deleteByExample(accountRoleExample);

        //新增账号-角色关系
        if(rolesIds != null) {
            for(Integer roles : rolesIds) {
                AccountRoleKey accountRoleKey = new AccountRoleKey();
                accountRoleKey.setRoleId(roles);
                accountRoleKey.setAccountId(account.getId());

                accountRoleMapper.insertSelective(accountRoleKey);
            }
        }
        logger.info("修改账号 {}",account);
    }

    /*
     *根据电话号码查找account对象
     * @author 马得草
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
     *保存账户登录信息
     * @author 马得草
     * @date 2018/4/17
     */
    @Override
    public void saveAccountLoginLog(AccountLoginLog accountLoginLog) {
        accountLoginLogMapper.insertSelective(accountLoginLog);
    }

    /*
     *根据accountID删除account对象
     * @author 马得草
     * @date 2018/4/18
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delAccountById(Integer id) throws ServiceException {

        //删除账户和角色的关系记录
        AccountRoleExample accountRoleExample = new AccountRoleExample();
        accountRoleExample.createCriteria().andAccountIdEqualTo(id);
        accountRoleMapper.deleteByExample(accountRoleExample);

        //删除账户
        Account account = accountMapper.selectByPrimaryKey(id);
        accountMapper.deleteByPrimaryKey(id);

        logger.info("删除角色 {}",account);


    }

}

