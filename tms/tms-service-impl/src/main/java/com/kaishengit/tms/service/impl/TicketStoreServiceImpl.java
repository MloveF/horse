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
     *�����µ���Ʊ��
     * @author ��ò�  
     * @date 2018/4/19   
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewTicktStore(TicketStore ticketStore) {
        ticketStore.setCreateTime(new Date());
        ticketStoreMapper.insertSelective(ticketStore);

        //������Ʊ���˺�
        StoreAccount storeAccount = new StoreAccount();
        storeAccount.setStoreAccount(ticketStore.getStoreTel());

        //Ĭ������Ϊ�绰�ĺ�6λ
        storeAccount.setStorePassword(DigestUtils.md5Hex(ticketStore.getStoreTel().substring(5)));
        storeAccount.setCreateTime(new Date());
        //��storeAccount����ticketStore��ID
        storeAccount.setTicketStoreId(ticketStore.getId());
        storeAccount.setStoreState(StoreAccount.ACCOUNT_STATE_NORMAL);
        storeAccount.setTicketStoreId(ticketStore.getId());
        storeAccountMapper.insertSelective(storeAccount);

        //���¹������˻�ID
        ticketStore.setStoreAccountId(storeAccount.getId());
        ticketStoreMapper.updateByPrimaryKeySelective(ticketStore);

    }

    /*
     *���ݵ�ǰҳ��Ͳ�ѯ������ѯ���۵�
     * @author ��ò�
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
     *����ID���Ҷ�Ӧ����Ʊ��
     * @author ��ò�
     * @date 2018/4/20
     */
    @Override
    public TicketStore findTicketStoreById(Integer id) {
        return ticketStoreMapper.selectByPrimaryKey(id);
    }


    /*
     *������Ʊ���StoreAccountId����������Ʊ���˺Ŷ���
     * @author ��ò�
     * @date 2018/4/20
     */
    @Override
    public StoreAccount findStoreAccountById(Integer id) {
        return storeAccountMapper.selectByPrimaryKey(id);
    }


    /*
     *�޸���Ʊ��
     * @author ��ò�
     * @date 2018/4/20
     */
    @Override
    public void updateTicketStore(TicketStore ticketStore) {
        ticketStore.setUpdateTime(new Date());

        //�ж��Ƿ��޸�����ϵ�绰
        StoreAccount storeAccount = storeAccountMapper.selectByPrimaryKey(ticketStore.getStoreAccountId());
        if(!ticketStore.getStoreTel().equals(storeAccount.getStoreAccount())) {
            //����޸�����Ʊ��ĵ绰����Ҫ�޸Ķ�Ӧ���˻�
            storeAccount.setStoreAccount(ticketStore.getStoreTel());
            //�����������벢����
            storeAccount.setStorePassword(DigestUtils.md5Hex(ticketStore.getStoreTel().substring(5)));
            storeAccount.setUpdateTime(new Date());
            storeAccountMapper.updateByPrimaryKeySelective(storeAccount);

        }
        ticketStoreMapper.updateByPrimaryKeySelective(ticketStore);
    }


    /*
     *��StroeAccount��״̬�޸�Ϊ���ã�disable��
     * @author ��ò�
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
     *����TicketStore��ID���Ҷ�Ӧ��StoreAccount����
     * @author ��ò�
     * @date 2018/4/23
     */
    @Override
    public StoreAccount findStoreAccountByTicketStoreId(Integer id) {
       return storeAccountMapper.selectStoreAccountByTicketStoreId(id);

    }


    /*
     *��StroeAccount��״̬�޸�Ϊ������normal��
     * @author ��ò�
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
     *������Ʊ���IDɾ����Ʊ�����Ʊ�˻�
     * @author ��ò�
     * @date 2018/4/23
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delTicketStoreWithAccountById(Integer id) {
        //ɾ����Ʊ��
        ticketStoreMapper.deleteByPrimaryKey(id);
        //ɾ����Ӧ���˻�
        storeAccountMapper.deletStoreAccountByTicketStoreId(id);


    }

    /**
     * ��ѯ���е���Ʊ��
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
     * �����˺ţ��ֻ����룩������Ʊ���¼�˺Ŷ���
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
    *������Ʊ��ĵ�¼��־
    * @date 2018/4/27
    * @param
    * @return
    */
    @Override
    public void saveStoreAccountLoginLog(StoreLoginLog storeLoginLog) {
        storeLoginLogMapper.insertSelective(storeLoginLog);
    }
}
