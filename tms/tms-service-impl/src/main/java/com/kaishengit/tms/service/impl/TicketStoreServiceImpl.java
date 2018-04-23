package com.kaishengit.tms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.tms.entity.StoreAccount;
import com.kaishengit.tms.entity.TicketStore;
import com.kaishengit.tms.entity.TicketStoreExample;
import com.kaishengit.tms.mapper.StoreAccountMapper;
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
}
