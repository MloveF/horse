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
}
