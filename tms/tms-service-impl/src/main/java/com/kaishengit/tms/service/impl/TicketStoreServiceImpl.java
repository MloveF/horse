package com.kaishengit.tms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.*;
import com.kaishengit.tms.mapper.StoreAccountMapper;
import com.kaishengit.tms.mapper.StoreLoginLogMapper;
import com.kaishengit.tms.mapper.TicketStoreMapper;
import com.kaishengit.tms.service.TicketStoreService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TicketStoreServiceImpl implements TicketStoreService {

    @Autowired
    private TicketStoreMapper ticketStoreMapper;

    @Autowired
    private StoreAccountMapper storeAccountMapper;

    @Autowired
    private StoreLoginLogMapper storeLoginLogMapper;
    
    /*   
     *创建新的售票点
     * @author 马得草  
     * @date 2018/4/19   
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewTicktStore(TicketStore ticketStore) {
        ticketStore.setCreateTime(new Date());
        ticketStoreMapper.insertSelective(ticketStore);

        //创建售票点账号
        StoreAccount storeAccount = new StoreAccount();
        storeAccount.setStoreAccount(ticketStore.getStoreTel());

        //默认密码为电话的后6位
        storeAccount.setStorePassword(DigestUtils.md5Hex(ticketStore.getStoreTel().substring(5)));
        storeAccount.setCreateTime(new Date());
        //在storeAccount存入ticketStore的ID
        storeAccount.setTicketStoreId(ticketStore.getId());
        storeAccount.setStoreState(StoreAccount.ACCOUNT_STATE_NORMAL);
        storeAccount.setTicketStoreId(ticketStore.getId());
        storeAccountMapper.insertSelective(storeAccount);

        //更新关联的账户ID
        ticketStore.setStoreAccountId(storeAccount.getId());
        ticketStoreMapper.updateByPrimaryKeySelective(ticketStore);

    }

    /*
     *根据当前页面和查询参数查询销售点
     * @author 马得草
     * @date 2018/4/20
     */
    @Override
    public PageInfo<TicketStore> findAllTicketStoreByPage(Integer pageNo, Map<String, Object> queryParam) {
        PageHelper.startPage(pageNo,10);

        String storeName = (String) queryParam.get("storeName");
        String storeManager = (String) queryParam.get("storeManager");
        String storeTel = (String) queryParam.get("storeTel");

        TicketStoreExample ticketStoreExample = new TicketStoreExample();
        TicketStoreExample.Criteria criteria = ticketStoreExample.createCriteria();

        if(StringUtils.isNotEmpty(storeName)) {
            criteria.andStoreNameLike("%"+ storeName+"%");
        }
        if(StringUtils.isNotEmpty(storeManager)) {
            criteria.andStoreManagerLike("%"+ storeManager +"%");
        }
        if(StringUtils.isNotEmpty(storeTel)) {
            criteria.andStoreTelLike("%"+ storeTel +"%");
        }

        ticketStoreExample.setOrderByClause("id desc");

        List<TicketStore> ticketStoreList = ticketStoreMapper.selectByExample(ticketStoreExample);
        return new PageInfo<>(ticketStoreList);
    }


    /*
     *根据ID查找对应的售票点
     * @author 马得草
     * @date 2018/4/20
     */
    @Override
    public TicketStore findTicketStoreById(Integer id) {
        return ticketStoreMapper.selectByPrimaryKey(id);
    }


    /*
     *根据售票点的StoreAccountId主键查找售票点账号对象
     * @author 马得草
     * @date 2018/4/20
     */
    @Override
    public StoreAccount findStoreAccountById(Integer id) {
        return storeAccountMapper.selectByPrimaryKey(id);
    }


    /*
     *修改售票点
     * @author 马得草
     * @date 2018/4/20
     */
    @Override
    public void updateTicketStore(TicketStore ticketStore) {
        ticketStore.setUpdateTime(new Date());

        //判断是否修改了联系电话
        StoreAccount storeAccount = storeAccountMapper.selectByPrimaryKey(ticketStore.getStoreAccountId());
        if(!ticketStore.getStoreTel().equals(storeAccount.getStoreAccount())) {
            //如果修改了售票点的电话则需要修改对应的账户
            storeAccount.setStoreAccount(ticketStore.getStoreTel());
            //重新设置密码并加密
            storeAccount.setStorePassword(DigestUtils.md5Hex(ticketStore.getStoreTel().substring(5)));
            storeAccount.setUpdateTime(new Date());
            storeAccountMapper.updateByPrimaryKeySelective(storeAccount);

        }
        ticketStoreMapper.updateByPrimaryKeySelective(ticketStore);
    }


    /*
     *把StroeAccount的状态修改为禁用（disable）
     * @author 马得草
     * @date 2018/4/22
     */
    @Override
    public void editStroeAccountState(Integer id) {
      TicketStore ticketStore = ticketStoreMapper.selectByPrimaryKey(id);
      StoreAccount storeAccount = storeAccountMapper.selectByPrimaryKey(ticketStore.getStoreAccountId());

      storeAccount.setStoreState("disable");


      storeAccountMapper.updateByPrimaryKeySelective(storeAccount);

    }


    /*
     *根据TicketStore的ID查找对应的StoreAccount对象
     * @author 马得草
     * @date 2018/4/23
     */
    @Override
    public StoreAccount findStoreAccountByTicketStoreId(Integer id) {
       return storeAccountMapper.selectStoreAccountByTicketStoreId(id);

    }


    /*
     *把StroeAccount的状态修改为正常（normal）
     * @author 马得草
     * @date 2018/4/23
     */
    @Override
    public void normalAccountState(Integer id) {
        TicketStore ticketStore = ticketStoreMapper.selectByPrimaryKey(id);
        StoreAccount storeAccount = storeAccountMapper.selectByPrimaryKey(ticketStore.getStoreAccountId());

        storeAccount.setStoreState("normal");


        storeAccountMapper.updateByPrimaryKeySelective(storeAccount);


    }


    /*
     *根据售票点的ID删除售票点和售票账户
     * @author 马得草
     * @date 2018/4/23
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delTicketStoreWithAccountById(Integer id) {
        //删除售票点
        ticketStoreMapper.deleteByPrimaryKey(id);
        //删除对应的账户
        storeAccountMapper.deletStoreAccountByTicketStoreId(id);


    }

    /**
     * 查询所有的售票点
     *
     * @return
     */
    @Override
    public List<TicketStore> findAllTicketStore() {

        TicketStoreExample ticketStoreExample = new TicketStoreExample();
        ticketStoreExample.setOrderByClause("id desc");
        return ticketStoreMapper.selectByExample(ticketStoreExample);
    }

    /**
     * 根据账号（手机号码）查找售票点登录账号对象
     *
     * @param name@return
     */
    @Override
    public StoreAccount findStoreAccountByName(String name) {
        StoreAccountExample example = new StoreAccountExample();
        example.createCriteria().andStoreAccountEqualTo(name);

        List<StoreAccount> storeAccountList = storeAccountMapper.selectByExample(example);
        if(storeAccountList != null && !storeAccountList.isEmpty()) {
            return storeAccountList.get(0);
        }
        return null;
    }

   /*
    *保存售票点的登录日志
    * @date 2018/4/27
    * @param
    * @return
    */
    @Override
    public void saveStoreAccountLoginLog(StoreLoginLog storeLoginLog) {
        storeLoginLogMapper.insertSelective(storeLoginLog);
    }
}
